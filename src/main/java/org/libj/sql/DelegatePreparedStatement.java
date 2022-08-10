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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * A {@link DelegatePreparedStatement} delegates to some other {@link Statement}, possibly transforming the method parameters along
 * the way or providing additional functionality. The class {@link DelegatePreparedStatement} itself simply implements default
 * methods of the {@link Statement} interface with versions that delegate all calls to the object returned by {@link #getTarget()}.
 * Subclasses of {@link DelegatePreparedStatement} may further override some of these methods and may also provide additional
 * methods and fields.
 */
public interface DelegatePreparedStatement extends DelegateStatement, PreparedStatement {
  /**
   * Returns the target {@link PreparedStatement} to which all method calls will be delegated.
   *
   * @return The target {@link PreparedStatement} to which all method calls will be delegated.
   */
  @Override
  PreparedStatement getTarget();

  @Override
  default ResultSet executeQuery() throws SQLException {
    return getTarget().executeQuery();
  }

  @Override
  default int executeUpdate() throws SQLException {
    return getTarget().executeUpdate();
  }

  @Override
  default void setNull(final int parameterIndex, final int sqlType) throws SQLException {
    getTarget().setNull(parameterIndex, sqlType);
  }

  @Override
  default void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
    getTarget().setBoolean(parameterIndex, x);
  }

  @Override
  default void setByte(final int parameterIndex, final byte x) throws SQLException {
    getTarget().setByte(parameterIndex, x);
  }

  @Override
  default void setShort(final int parameterIndex, final short x) throws SQLException {
    getTarget().setShort(parameterIndex, x);
  }

  @Override
  default void setInt(final int parameterIndex, final int x) throws SQLException {
    getTarget().setInt(parameterIndex, x);
  }

  @Override
  default void setLong(final int parameterIndex, final long x) throws SQLException {
    getTarget().setLong(parameterIndex, x);
  }

  @Override
  default void setFloat(final int parameterIndex, final float x) throws SQLException {
    getTarget().setFloat(parameterIndex, x);
  }

  @Override
  default void setDouble(final int parameterIndex, final double x) throws SQLException {
    getTarget().setDouble(parameterIndex, x);
  }

  @Override
  default void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
    getTarget().setBigDecimal(parameterIndex, x);
  }

  @Override
  default void setString(final int parameterIndex, final String x) throws SQLException {
    getTarget().setString(parameterIndex, x);
  }

  @Override
  default void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
    getTarget().setBytes(parameterIndex, x);
  }

  @Override
  default void setDate(final int parameterIndex, final Date x) throws SQLException {
    getTarget().setDate(parameterIndex, x);
  }

  @Override
  default void setTime(final int parameterIndex, final Time x) throws SQLException {
    getTarget().setTime(parameterIndex, x);
  }

  @Override
  default void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
    getTarget().setTimestamp(parameterIndex, x);
  }

  @Override
  default void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x, length);
  }

  @Override
  @Deprecated//(since="1.2")
  default void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setUnicodeStream(parameterIndex, x, length);
  }

  @Override
  default void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x, length);
  }

  @Override
  default void clearParameters() throws SQLException {
    getTarget().clearParameters();
  }

  @Override
  default void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scale) throws SQLException {
    getTarget().setObject(parameterIndex, x, targetSqlType, scale);
  }

  @Override
  default void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
    getTarget().setObject(parameterIndex, x, targetSqlType);
  }

  @Override
  default void setObject(final int parameterIndex, final Object x) throws SQLException {
    getTarget().setObject(parameterIndex, x);
  }

  @Override
  default boolean execute() throws SQLException {
    return getTarget().execute();
  }

  @Override
  default void addBatch() throws SQLException {
    getTarget().addBatch();
  }

  @Override
  default void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  default void setRef(final int i, final Ref x) throws SQLException {
    getTarget().setRef(i, x);
  }

  @Override
  default void setBlob(final int i, final Blob x) throws SQLException {
    getTarget().setBlob(i, x);
  }

  @Override
  default void setClob(final int i, final Clob x) throws SQLException {
    getTarget().setClob(i, x);
  }

  @Override
  default void setArray(final int i, final Array x) throws SQLException {
    getTarget().setArray(i, x);
  }

  @Override
  default ResultSetMetaData getMetaData() throws SQLException {
    return getTarget().getMetaData();
  }

  @Override
  default void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
    getTarget().setDate(parameterIndex, x, cal);
  }

  @Override
  default void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
    getTarget().setTime(parameterIndex, x, cal);
  }

  @Override
  default void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
    getTarget().setTimestamp(parameterIndex, x, cal);
  }

  @Override
  default void setNull(final int paramIndex, final int sqlType, final String typeName) throws SQLException {
    getTarget().setNull(paramIndex, sqlType, typeName);
  }

  @Override
  default void setURL(final int parameterIndex, final URL x) throws SQLException {
    getTarget().setURL(parameterIndex, x);
  }

  @Override
  default ParameterMetaData getParameterMetaData() throws SQLException {
    return getTarget().getParameterMetaData();
  }

  @Override
  default void setRowId(final int parameterIndex, final RowId x) throws SQLException {
    getTarget().setRowId(parameterIndex, x);
  }

  @Override
  default void setNString(final int parameterIndex, final String value) throws SQLException {
    getTarget().setNString(parameterIndex, value);
  }

  @Override
  default void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
    getTarget().setNCharacterStream(parameterIndex, value, length);
  }

  @Override
  default void setNClob(final int parameterIndex, final NClob value) throws SQLException {
    getTarget().setNClob(parameterIndex, value);
  }

  @Override
  default void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setClob(parameterIndex, reader, length);
  }

  @Override
  default void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
    getTarget().setBlob(parameterIndex, inputStream, length);
  }

  @Override
  default void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setNClob(parameterIndex, reader, length);
  }

  @Override
  default void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
    getTarget().setSQLXML(parameterIndex, xmlObject);
  }

  @Override
  default void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x, length);
  }

  @Override
  default void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x, length);
  }

  @Override
  default void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  default void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x);
  }

  @Override
  default void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x);
  }

  @Override
  default void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader);
  }

  @Override
  default void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
    getTarget().setNCharacterStream(parameterIndex, value);
  }

  @Override
  default void setClob(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setClob(parameterIndex, reader);
  }

  @Override
  default void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
    getTarget().setBlob(parameterIndex, inputStream);
  }

  @Override
  default void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setNClob(parameterIndex, reader);
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
}