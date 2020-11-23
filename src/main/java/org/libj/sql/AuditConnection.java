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

import static org.libj.sql.Util.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.libj.lang.Numbers;
import org.libj.lang.Strings;
import org.libj.lang.Strings.Align;
import org.libj.util.IdentityHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Connection} that delegates all method calls to another connection.
 * The sole purpose of this class is to override all "create" and "prepare"
 * methods to return "Audit" implementations of the respective return types.
 *
 * @see AuditStatement
 * @see AuditPreparedStatement
 * @see AuditCallableStatement
 */
public class AuditConnection extends DelegateConnection {
  private static final Logger logger = LoggerFactory.getLogger(AuditConnection.class);
  private static final boolean trace;

  static {
    final String traceProp = System.getProperty("org.libj.sql.AuditConnection.trace");
    trace = traceProp != null && !traceProp.equals("false");
  }

  private static final IdentityHashSet<AuditConnection> openConnections = trace ? new IdentityHashSet<>() : null;

  /**
   * Print a log of the open connections to {@code stderr}.
   * <p>
   * <b>Note:</b> This only works if
   * {@code -Dorg.libj.sql.AuditConnection.trace} is specified as a system
   * property.
   */
  public static void traceOpenConnections() {
    if (trace) {
      final int size = openConnections.size();
      if (size > 0) {
        final Iterator<AuditConnection> iterator = openConnections.iterator();
        final StringBuffer builder = new StringBuffer();
        for (int i = 0; iterator.hasNext(); ++i) {
          if (i > 0)
            builder.append('\n');

          builder.append(Strings.pad(String.valueOf(i), Align.RIGHT, Numbers.precision(size)) + ") " + iterator.next());
        }

        System.err.println(builder);
      }
    }
  }

  /**
   * Releases the specified {@link Connection} object's database and JDBC
   * resources immediately instead of waiting for them to be automatically
   * released.
   * <p>
   * This method differs itself from {@link Connection#close()} by not throwing
   * a {@link SQLException} if a database access error occurs. If a database
   * access error occurs, a warning will be logged to the logger associated with
   * the {@link AuditConnection} class.
   *
   * @param connection The connection to be closed.
   * @return {@code null} if the {@link ResultSet#close()} operation is
   *         successful, otherwise the {@link SQLException} that caused the
   *         failure.
   * @throws NullPointerException If {@code connection} is null.
   */
  public static SQLException close(final Connection connection) {
    try {
      if (!connection.isClosed())
        connection.close();

      return null;
    }
    catch (final SQLException e) {
      logger.warn(connection.getClass().getName() + ".close(): " + e.getMessage());
      return e;
    }
  }

  /**
   * Creates a new {@link AuditConnection} with the specified {@code target} to
   * which all method calls will be delegated.
   *
   * @param target The {@link Connection} to which all method calls will be
   *          delegated.
   */
  public AuditConnection(final Connection target) {
    super(target);
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditStatement} instance that
   * delegates all method calls to the underlying {@link Statement}.
   */
  @Override
  public Statement createStatement() throws SQLException {
    if (trace)
      openConnections.add(this);

    return new AuditStatement(target.createStatement());
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditPreparedStatement} instance
   * that delegates all method calls to the underlying
   * {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareStatement", toString()).toString());

      return new AuditPreparedStatement(target.prepareStatement(sql), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareStatement", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditStatement} instance that
   * delegates all method calls to the underlying {@link Statement}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (trace)
      openConnections.add(this);

    return new AuditStatement(target.createStatement(resultSetType, resultSetConcurrency));
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditPreparedStatement} instance
   * that delegates all method calls to the underlying
   * {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareStatement", toString()).toString());

      return new AuditPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareStatement", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditCallableStatement} instance
   * that delegates all method calls to the underlying
   * {@link CallableStatement}.
   */
  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareCall", toString()).toString());

      return new AuditCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareCall", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditStatement} instance that
   * delegates all method calls to the underlying {@link Statement}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (trace)
      openConnections.add(this);

    return new AuditStatement(target.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditPreparedStatement} instance
   * that delegates all method calls to the underlying
   * {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareStatement", toString()).toString());

      return new AuditPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareStatement", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditCallableStatement} instance
   * that delegates all method calls to the underlying
   * {@link CallableStatement}.
   */
  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareCall", toString()).toString());

      return new AuditCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareCall", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditPreparedStatement} instance
   * that delegates all method calls to the underlying
   * {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareStatement", toString()).toString());

      return new AuditPreparedStatement(target.prepareStatement(sql, autoGeneratedKeys), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareStatement", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditPreparedStatement} instance
   * that delegates all method calls to the underlying
   * {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareStatement", toString()).toString());

      return new AuditPreparedStatement(target.prepareStatement(sql, columnIndexes), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareStatement", sql).toString());
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditPreparedStatement} instance
   * that delegates all method calls to the underlying
   * {@link PreparedStatement}.
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
    if (trace)
      openConnections.add(this);

    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "prepareStatement", toString()).toString());

      return new AuditPreparedStatement(target.prepareStatement(sql, columnNames), sql);
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(log(this, "prepareStatement", sql).toString());
    }
  }

  @Override
  public void close() throws SQLException {
    if (trace)
      openConnections.remove(this);

    super.close();
  }
}