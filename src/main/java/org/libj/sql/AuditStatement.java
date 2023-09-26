/* Copyright (c) 2009 LibJ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.sql;

import static org.libj.sql.AuditUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.libj.lang.Strings;
import org.libj.logging.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * A {@link Statement} that delegates all method calls to another statement. This class overrides all execution methods in order to
 * log the SQL that is executed. When an "execute" method is invoked, it will be logged to the logger associated with the
 * {@link AuditStatement} class.
 */
public class AuditStatement implements DelegateStatement {
  private static final Logger logger = LoggerFactory.getLogger(AuditStatement.class);

  /**
   * Releases the specified {@link Statement} object's database and JDBC resources immediately instead of waiting for them to be
   * automatically released.
   * <p>
   * This method differs itself from {@link Statement#close()} by not throwing a {@link SQLException} if a database access error
   * occurs. If a database access error occurs, a warning will be logged to the logger associated with the {@link AuditStatement}
   * class.
   *
   * @param statement The {@link Statement} to close.
   * @return {@code null} if the {@link ResultSet#close()} operation is successful, otherwise the {@link SQLException} that caused the
   *         failure.
   * @throws NullPointerException If {@code statement} is null.
   */
  public static SQLException close(final Statement statement) {
    try {
      if (!statement.isClosed())
        statement.close();

      return null;
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled()) { logger.warn(statement.getClass().getName() + ".close(): " + e.getMessage()); }
      return e;
    }
  }

  /**
   * Returns a {@link AuditStatement} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   * {@link Statement}.
   *
   * @param target The {@link Statement} to wrap.
   * @return A {@link AuditStatement} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   *         {@link Statement}.
   */
  public static Statement wrapIfDebugEnabled(final Statement target) {
    return logger.isDebugEnabled() ? new AuditStatement(target) : target;
  }

  private final Statement target;

  /**
   * Creates a new {@link AuditStatement} with the specified {@code target} to which all method calls will be delegated.
   *
   * @param target The {@link Statement} to which all method calls will be delegated.
   */
  public AuditStatement(final Statement target) {
    this.target = target;
  }

  @Override
  public Statement getTarget() {
    return target;
  }

  static StringBuilder withResult(final StringBuilder builder, final Object result, final long time) {
    builder.append(" -> ").append(result).append("\t\t").append(System.currentTimeMillis() - time).append("ms");
    return builder;
  }

  @Override
  public ResultSet executeQuery(final String sql) throws SQLException {
    long time = -1;
    int size = -1;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "executeQuery", getConnection(), sql).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      final ResultSet resultSet = getTarget().executeQuery(sql);
      if (LoggerUtil.isLoggable(logger, Level.DEBUG))
        size = ResultSets.getSize(resultSet);

      return resultSet;
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "executeQuery", getConnection(), sql), size, time).toString()); }
    }
  }

  @Override
  public int executeUpdate(final String sql) throws SQLException {
    long time = -1;
    int count = -1;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "executeUpdate", getConnection(), sql).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return count = getTarget().executeUpdate(sql);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "executeUpdate", getConnection(), sql), count, time).toString()); }
    }
  }

  @Override
  public void close() throws SQLException {
    try {
      final Statement target = getTarget();
      if (!target.isClosed())
        target.close();

      if (batch != null) {
        batch.clear();
        batch = null;
      }
    }
    catch (final SQLException e) {
      if (!"Connection is closed.".equals(e.getMessage()))
        throw e;
    }
  }

  @Override
  public boolean execute(final String sql) throws SQLException {
    long time = -1;
    Boolean result = null;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "execute", getConnection(), sql).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return result = getTarget().execute(sql);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "execute", getConnection(), sql), result, time).toString()); }
    }
  }

  private ArrayList<String> batch;

  protected final void addBatch0(final String sql) {
    if (batch == null)
      batch = new ArrayList<>();

    batch.add(sql);
  }

  @Override
  public void addBatch(final String sql) throws SQLException {
    if (logger.isDebugEnabled())
      addBatch0(sql);

    getTarget().addBatch(sql);
  }

  @Override
  public void clearBatch() throws SQLException {
    getTarget().clearBatch();
    if (batch != null)
      batch.clear();
  }

  private String logExecuteBatch(final int[] count, final Long time) throws SQLException {
    final StringBuilder builder = log(this, "executeBatch", getConnection(), null);
    builder.setLength(builder.length() - 3);
    builder.append('[');
    if (batch != null) {
      if (count != null)
        for (int i = 0, i$ = batch.size(); i < i$; ++i) // [RA]
          builder.append("\n ").append(Strings.indent(batch.get(i), 2)).append(" -> ").append(count[i]);
      else
        for (int i = 0, i$ = batch.size(); i < i$; ++i) // [RA]
          builder.append("\n ").append(Strings.indent(batch.get(i), 2)).append(" -> -1");
    }

    builder.append("\n])");
    if (time != null)
      builder.append(' ').append(System.currentTimeMillis() - time).append("ms");

    return builder.toString();
  }

  @Override
  public int[] executeBatch() throws SQLException {
    long time = -1;
    int[] count = null;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(logExecuteBatch(count, null)); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return count = getTarget().executeBatch();
    }
    finally {
      if (debugEnabled) { logger.debug(logExecuteBatch(count, time)); }
    }
  }

  @Override
  public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
    long time = -1;
    int count = -1;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "executeUpdate", getConnection(), sql).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return count = getTarget().executeUpdate(sql, autoGeneratedKeys);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "executeUpdate", getConnection(), sql).append("\n,  ").append(autoGeneratedKeys), count, time).toString()); }
    }
  }

  @Override
  public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
    long time = -1;
    int count = -1;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "executeUpdate", getConnection(), sql).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return count = getTarget().executeUpdate(sql, columnIndexes);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "executeUpdate", getConnection(), sql).append("\n, [").append(Arrays.toString(columnIndexes)).append(']'), count, time).toString()); }
    }
  }

  @Override
  public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
    long time = -1;
    int count = -1;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "executeUpdate", getConnection(), sql + "\n,  " + Arrays.toString(columnNames)).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return count = getTarget().executeUpdate(sql, columnNames);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "executeUpdate", getConnection(), sql + "\n,  " + Arrays.toString(columnNames)), count, time).toString()); }
    }
  }

  @Override
  public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
    long time = -1;
    Boolean result = null;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "execute", getConnection(), sql + "\n,  " + autoGeneratedKeys).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return result = getTarget().execute(sql, autoGeneratedKeys);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "execute", getConnection(), sql + "\n,  " + autoGeneratedKeys), result, time).toString()); }
    }
  }

  @Override
  public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
    long time = -1;
    Boolean result = null;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "execute", getConnection(), sql + "\n,  " + Arrays.toString(columnIndexes)).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return result = getTarget().execute(sql, columnIndexes);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "execute", getConnection(), sql + "\n,  " + Arrays.toString(columnIndexes)), result, time).toString()); }
    }
  }

  @Override
  public boolean execute(final String sql, final String[] columnNames) throws SQLException {
    long time = -1;
    Boolean result = null;
    final boolean debugEnabled = logger.isDebugEnabled();
    try {
      if (logger.isTraceEnabled()) { logger.trace(log(this, "execute", getConnection(), sql + "\n,  " + Arrays.toString(columnNames)).toString()); }

      if (debugEnabled)
        time = System.currentTimeMillis();

      return result = getTarget().execute(sql, columnNames);
    }
    finally {
      if (debugEnabled) { logger.debug(withResult(log(this, "execute", getConnection(), sql + "\n,  " + Arrays.toString(columnNames)), result, time).toString()); }
    }
  }

  @Override
  public boolean equals(final Object obj) {
    return getTarget().equals(obj);
  }

  @Override
  public int hashCode() {
    return getTarget().hashCode();
  }

  @Override
  public String toString() {
    return getTarget().toString();
  }
}