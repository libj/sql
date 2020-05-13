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
    return new AuditPreparedStatement(target.prepareStatement(sql), sql);
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditStatement} instance that
   * delegates all method calls to the underlying {@link Statement}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
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
    return new AuditPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
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
    return new AuditCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This method returns an {@link AuditStatement} instance that
   * delegates all method calls to the underlying {@link Statement}.
   */
  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
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
    return new AuditPreparedStatement(target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
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
    return new AuditCallableStatement(target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
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
    return new AuditPreparedStatement(target.prepareStatement(sql, autoGeneratedKeys), sql);
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
    return new AuditPreparedStatement(target.prepareStatement(sql, columnIndexes), sql);
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
    return new AuditPreparedStatement(target.prepareStatement(sql, columnNames), sql);
  }
}