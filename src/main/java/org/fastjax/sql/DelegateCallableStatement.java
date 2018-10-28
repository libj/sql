/* Copyright (c) 2018 FastJAX
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

package org.fastjax.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A {@code DelegateCallableStatement} delegates to some other
 * {@link Statement}, possibly transforming the method parameters along the way
 * or providing additional functionality. The class
 * {@code DelegateCallableStatement} itself simply implements default methods of
 * the {@link Statement} interface with versions that delegate all calls to the
 * object returned by {@link #getTarget()}. Subclasses of
 * {@code DelegateCallableStatement} may further override some of these methods
 * and may also provide additional methods and fields.
 */
public interface DelegateCallableStatement extends DelegatePreparedStatement, CallableStatement {
  /**
   * @return The target {@link CallableStatement} to which all method calls will
   *         be delegated.
   */
  @Override
  CallableStatement getTarget();

  @Override
  default void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
    getTarget().registerOutParameter(parameterIndex, sqlType);
  }

  @Override
  default void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
    getTarget().registerOutParameter(parameterIndex, sqlType, scale);
  }

  @Override
  default boolean wasNull() throws SQLException {
    return getTarget().wasNull();
  }

  @Override
  default String getString(final int parameterIndex) throws SQLException {
    return getTarget().getString(parameterIndex);
  }

  @Override
  default boolean getBoolean(final int parameterIndex) throws SQLException {
    return getTarget().getBoolean(parameterIndex);
  }

  @Override
  default byte getByte(final int parameterIndex) throws SQLException {
    return getTarget().getByte(parameterIndex);
  }

  @Override
  default short getShort(final int parameterIndex) throws SQLException {
    return getTarget().getShort(parameterIndex);
  }

  @Override
  default int getInt(final int parameterIndex) throws SQLException {
    return getTarget().getInt(parameterIndex);
  }

  @Override
  default long getLong(final int parameterIndex) throws SQLException {
    return getTarget().getLong(parameterIndex);
  }

  @Override
  default float getFloat(final int parameterIndex) throws SQLException {
    return getTarget().getFloat(parameterIndex);
  }

  @Override
  default double getDouble(final int parameterIndex) throws SQLException {
    return getTarget().getDouble(parameterIndex);
  }

  @Override
  @Deprecated(since="1.2")
  default BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
    return getTarget().getBigDecimal(parameterIndex);
  }

  @Override
  default byte[] getBytes(final int parameterIndex) throws SQLException {
    return getTarget().getBytes(parameterIndex);
  }

  @Override
  default Date getDate(final int parameterIndex) throws SQLException {
    return getTarget().getDate(parameterIndex);
  }

  @Override
  default Time getTime(final int parameterIndex) throws SQLException {
    return getTarget().getTime(parameterIndex);
  }

  @Override
  default Timestamp getTimestamp(final int parameterIndex) throws SQLException {
    return getTarget().getTimestamp(parameterIndex);
  }

  @Override
  default Object getObject(final int parameterIndex) throws SQLException {
    return getTarget().getObject(parameterIndex);
  }

  @Override
  default BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
    return getTarget().getBigDecimal(parameterIndex);
  }

  @Override
  default Object getObject(final int i, final Map<String,Class<?>> map) throws SQLException {
    return getTarget().getObject(i, map);
  }

  @Override
  default Ref getRef(final int i) throws SQLException {
    return getTarget().getRef(i);
  }

  @Override
  default Blob getBlob(final int i) throws SQLException {
    return getTarget().getBlob(i);
  }

  @Override
  default Clob getClob(final int i) throws SQLException {
    return getTarget().getClob(i);
  }

  @Override
  default Array getArray(final int i) throws SQLException {
    return getTarget().getArray(i);
  }

  @Override
  default Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
    return getTarget().getDate(parameterIndex, cal);
  }

  @Override
  default Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
    return getTarget().getTime(parameterIndex, cal);
  }

  @Override
  default Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
    return getTarget().getTimestamp(parameterIndex, cal);
  }

  @Override
  default void registerOutParameter(final int paramIndex, final int sqlType, final String typeName) throws SQLException {
    getTarget().registerOutParameter(paramIndex, sqlType, typeName);
  }

  @Override
  default void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
    getTarget().registerOutParameter(parameterName, sqlType);
  }

  @Override
  default void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
    getTarget().registerOutParameter(parameterName, sqlType, scale);
  }

  @Override
  default void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
    getTarget().registerOutParameter(parameterName, sqlType, typeName);
  }

  @Override
  default URL getURL(final int parameterIndex) throws SQLException {
    return getTarget().getURL(parameterIndex);
  }

  @Override
  default void setURL(final String parameterName, final URL val) throws SQLException {
    getTarget().setURL(parameterName, val);
  }

  @Override
  default void setNull(final String parameterName, final int sqlType) throws SQLException {
    getTarget().setNull(parameterName, sqlType);
  }

  @Override
  default void setBoolean(final String parameterName, final boolean x) throws SQLException {
    getTarget().setBoolean(parameterName, x);
  }

  @Override
  default void setByte(final String parameterName, final byte x) throws SQLException {
    getTarget().setByte(parameterName, x);
  }

  @Override
  default void setShort(final String parameterName, final short x) throws SQLException {
    getTarget().setShort(parameterName, x);
  }

  @Override
  default void setInt(final String parameterName, final int x) throws SQLException {
    getTarget().setInt(parameterName, x);
  }

  @Override
  default void setLong(final String parameterName, final long x) throws SQLException {
    getTarget().setLong(parameterName, x);
  }

  @Override
  default void setFloat(final String parameterName, final float x) throws SQLException {
    getTarget().setFloat(parameterName, x);
  }

  @Override
  default void setDouble(final String parameterName, final double x) throws SQLException {
    getTarget().setDouble(parameterName, x);
  }

  @Override
  default void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
    getTarget().setBigDecimal(parameterName, x);
  }

  @Override
  default void setString(final String parameterName, final String x) throws SQLException {
    getTarget().setString(parameterName, x);
  }

  @Override
  default void setBytes(final String parameterName, final byte[] x) throws SQLException {
    getTarget().setBytes(parameterName, x);
  }

  @Override
  default void setDate(final String parameterName, final Date x) throws SQLException {
    getTarget().setDate(parameterName, x);
  }

  @Override
  default void setTime(final String parameterName, final Time x) throws SQLException {
    getTarget().setTime(parameterName, x);
  }

  @Override
  default void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
    getTarget().setTimestamp(parameterName, x);
  }

  @Override
  default void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
    getTarget().setAsciiStream(parameterName, x, length);
  }

  @Override
  default void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
    getTarget().setBinaryStream(parameterName, x, length);
  }

  @Override
  default void setObject(final String parameterName, final Object x, int targetSqlType, final int scale) throws SQLException {
    getTarget().setObject(parameterName, x, targetSqlType, scale);
  }

  @Override
  default void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
    getTarget().setObject(parameterName, x, targetSqlType);
  }

  @Override
  default void setObject(final String parameterName, final Object x) throws SQLException {
    getTarget().setObject(parameterName, x);
  }

  @Override
  default void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
    getTarget().setCharacterStream(parameterName, reader, length);
  }

  @Override
  default void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
    getTarget().setDate(parameterName, x, cal);
  }

  @Override
  default void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
    getTarget().setTime(parameterName, x, cal);
  }

  @Override
  default void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
    getTarget().setTimestamp(parameterName, x, cal);
  }

  @Override
  default void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
    getTarget().setNull(parameterName, sqlType, typeName);
  }

  @Override
  default String getString(final String parameterName) throws SQLException {
    return getTarget().getString(parameterName);
  }

  @Override
  default boolean getBoolean(final String parameterName) throws SQLException {
    return getTarget().getBoolean(parameterName);
  }

  @Override
  default byte getByte(final String parameterName) throws SQLException {
    return getTarget().getByte(parameterName);
  }

  @Override
  default short getShort(final String parameterName) throws SQLException {
    return getTarget().getShort(parameterName);
  }

  @Override
  default int getInt(final String parameterName) throws SQLException {
    return getTarget().getInt(parameterName);
  }

  @Override
  default long getLong(final String parameterName) throws SQLException {
    return getTarget().getLong(parameterName);
  }

  @Override
  default float getFloat(final String parameterName) throws SQLException {
    return getTarget().getFloat(parameterName);
  }

  @Override
  default double getDouble(final String parameterName) throws SQLException {
    return getTarget().getDouble(parameterName);
  }

  @Override
  default byte[] getBytes(final String parameterName) throws SQLException {
    return getTarget().getBytes(parameterName);
  }

  @Override
  default Date getDate(final String parameterName) throws SQLException {
    return getTarget().getDate(parameterName);
  }

  @Override
  default Time getTime(final String parameterName) throws SQLException {
    return getTarget().getTime(parameterName);
  }

  @Override
  default Timestamp getTimestamp(final String parameterName) throws SQLException {
    return getTarget().getTimestamp(parameterName);
  }

  @Override
  default Object getObject(final String parameterName) throws SQLException {
    return getTarget().getObject(parameterName);
  }

  @Override
  default BigDecimal getBigDecimal(final String parameterName) throws SQLException {
    return getTarget().getBigDecimal(parameterName);
  }

  @Override
  default Object getObject(final String parameterName, final Map<String,Class<?>> map) throws SQLException {
    return getTarget().getObject(parameterName, map);
  }

  @Override
  default Ref getRef(final String parameterName) throws SQLException {
    return getTarget().getRef(parameterName);
  }

  @Override
  default Blob getBlob(final String parameterName) throws SQLException {
    return getTarget().getBlob(parameterName);
  }

  @Override
  default Clob getClob(final String parameterName) throws SQLException {
    return getTarget().getClob(parameterName);
  }

  @Override
  default Array getArray(final String parameterName) throws SQLException {
    return getTarget().getArray(parameterName);
  }

  @Override
  default Date getDate(final String parameterName, final Calendar cal) throws SQLException {
    return getTarget().getDate(parameterName, cal);
  }

  @Override
  default Time getTime(final String parameterName, final Calendar cal) throws SQLException {
    return getTarget().getTime(parameterName, cal);
  }

  @Override
  default Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
    return getTarget().getTimestamp(parameterName, cal);
  }

  @Override
  default URL getURL(final String parameterName) throws SQLException {
    return getTarget().getURL(parameterName);
  }

  @Override
  default RowId getRowId(final int parameterIndex) throws SQLException {
    return getTarget().getRowId(parameterIndex);
  }

  @Override
  default RowId getRowId(final String parameterName) throws SQLException {
    return getTarget().getRowId(parameterName);
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
  default <T extends Object> T unwrap(final Class<T> iface) throws SQLException {
    return getTarget().unwrap(iface);
  }

  @Override
  default boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return getTarget().isWrapperFor(iface);
  }

  @Override
  default void setRowId(final String parameterName, final RowId x) throws SQLException {
    getTarget().setRowId(parameterName, x);
  }

  @Override
  default void setNString(final String parameterName, final String value) throws SQLException {
    getTarget().setNString(parameterName, value);
  }

  @Override
  default void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
    getTarget().setNCharacterStream(parameterName, value, length);
  }

  @Override
  default void setNClob(final String parameterName, final NClob value) throws SQLException {
    getTarget().setNClob(parameterName, value);
  }

  @Override
  default void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
    getTarget().setClob(parameterName, reader);
  }

  @Override
  default void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
    getTarget().setBlob(parameterName, inputStream);
  }

  @Override
  default void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
    getTarget().setNClob(parameterName, reader, length);
  }

  @Override
  default NClob getNClob(final int parameterIndex) throws SQLException {
    return getTarget().getNClob(parameterIndex);
  }

  @Override
  default NClob getNClob(final String parameterName) throws SQLException {
    return getTarget().getNClob(parameterName);
  }

  @Override
  default void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
    getTarget().setSQLXML(parameterName, xmlObject);
  }

  @Override
  default SQLXML getSQLXML(final int parameterIndex) throws SQLException {
    return getTarget().getSQLXML(parameterIndex);
  }

  @Override
  default SQLXML getSQLXML(final String parameterName) throws SQLException {
    return getTarget().getSQLXML(parameterName);
  }

  @Override
  default String getNString(final int parameterIndex) throws SQLException {
    return getTarget().getNString(parameterIndex);
  }

  @Override
  default String getNString(final String parameterName) throws SQLException {
    return getTarget().getNString(parameterName);
  }

  @Override
  default Reader getNCharacterStream(final int parameterIndex) throws SQLException {
    return getTarget().getNCharacterStream(parameterIndex);
  }

  @Override
  default Reader getNCharacterStream(final String parameterName) throws SQLException {
    return getTarget().getNCharacterStream(parameterName);
  }

  @Override
  default Reader getCharacterStream(final int parameterIndex) throws SQLException {
    return getTarget().getCharacterStream(parameterIndex);
  }

  @Override
  default Reader getCharacterStream(final String parameterName) throws SQLException {
    return getTarget().getCharacterStream(parameterName);
  }

  @Override
  default void setBlob(final String parameterName, final Blob x) throws SQLException {
    getTarget().setBlob(parameterName, x);
  }

  @Override
  default void setClob(final String parameterName, final Clob x) throws SQLException {
    getTarget().setClob(parameterName, x);
  }

  @Override
  default void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
    getTarget().setAsciiStream(parameterName, x, length);
  }

  @Override
  default void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
    getTarget().setBinaryStream(parameterName, x, length);
  }

  @Override
  default void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
    getTarget().setCharacterStream(parameterName, reader, length);
  }

  @Override
  default void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
    getTarget().setAsciiStream(parameterName, x);
  }

  @Override
  default void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
    getTarget().setBinaryStream(parameterName, x);
  }

  @Override
  default void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
    getTarget().setCharacterStream(parameterName, reader);
  }

  @Override
  default void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
    getTarget().setNCharacterStream(parameterName, value);
  }

  @Override
  default void setClob(final String parameterName, final Reader reader) throws SQLException {
    getTarget().setClob(parameterName, reader);
  }

  @Override
  default void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
    getTarget().setBlob(parameterName, inputStream);
  }

  @Override
  default void setNClob(final String parameterName, final Reader reader) throws SQLException {
    getTarget().setNClob(parameterName, reader);
  }

  @Override
  default <T>T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
    return getTarget().getObject(parameterIndex, type);
  }

  @Override
  default <T>T getObject(final String parameterName, final Class<T> type) throws SQLException {
    return getTarget().getObject(parameterName, type);
  }
}