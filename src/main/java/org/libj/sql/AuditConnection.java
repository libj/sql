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
public class AuditConnection extends Audit implements DelegateConnection {
  private static final Logger logger = LoggerFactory.getLogger(AuditConnection.class);
  private static final boolean traceOpenConnections = Systems.hasProperty("org.libj.sql.AuditConnection.trace");

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
      return AuditConnection.this + "\nAge: " + (System.currentTimeMillis() - timestamp) + "\n" + stackTrace;
    }
  }

  private static final IdentityHashMap<AuditConnection,Trace> openConnections = traceOpenConnections ? new IdentityHashMap<>() : null;

  /**
   * Print a log of the open connections to the provided {@link Consumer}.
   *
   * @param c The {@link Consumer} by which a log of open connections is to be accepted.
   * @implSpec This only works if {@code -Dorg.libj.sql.AuditConnection.trace} is specified as a system property.
   */
  public static void traceOpenConnections(final Consumer<String> c) {
    if (traceOpenConnections && openConnections.size() > 0) {
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
   * @return {@code true} or {@code false} if the {@link Connection#isClosed()} operation is successful, otherwise {@code null} if a
   *         {@link SQLException} was encountered.
   * @throws NullPointerException If {@code connection} is null.
   */
  public static Boolean isClosed(final Connection connection) {
    try {
      return connection.isClosed();
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled()) { logger.warn(connection.getClass().getName() + ".isClosed(): " + e.getMessage()); }
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
   * @return {@code null} if the {@link Connection#close()} operation is successful, otherwise the {@link SQLException} that caused
   *         the failure.
   * @throws NullPointerException If {@code connection} is null.
   */
  public static SQLException close(final Connection connection) {
    try {
      if (!connection.isClosed())
        connection.close();

      return null;
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled()) { logger.warn(connection.getClass().getName() + ".close(): " + e.getMessage()); }
      return e;
    }
  }

  /**
   * Returns a {@link AuditConnection} if {@code DEBUG} level logging is enabled, or if the {@code org.libj.sql.AuditConnection.trace}
   * system property is specified. Otherwise, returns the provided target {@link Connection}.
   *
   * @param target The {@link Connection} to wrap.
   * @return A {@link AuditConnection} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   *         {@link Connection}.
   */
  public static Connection wrapIfDebugEnabled(final Connection target) {
    return traceOpenConnections || logger.isDebugEnabled() ? new AuditConnection(target) : target;
  }

  private Connection target;

  /**
   * Creates a new {@link AuditConnection} with the specified {@code target} to which all method calls will be delegated.
   *
   * @param target The {@link Connection} to which all method calls will be delegated.
   * @throws NullPointerException If the target {@link Connection} is null.
   */
  public AuditConnection(final Connection target) {
    this.target = target;
  }

  /**
   * Returns a log entry for the provided parameters.
   *
   * @param enabled If {@code true}, this method will return a log entry for the provided parameters. Otherwise if {@code false}, this
   *          method will return {@code null}.
   * @param method The name of the method for which the log entry is being created.
   * @param newLine If {@code true}, a newline character {@code '\n'} will be appended before the {@code sql} string. If
   *          {@code false}, no newline character will be appended before the {@code sql} string.
   * @param sql The SQL statement.
   * @param autoGeneratedKeys A flag indicating whether auto-generated keys should be returned; one of
   *          {@link Statement#RETURN_GENERATED_KEYS}, {@link Statement#NO_GENERATED_KEYS}, or {@code Integer.MIN_VALUE} for the
   *          parameter to be omitted.
   * @param columnIndexes An array of column indexes indicating the columns that should be returned from the inserted row or rows, or
   *          {@code null} for the parameter to be omitted.
   * @param columnNames An array of column names indicating the columns that should be returned from the inserted row or rows, or
   *          {@code null} for the parameter to be omitted.
   * @param resultSetType One of the following ResultSet constants: {@link ResultSet#TYPE_FORWARD_ONLY},
   *          {@link ResultSet#TYPE_SCROLL_INSENSITIVE}, {@link ResultSet#TYPE_SCROLL_SENSITIVE}, or {@code Integer.MIN_VALUE} for the
   *          parameter to be omitted.
   * @param resultSetConcurrency One of the following ResultSet constants: {@link ResultSet#CONCUR_READ_ONLY},
   *          {@link ResultSet#CONCUR_UPDATABLE}, or {@code Integer.MIN_VALUE} for the parameter to be omitted.
   * @param resultSetHoldability One of the following ResultSet constants: {@link ResultSet#HOLD_CURSORS_OVER_COMMIT},
   *          {@link ResultSet#CLOSE_CURSORS_AT_COMMIT}, or {@code Integer.MIN_VALUE} for the parameter to be omitted.
   * @return A log entry for the provided parameters.
   */
  protected String log(final boolean enabled, final String method, final boolean newLine, final String sql, final int autoGeneratedKeys, final int[] columnIndexes, final String[] columnNames, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
    if (!enabled)
      return null;

    final StringBuilder b = AuditUtil.log(this, method, this, newLine, new StringBuilder(sql));
    if (autoGeneratedKeys != Integer.MIN_VALUE) {
      b.append(", ").append(autoGeneratedKeys);
    }
    else {
      if (columnIndexes != null) {
        for (final int columnIndex : columnIndexes) // [A]
          b.append(", ").append(columnIndex);
      }
      else if (columnNames != null) {
        for (final String columnName : columnNames) // [A]
          b.append(", ").append(columnName);
      }
      else {
        if (resultSetType != Integer.MIN_VALUE)
          b.append(", ").append(resultSetType);

        if (resultSetConcurrency != Integer.MIN_VALUE)
          b.append(", ").append(resultSetConcurrency);

        if (resultSetHoldability != Integer.MIN_VALUE)
          b.append(", ").append(resultSetHoldability);
      }
    }

    return b.append("\n)").toString();
  }

  @Override
  public Connection getTarget() {
    return target;
  }

  @Override
  protected Logger logger() {
    return logger;
  }

  /**
   * Wraps the provided {@link Statement}.
   * <p>
   * This method is called by this {@link AuditConnection} for each newly created {@link Statement}.
   * <p>
   * This method can be overridden to support custom wrapper implementations, and must not return {@code null}.
   *
   * @param statement The {@link Statement} to wrap.
   * @return A {@link Statement} instance.
   * @see #createStatement()
   * @see #createStatement(int,int)
   * @see #createStruct(String,Object[])
   */
  protected Statement wrap(final Statement statement) {
    return AuditStatement.wrapIfDebugEnabled(statement);
  }

  /**
   * Wraps the provided {@link PreparedStatement}.
   * <p>
   * This method is called by this {@link AuditConnection} for each newly created {@link PreparedStatement}.
   * <p>
   * This method can be overridden to support custom wrapper implementations, and must not return {@code null}.
   *
   * @param statement The {@link PreparedStatement} to wrap.
   * @param sql A SQL statement to be sent to the database; may contain one or more '?' parameters.
   * @return A {@link PreparedStatement} instance.
   * @see #prepareStatement(String)
   * @see #prepareStatement(String,int)
   * @see #prepareStatement(String,int[])
   * @see #prepareStatement(String,String[])
   * @see #prepareStatement(String,int,int,int)
   * @see #prepareStatement(String,int,int)
   */
  protected PreparedStatement wrap(final PreparedStatement statement, final String sql) {
    return AuditPreparedStatement.wrapIfDebugEnabled(statement, sql);
  }

  /**
   * Wraps the provided {@link CallableStatement}.
   * <p>
   * This method is called by this {@link AuditConnection} for each newly created {@link CallableStatement}.
   * <p>
   * This method can be overridden to support custom wrapper implementations, and must not return {@code null}.
   *
   * @param statement The {@link CallableStatement} to wrap.
   * @param sql A SQL statement to be sent to the database; may contain one or more '?' parameters.
   * @return A {@link CallableStatement} instance.
   * @see #prepareCall(String)
   * @see #prepareCall(String,int,int)
   * @see #prepareCall(String,int,int,int)
   */
  protected CallableStatement wrap(final CallableStatement statement, final String sql) {
    return AuditCallableStatement.wrapIfDebugEnabled(statement, sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link Statement} instance provided by {@link #wrap(Statement)}.
   */
  @Override
  public Statement createStatement() throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    return wrap(target.createStatement());
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link PreparedStatement} instance provided by {@link #wrap(PreparedStatement,String)}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "executeQuery", true, sql, Integer.MIN_VALUE, null, null, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), sql);
    return wrap(target.prepareStatement(sql), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link Statement} instance provided by {@link #wrap(Statement)}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    return wrap(target.createStatement(resultSetType, resultSetConcurrency));
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link PreparedStatement} instance provided by {@link #wrap(PreparedStatement,String)}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareStatement", true, sql, Integer.MIN_VALUE, null, null, resultSetType, resultSetConcurrency, Integer.MIN_VALUE), sql);
    return wrap(target.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link CallableStatement} instance provided by {@link #wrap(CallableStatement,String)}.
   */
  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareCall", true, sql, Integer.MIN_VALUE, null, null, resultSetType, resultSetConcurrency, Integer.MIN_VALUE), sql);
    return wrap(target.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link Statement} instance provided by {@link #wrap(Statement)}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    return wrap(target.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link PreparedStatement} instance provided by {@link #wrap(PreparedStatement,String)}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareStatement", true, sql, Integer.MIN_VALUE, null, null, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    return wrap(target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link CallableStatement} instance provided by {@link #wrap(CallableStatement,String)}.
   */
  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareCall", true, sql, Integer.MIN_VALUE, null, null, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    return wrap(target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link PreparedStatement} instance provided by {@link #wrap(PreparedStatement,String)}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareStatement", true, sql, autoGeneratedKeys, null, null, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), sql);
    return wrap(target.prepareStatement(sql, autoGeneratedKeys), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link PreparedStatement} instance provided by {@link #wrap(PreparedStatement,String)}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareStatement", true, sql, Integer.MIN_VALUE, columnIndexes, null, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), sql);
    return wrap(target.prepareStatement(sql, columnIndexes), sql);
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method returns a {@link PreparedStatement} instance provided by {@link #wrap(PreparedStatement,String)}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
    if (traceOpenConnections)
      openConnections.put(this, new Trace());

    trace(null, log(isTraceEnabled(), "prepareStatement", true, sql, Integer.MIN_VALUE, null, columnNames, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), sql);
    return wrap(target.prepareStatement(sql, columnNames), sql);
  }

  @Override
  public void close() throws SQLException {
    if (traceOpenConnections)
      openConnections.remove(this);

    DelegateConnection.super.close();
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