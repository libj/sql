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

import static org.libj.lang.Assertions.*;
import static org.libj.sql.AuditUtil.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.function.Consumer;

import org.libj.lang.Numbers;
import org.libj.lang.Strings;
import org.libj.lang.Strings.Align;
import org.libj.lang.Systems;
import org.libj.lang.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Connection} that delegates all method calls to another connection. The sole purpose of this class is to override all
 * "create" and "prepare" methods to return "Audit" implementations of the respective return types.
 *
 * @see AuditStatement
 * @see AuditPreparedStatement
 * @see AuditCallableStatement
 */
public class AuditConnection extends DelegateConnection {
  private static final Logger logger = LoggerFactory.getLogger(AuditConnection.class);
  private static final boolean trace = Systems.hasProperty("org.libj.sql.AuditConnection.trace");

  private class Trace implements Comparable<Trace> {
    private final String stackTrace;
    private final long timestamp;

    private Trace() {
      this.stackTrace = Throwables.toString(new Exception());
      this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(final Trace o) {
      return Long.compare(timestamp, o.timestamp);
    }

    @Override
    public String toString() {
      return AuditConnection.this.toString() + "\n" + "Age: " + (System.currentTimeMillis() - timestamp) + "\n" + stackTrace;
    }
  }

  private static final IdentityHashMap<AuditConnection,Trace> openConnections = trace ? new IdentityHashMap<>() : null;

  /**
   * Print a log of the open connections to the provided {@link Consumer}.
   *
   * @param c The {@link Consumer} by which a log of open connections is to be accepted.
   * @implSpec This only works if {@code -Dorg.libj.sql.AuditConnection.trace} is specified as a system property.
   */
  public static void traceOpenConnections(final Consumer<String> c) {
    if (trace && openConnections.size() > 0) {
      final ArrayList<Trace> list = new ArrayList<>(openConnections.values());
      list.sort(null);

      for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
        if (i > 0)
          c.accept("\n");

        c.accept(Strings.pad(String.valueOf(i), Align.RIGHT, Numbers.precision(i$)) + ") " + list.get(i).toString().replace("\n", "\n    "));
      }
    }
  }

  /**
   * Retrieves whether this {@link Connection} object has been closed. A connection is closed if the method {@code close} has been
   * called on it or if certain fatal errors have occurred. This method is guaranteed to return {@code true} only when it is called
   * after the method {@code Connection.close} has been called.
   * <p>
   * This method differs itself from {@link Connection#isClosed()} by not throwing a {@link SQLException} if a database access error
   * occurs. If a database access error occurs, a warning will be logged to the logger associated with the {@link AuditConnection}
   * class.
   *
   * @param connection The connection to be checked if closed.
   * @return {@code true} or {@code false} if the {@link ResultSet#isClosed()} operation is successful, otherwise {@code null} if a
   *         {@link SQLException} was encountered.
   * @throws IllegalArgumentException If {@code connection} is null.
   */
  public static Boolean isClosed(final Connection connection) {
    try {
      return assertNotNull(connection).isClosed();
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled())
        logger.warn(connection.getClass().getName() + ".isClosed(): " + e.getMessage());

      return null;
    }
  }

  /**
   * Releases the specified {@link Connection} object's database and JDBC resources immediately instead of waiting for them to be
   * automatically released.
   * <p>
   * This method differs itself from {@link Connection#close()} by not throwing a {@link SQLException} if a database access error
   * occurs. If a database access error occurs, a warning will be logged to the logger associated with the {@link AuditConnection}
   * class.
   *
   * @param connection The connection to be closed.
   * @return {@code null} if the {@link ResultSet#close()} operation is successful, otherwise the {@link SQLException} that caused
   *         the failure.
   * @throws IllegalArgumentException If {@code connection} is null.
   */
  public static SQLException close(final Connection connection) {
    try {
      if (!assertNotNull(connection).isClosed())
        connection.close();

      return null;
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled())
        logger.warn(connection.getClass().getName() + ".close(): " + e.getMessage());

      return e;
    }
  }

  /**
   * Creates a new {@link AuditConnection} with the specified {@code target} to which all method calls will be delegated.
   *
   * @param target The {@link Connection} to which all method calls will be delegated.
   */
  public AuditConnection(final Connection target) {
    super(target);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditStatement} instance that delegates all method calls to the underlying
   *           {@link Statement}.
   */
  @Override
  public Statement createStatement() throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    return new AuditStatement(target.createStatement());
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditPreparedStatement} instance that delegates all method calls to the underlying
   *           {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareStatement", this, sql).toString());

    return new AuditPreparedStatement(target.prepareStatement(sql), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditStatement} instance that delegates all method calls to the underlying
   *           {@link Statement}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    return new AuditStatement(target.createStatement(resultSetType, resultSetConcurrency));
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditPreparedStatement} instance that delegates all method calls to the underlying
   *           {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareStatement", this, sql).toString());

    return new AuditPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditCallableStatement} instance that delegates all method calls to the underlying
   *           {@link CallableStatement}.
   */
  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareCall", this, sql).toString());

    return new AuditCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditStatement} instance that delegates all method calls to the underlying
   *           {@link Statement}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    return new AuditStatement(target.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditPreparedStatement} instance that delegates all method calls to the underlying
   *           {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareStatement", this, sql).toString());

    return new AuditPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditCallableStatement} instance that delegates all method calls to the underlying
   *           {@link CallableStatement}.
   */
  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareCall", this, sql).toString());

    return new AuditCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditPreparedStatement} instance that delegates all method calls to the underlying
   *           {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareStatement", this, sql).toString());

    return new AuditPreparedStatement(target.prepareStatement(sql, autoGeneratedKeys), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditPreparedStatement} instance that delegates all method calls to the underlying
   *           {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareStatement", this, sql).toString());

    return new AuditPreparedStatement(target.prepareStatement(sql, columnIndexes), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns an {@link AuditPreparedStatement} instance that delegates all method calls to the underlying
   *           {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
    if (trace)
      openConnections.put(this, new Trace());

    if (logger.isTraceEnabled())
      logger.trace(log(this, "prepareStatement", this, sql).toString());

    return new AuditPreparedStatement(target.prepareStatement(sql, columnNames), sql);
  }

  @Override
  public void close() throws SQLException {
    if (trace)
      openConnections.remove(this);

    super.close();
  }
}