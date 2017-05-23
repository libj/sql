/* Copyright (c) 2009 lib4j
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

package org.lib4j.sql;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.lib4j.lang.Throwables;

public class ConnectionProxy implements Connection {
  public static void close(final Connection connection) {
    try {
      if (connection != null && !connection.isClosed())
        connection.close();
    }
    catch (final SQLException e) {
    }
  }

  protected final Connection connection;

  public ConnectionProxy(final Connection connection) {
    this.connection = connection;
  }

  @Override
  public Statement createStatement() throws SQLException {
    return new StatementProxy(connection.createStatement());
  }

  @Override
  public PreparedStatement prepareStatement(final String sql) throws SQLException {
    try {
      return new PreparedStatementProxy(connection.prepareStatement(sql), sql);
    }
    catch (final SQLException e) {
      Throwables.set(e, e.getMessage() + " "  + sql);
      throw e;
    }
  }

  @Override
  public CallableStatement prepareCall(final String sql) throws SQLException {
    return new CallableStatementProxy(connection.prepareCall(sql), sql);
  }

  @Override
  public String nativeSQL(final String sql) throws SQLException {
    return connection.nativeSQL(sql);
  }

  @Override
  public void setAutoCommit(final boolean autoCommit) throws SQLException {
    connection.setAutoCommit(autoCommit);
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return connection.getAutoCommit();
  }

  @Override
  public void commit() throws SQLException {
    connection.commit();
  }

  @Override
  public void rollback() throws SQLException {
    connection.rollback();
  }

  @Override
  public void close() throws SQLException {
    connection.close();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return connection.isClosed();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return connection.getMetaData();
  }

  @Override
  public void setReadOnly(final boolean readOnly) throws SQLException {
    connection.setReadOnly(readOnly);
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return connection.isReadOnly();
  }

  @Override
  public void setCatalog(final String catalog) throws SQLException {
    connection.setCatalog(catalog);
  }

  @Override
  public String getCatalog() throws SQLException {
    return connection.getCatalog();
  }

  @Override
  public void setTransactionIsolation(final int level) throws SQLException {
    connection.setTransactionIsolation(level);
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return connection.getTransactionIsolation();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return connection.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    connection.clearWarnings();
  }

  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
    return new StatementProxy(connection.createStatement(resultSetType, resultSetConcurrency));
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    try {
      return new PreparedStatementProxy(connection.prepareStatement(sql, resultSetType, resultSetConcurrency), sql);
    }
    catch (final SQLException e) {
      Throwables.set(e, e.getMessage() + " "  + sql);
      throw e;
    }
  }

  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    return new CallableStatementProxy(connection.prepareCall(sql, resultSetType, resultSetConcurrency), sql);
  }

  @Override
  public Map<String,Class<?>> getTypeMap() throws SQLException {
    return connection.getTypeMap();
  }

  @Override
  public void setTypeMap(final Map<String,Class<?>> map) throws SQLException {
    connection.setTypeMap(map);
  }

  @Override
  public void setHoldability(final int holdability) throws SQLException {
    connection.setHoldability(holdability);
  }

  @Override
  public int getHoldability() throws SQLException {
    return connection.getHoldability();
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    return connection.setSavepoint();
  }

  @Override
  public Savepoint setSavepoint(final String name) throws SQLException {
    return connection.setSavepoint(name);
  }

  @Override
  public void rollback(final Savepoint savepoint) throws SQLException {
    connection.rollback(savepoint);
  }

  @Override
  public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
    connection.releaseSavepoint(savepoint);
  }

  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    return new StatementProxy(connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    try {
      return new PreparedStatementProxy(connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    }
    catch (final SQLException e) {
      Throwables.set(e, e.getMessage() + " "  + sql);
      throw e;
    }
  }

  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    return new CallableStatementProxy(connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql);
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
    try {
      return new PreparedStatementProxy(connection.prepareStatement(sql, autoGeneratedKeys), sql);
    }
    catch (final SQLException e) {
      Throwables.set(e, e.getMessage() + " "  + sql);
      throw e;
    }
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
    try {
      return new PreparedStatementProxy(connection.prepareStatement(sql, columnIndexes), sql);
    }
    catch (final SQLException e) {
      Throwables.set(e, e.getMessage() + " "  + sql);
      throw e;
    }
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
    try {
      return new PreparedStatementProxy(connection.prepareStatement(sql, columnNames), sql);
    }
    catch (final SQLException e) {
      Throwables.set(e, e.getMessage() + " "  + sql);
      throw e;
    }
  }

  @Override
  public Clob createClob() throws SQLException {
    return connection.createClob();
  }

  @Override
  public Blob createBlob() throws SQLException {
    return connection.createBlob();
  }

  @Override
  public NClob createNClob() throws SQLException {
    return connection.createNClob();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return connection.createSQLXML();
  }

  @Override
  public boolean isValid(final int timeout) throws SQLException {
    return connection.isValid(timeout);
  }

  @Override
  public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
    connection.setClientInfo(name, value);
  }

  @Override
  public void setClientInfo(final Properties properties) throws SQLClientInfoException {
    connection.setClientInfo(properties);
  }

  @Override
  public String getClientInfo(final String name) throws SQLException {
    return connection.getClientInfo(name);
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return connection.getClientInfo();
  }

  @Override
  public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
    return connection.createArrayOf(typeName, elements);
  }

  @Override
  public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
    return connection.createStruct(typeName, attributes);
  }

  @Override
  public <T extends Object>T unwrap(final Class<T> iface) throws SQLException {
    return connection.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return connection.isWrapperFor(iface);
  }

  @Override
  public void setSchema(final String schema) throws SQLException {
    connection.setSchema(schema);
  }

  @Override
  public String getSchema() throws SQLException {
    return connection.getSchema();
  }

  @Override
  public void abort(final Executor executor) throws SQLException {
    connection.abort(executor);
  }

  @Override
  public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
    connection.setNetworkTimeout(executor, milliseconds);
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return connection.getNetworkTimeout();
  }
}