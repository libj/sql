/* Copyright (c) 2018 LibJ
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

/**
 * A {@link DelegateConnection} delegates to some other {@link Connection}, possibly transforming the method parameters along the
 * way or providing additional functionality. The class {@link DelegateConnection} itself simply overrides all methods of
 * {@link Connection} with versions that delegate all calls to the object returned by {@link #getTarget()}. Subclasses of
 * {@link DelegateConnection} may further override some of these methods and may also provide additional methods and fields.
 */
public interface DelegateConnection extends Connection {
  /**
   * Returns the getTarget() {@link Connection} to which all method calls will be delegated.
   *
   * @return The getTarget() {@link Connection} to which all method calls will be delegated.
   */
  Connection getTarget();

  @Override
  default Statement createStatement() throws SQLException {
    return getTarget().createStatement();
  }

  @Override
  default PreparedStatement prepareStatement(final String sql) throws SQLException {
    return getTarget().prepareStatement(sql);
  }

  @Override
  default CallableStatement prepareCall(final String sql) throws SQLException {
    return getTarget().prepareCall(sql);
  }

  @Override
  default String nativeSQL(final String sql) throws SQLException {
    return getTarget().nativeSQL(sql);
  }

  @Override
  default void setAutoCommit(final boolean autoCommit) throws SQLException {
    getTarget().setAutoCommit(autoCommit);
  }

  @Override
  default boolean getAutoCommit() throws SQLException {
    return getTarget().getAutoCommit();
  }

  @Override
  default void commit() throws SQLException {
    getTarget().commit();
  }

  @Override
  default void rollback() throws SQLException {
    getTarget().rollback();
  }

  @Override
  default void close() throws SQLException {
    getTarget().close();
  }

  @Override
  default boolean isClosed() throws SQLException {
    return getTarget().isClosed();
  }

  @Override
  default DatabaseMetaData getMetaData() throws SQLException {
    return getTarget().getMetaData();
  }

  @Override
  default void setReadOnly(final boolean readOnly) throws SQLException {
    getTarget().setReadOnly(readOnly);
  }

  @Override
  default boolean isReadOnly() throws SQLException {
    return getTarget().isReadOnly();
  }

  @Override
  default void setCatalog(final String catalog) throws SQLException {
    getTarget().setCatalog(catalog);
  }

  @Override
  default String getCatalog() throws SQLException {
    return getTarget().getCatalog();
  }

  @Override
  default void setTransactionIsolation(final int level) throws SQLException {
    getTarget().setTransactionIsolation(level);
  }

  @Override
  default int getTransactionIsolation() throws SQLException {
    return getTarget().getTransactionIsolation();
  }

  @Override
  default SQLWarning getWarnings() throws SQLException {
    return getTarget().getWarnings();
  }

  @Override
  default void clearWarnings() throws SQLException {
    getTarget().clearWarnings();
  }

  @Override
  default Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
    return getTarget().createStatement(resultSetType, resultSetConcurrency);
  }

  @Override
  default PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    return getTarget().prepareStatement(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  default CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    return getTarget().prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  @Override
  default Map<String,Class<?>> getTypeMap() throws SQLException {
    return getTarget().getTypeMap();
  }

  @Override
  default void setTypeMap(final Map<String,Class<?>> map) throws SQLException {
    getTarget().setTypeMap(map);
  }

  @Override
  default void setHoldability(final int holdability) throws SQLException {
    getTarget().setHoldability(holdability);
  }

  @Override
  default int getHoldability() throws SQLException {
    return getTarget().getHoldability();
  }

  @Override
  default Savepoint setSavepoint() throws SQLException {
    return getTarget().setSavepoint();
  }

  @Override
  default Savepoint setSavepoint(final String name) throws SQLException {
    return getTarget().setSavepoint(name);
  }

  @Override
  default void rollback(final Savepoint savepoint) throws SQLException {
    getTarget().rollback(savepoint);
  }

  @Override
  default void releaseSavepoint(final Savepoint savepoint) throws SQLException {
    getTarget().releaseSavepoint(savepoint);
  }

  @Override
  default Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    return getTarget().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  default PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    return getTarget().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  default CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
    return getTarget().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  @Override
  default PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
    return getTarget().prepareStatement(sql, autoGeneratedKeys);
  }

  @Override
  default PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
    return getTarget().prepareStatement(sql, columnIndexes);
  }

  @Override
  default PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
    return getTarget().prepareStatement(sql, columnNames);
  }

  @Override
  default Clob createClob() throws SQLException {
    return getTarget().createClob();
  }

  @Override
  default Blob createBlob() throws SQLException {
    return getTarget().createBlob();
  }

  @Override
  default NClob createNClob() throws SQLException {
    return getTarget().createNClob();
  }

  @Override
  default SQLXML createSQLXML() throws SQLException {
    return getTarget().createSQLXML();
  }

  @Override
  default boolean isValid(final int timeout) throws SQLException {
    return getTarget().isValid(timeout);
  }

  @Override
  default void setClientInfo(final String name, final String value) throws SQLClientInfoException {
    getTarget().setClientInfo(name, value);
  }

  @Override
  default void setClientInfo(final Properties properties) throws SQLClientInfoException {
    getTarget().setClientInfo(properties);
  }

  @Override
  default String getClientInfo(final String name) throws SQLException {
    return getTarget().getClientInfo(name);
  }

  @Override
  default Properties getClientInfo() throws SQLException {
    return getTarget().getClientInfo();
  }

  @Override
  default Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
    return getTarget().createArrayOf(typeName, elements);
  }

  @Override
  default Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
    return getTarget().createStruct(typeName, attributes);
  }

  @Override
  default <T> T unwrap(final Class<T> iface) throws SQLException {
    return getTarget().unwrap(iface);
  }

  @Override
  default boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return getTarget().isWrapperFor(iface);
  }

  @Override
  default void setSchema(final String schema) throws SQLException {
    getTarget().setSchema(schema);
  }

  @Override
  default String getSchema() throws SQLException {
    return getTarget().getSchema();
  }

  @Override
  default void abort(final Executor executor) throws SQLException {
    getTarget().abort(executor);
  }

  @Override
  default void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
    getTarget().setNetworkTimeout(executor, milliseconds);
  }

  @Override
  default int getNetworkTimeout() throws SQLException {
    return getTarget().getNetworkTimeout();
  }
}