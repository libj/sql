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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * A {@link DelegateStatement} delegates to some other {@link Statement}, possibly transforming the method parameters along the way
 * or providing additional functionality. The class {@link DelegateStatement} itself simply implements default methods of the
 * {@link Statement} interface with versions that delegate all calls to the object returned by {@link #getTarget()}. Subclasses of
 * {@link DelegateStatement} may further override some of these methods and may also provide additional methods and fields.
 */
public interface DelegateStatement extends Statement {
  /**
   * Returns the target {@link Statement} to which all method calls will be delegated.
   *
   * @return The target {@link Statement} to which all method calls will be delegated.
   */
  Statement getTarget();

  @Override
  default ResultSet executeQuery(final String sql) throws SQLException {
    return getTarget().executeQuery(sql);
  }

  @Override
  default int executeUpdate(final String sql) throws SQLException {
    return getTarget().executeUpdate(sql);
  }

  @Override
  default void close() throws SQLException {
    getTarget().close();
  }

  @Override
  default int getMaxFieldSize() throws SQLException {
    return getTarget().getMaxFieldSize();
  }

  @Override
  default void setMaxFieldSize(final int max) throws SQLException {
    getTarget().setMaxFieldSize(max);
  }

  @Override
  default int getMaxRows() throws SQLException {
    return getTarget().getMaxRows();
  }

  @Override
  default void setMaxRows(final int max) throws SQLException {
    getTarget().setMaxRows(max);
  }

  @Override
  default void setEscapeProcessing(final boolean enable) throws SQLException {
    getTarget().setEscapeProcessing(enable);
  }

  @Override
  default int getQueryTimeout() throws SQLException {
    return getTarget().getQueryTimeout();
  }

  @Override
  default void setQueryTimeout(final int seconds) throws SQLException {
    getTarget().setQueryTimeout(seconds);
  }

  @Override
  default void cancel() throws SQLException {
    getTarget().cancel();
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
  default void setCursorName(final String name) throws SQLException {
    getTarget().setCursorName(name);
  }

  @Override
  default boolean execute(final String sql) throws SQLException {
    return getTarget().execute(sql);
  }

  @Override
  default ResultSet getResultSet() throws SQLException {
    return getTarget().getResultSet();
  }

  @Override
  default int getUpdateCount() throws SQLException {
    return getTarget().getUpdateCount();
  }

  @Override
  default boolean getMoreResults() throws SQLException {
    return getTarget().getMoreResults();
  }

  @Override
  default void setFetchDirection(final int direction) throws SQLException {
    getTarget().setFetchDirection(direction);
  }

  @Override
  default int getFetchDirection() throws SQLException {
    return getTarget().getFetchDirection();
  }

  @Override
  default void setFetchSize(final int rows) throws SQLException {
    getTarget().setFetchSize(rows);
  }

  @Override
  default int getFetchSize() throws SQLException {
    return getTarget().getFetchSize();
  }

  @Override
  default int getResultSetConcurrency() throws SQLException {
    return getTarget().getResultSetConcurrency();
  }

  @Override
  default int getResultSetType() throws SQLException {
    return getTarget().getResultSetType();
  }

  @Override
  default void addBatch(final String sql) throws SQLException {
    getTarget().addBatch(sql);
  }

  @Override
  default void clearBatch() throws SQLException {
    getTarget().clearBatch();
  }

  @Override
  default int[] executeBatch() throws SQLException {
    return getTarget().executeBatch();
  }

  @Override
  default Connection getConnection() throws SQLException {
    return getTarget().getConnection();
  }

  @Override
  default boolean getMoreResults(final int current) throws SQLException {
    return getTarget().getMoreResults(current);
  }

  @Override
  default ResultSet getGeneratedKeys() throws SQLException {
    return getTarget().getGeneratedKeys();
  }

  @Override
  default int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
    return getTarget().executeUpdate(sql);
  }

  @Override
  default int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
    return getTarget().executeUpdate(sql, columnIndexes);
  }

  @Override
  default int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
    return getTarget().executeUpdate(sql, columnNames);
  }

  @Override
  default boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
    return getTarget().execute(sql, autoGeneratedKeys);
  }

  @Override
  default boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
    return getTarget().execute(sql, columnIndexes);
  }

  @Override
  default boolean execute(final String sql, final String[] columnNames) throws SQLException {
    return getTarget().execute(sql, columnNames);
  }

  @Override
  default int getResultSetHoldability() throws SQLException {
    return getTarget().getResultSetHoldability();
  }

  @Override
  default boolean isClosed() throws SQLException {
    return getTarget().isClosed();
  }

  @Override
  default void setPoolable(final boolean poolable) throws SQLException {
    getTarget().setPoolable(poolable);
  }

  @Override
  default boolean isPoolable() throws SQLException {
    return getTarget().isPoolable();
  }

  @Override
  default <T>T unwrap(final Class<T> iface) throws SQLException {
    return getTarget().unwrap(iface);
  }

  @Override
  default boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return getTarget().isWrapperFor(iface);
  }

  @Override
  default void closeOnCompletion() throws SQLException {
    getTarget().closeOnCompletion();
  }

  @Override
  default boolean isCloseOnCompletion() throws SQLException {
    return getTarget().isCloseOnCompletion();
  }
}