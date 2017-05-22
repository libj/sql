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

package org.safris.commons.sql;

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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public final class CallableStatementProxy extends PreparedStatementProxy implements CallableStatement {
  public CallableStatementProxy(final CallableStatement callableStatement, final String sql) {
    super(callableStatement, sql);
  }

  @Override
  protected final CallableStatement getStatement() {
    return (CallableStatement)super.getStatement();
  }

  @Override
  public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
    getStatement().registerOutParameter(parameterIndex, sqlType);
  }

  @Override
  public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
    getStatement().registerOutParameter(parameterIndex, sqlType, scale);
  }

  @Override
  public boolean wasNull() throws SQLException {
    return getStatement().wasNull();
  }

  @Override
  public String getString(final int parameterIndex) throws SQLException {
    return getStatement().getString(parameterIndex);
  }

  @Override
  public boolean getBoolean(final int parameterIndex) throws SQLException {
    return getStatement().getBoolean(parameterIndex);
  }

  @Override
  public byte getByte(final int parameterIndex) throws SQLException {
    return getStatement().getByte(parameterIndex);
  }

  @Override
  public short getShort(final int parameterIndex) throws SQLException {
    return getStatement().getShort(parameterIndex);
  }

  @Override
  public int getInt(final int parameterIndex) throws SQLException {
    return getStatement().getInt(parameterIndex);
  }

  @Override
  public long getLong(final int parameterIndex) throws SQLException {
    return getStatement().getLong(parameterIndex);
  }

  @Override
  public float getFloat(final int parameterIndex) throws SQLException {
    return getStatement().getFloat(parameterIndex);
  }

  @Override
  public double getDouble(final int parameterIndex) throws SQLException {
    return getStatement().getDouble(parameterIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
    return getStatement().getBigDecimal(parameterIndex);
  }

  @Override
  public byte[] getBytes(final int parameterIndex) throws SQLException {
    return getStatement().getBytes(parameterIndex);
  }

  @Override
  public Date getDate(final int parameterIndex) throws SQLException {
    return getStatement().getDate(parameterIndex);
  }

  @Override
  public Time getTime(final int parameterIndex) throws SQLException {
    return getStatement().getTime(parameterIndex);
  }

  @Override
  public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
    return getStatement().getTimestamp(parameterIndex);
  }

  @Override
  public Object getObject(final int parameterIndex) throws SQLException {
    return getStatement().getObject(parameterIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
    return getStatement().getBigDecimal(parameterIndex);
  }

  @Override
  public Object getObject(final int i, final Map<String,Class<?>> map) throws SQLException {
    return getStatement().getObject(i, map);
  }

  @Override
  public Ref getRef(final int i) throws SQLException {
    return getStatement().getRef(i);
  }

  @Override
  public Blob getBlob(final int i) throws SQLException {
    return getStatement().getBlob(i);
  }

  @Override
  public Clob getClob(final int i) throws SQLException {
    return getStatement().getClob(i);
  }

  @Override
  public Array getArray(final int i) throws SQLException {
    return getStatement().getArray(i);
  }

  @Override
  public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
    return getStatement().getDate(parameterIndex, cal);
  }

  @Override
  public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
    return getStatement().getTime(parameterIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
    return getStatement().getTimestamp(parameterIndex, cal);
  }

  @Override
  public void registerOutParameter(final int paramIndex, final int sqlType, final String typeName) throws SQLException {
    getStatement().registerOutParameter(paramIndex, sqlType, typeName);
  }

  @Override
  public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
    getStatement().registerOutParameter(parameterName, sqlType);
  }

  @Override
  public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
    getStatement().registerOutParameter(parameterName, sqlType, scale);
  }

  @Override
  public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
    getStatement().registerOutParameter(parameterName, sqlType, typeName);
  }

  @Override
  public URL getURL(final int parameterIndex) throws SQLException {
    return getStatement().getURL(parameterIndex);
  }

  @Override
  public void setURL(final String parameterName, final URL val) throws SQLException {
    getStatement().setURL(parameterName, val);
  }

  @Override
  public void setNull(final String parameterName, final int sqlType) throws SQLException {
    getStatement().setNull(parameterName, sqlType);
  }

  @Override
  public void setBoolean(final String parameterName, final boolean x) throws SQLException {
    getStatement().setBoolean(parameterName, x);
  }

  @Override
  public void setByte(final String parameterName, final byte x) throws SQLException {
    getStatement().setByte(parameterName, x);
  }

  @Override
  public void setShort(final String parameterName, final short x) throws SQLException {
    getStatement().setShort(parameterName, x);
  }

  @Override
  public void setInt(final String parameterName, final int x) throws SQLException {
    getStatement().setInt(parameterName, x);
  }

  @Override
  public void setLong(final String parameterName, final long x) throws SQLException {
    getStatement().setLong(parameterName, x);
  }

  @Override
  public void setFloat(final String parameterName, final float x) throws SQLException {
    getStatement().setFloat(parameterName, x);
  }

  @Override
  public void setDouble(final String parameterName, final double x) throws SQLException {
    getStatement().setDouble(parameterName, x);
  }

  @Override
  public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
    getStatement().setBigDecimal(parameterName, x);
  }

  @Override
  public void setString(final String parameterName, final String x) throws SQLException {
    getStatement().setString(parameterName, x);
  }

  @Override
  public void setBytes(final String parameterName, final byte[] x) throws SQLException {
    getStatement().setBytes(parameterName, x);
  }

  @Override
  public void setDate(final String parameterName, final Date x) throws SQLException {
    getStatement().setDate(parameterName, x);
  }

  @Override
  public void setTime(final String parameterName, final Time x) throws SQLException {
    getStatement().setTime(parameterName, x);
  }

  @Override
  public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
    getStatement().setTimestamp(parameterName, x);
  }

  @Override
  public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
    getStatement().setAsciiStream(parameterName, x, length);
  }

  @Override
  public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
    getStatement().setBinaryStream(parameterName, x, length);
  }

  @Override
  public void setObject(final String parameterName, final Object x, int targetSqlType, final int scale) throws SQLException {
    getStatement().setObject(parameterName, x, targetSqlType, scale);
  }

  @Override
  public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
    getStatement().setObject(parameterName, x, targetSqlType);
  }

  @Override
  public void setObject(final String parameterName, final Object x) throws SQLException {
    getStatement().setObject(parameterName, x);
  }

  @Override
  public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
    getStatement().setCharacterStream(parameterName, reader, length);
  }

  @Override
  public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
    getStatement().setDate(parameterName, x, cal);
  }

  @Override
  public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
    getStatement().setTime(parameterName, x, cal);
  }

  @Override
  public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
    getStatement().setTimestamp(parameterName, x, cal);
  }

  @Override
  public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
    getStatement().setNull(parameterName, sqlType, typeName);
  }

  @Override
  public String getString(final String parameterName) throws SQLException {
    return getStatement().getString(parameterName);
  }

  @Override
  public boolean getBoolean(final String parameterName) throws SQLException {
    return getStatement().getBoolean(parameterName);
  }

  @Override
  public byte getByte(final String parameterName) throws SQLException {
    return getStatement().getByte(parameterName);
  }

  @Override
  public short getShort(final String parameterName) throws SQLException {
    return getStatement().getShort(parameterName);
  }

  @Override
  public int getInt(final String parameterName) throws SQLException {
    return getStatement().getInt(parameterName);
  }

  @Override
  public long getLong(final String parameterName) throws SQLException {
    return getStatement().getLong(parameterName);
  }

  @Override
  public float getFloat(final String parameterName) throws SQLException {
    return getStatement().getFloat(parameterName);
  }

  @Override
  public double getDouble(final String parameterName) throws SQLException {
    return getStatement().getDouble(parameterName);
  }

  @Override
  public byte[] getBytes(final String parameterName) throws SQLException {
    return getStatement().getBytes(parameterName);
  }

  @Override
  public Date getDate(final String parameterName) throws SQLException {
    return getStatement().getDate(parameterName);
  }

  @Override
  public Time getTime(final String parameterName) throws SQLException {
    return getStatement().getTime(parameterName);
  }

  @Override
  public Timestamp getTimestamp(final String parameterName) throws SQLException {
    return getStatement().getTimestamp(parameterName);
  }

  @Override
  public Object getObject(final String parameterName) throws SQLException {
    return getStatement().getObject(parameterName);
  }

  @Override
  public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
    return getStatement().getBigDecimal(parameterName);
  }

  @Override
  public Object getObject(final String parameterName, final Map<String,Class<?>> map) throws SQLException {
    return getStatement().getObject(parameterName, map);
  }

  @Override
  public Ref getRef(final String parameterName) throws SQLException {
    return getStatement().getRef(parameterName);
  }

  @Override
  public Blob getBlob(final String parameterName) throws SQLException {
    return getStatement().getBlob(parameterName);
  }

  @Override
  public Clob getClob(final String parameterName) throws SQLException {
    return getStatement().getClob(parameterName);
  }

  @Override
  public Array getArray(final String parameterName) throws SQLException {
    return getStatement().getArray(parameterName);
  }

  @Override
  public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
    return getStatement().getDate(parameterName, cal);
  }

  @Override
  public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
    return getStatement().getTime(parameterName, cal);
  }

  @Override
  public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
    return getStatement().getTimestamp(parameterName, cal);
  }

  @Override
  public URL getURL(final String parameterName) throws SQLException {
    return getStatement().getURL(parameterName);
  }

  @Override
  public RowId getRowId(final int parameterIndex) throws SQLException {
    return getStatement().getRowId(parameterIndex);
  }

  @Override
  public RowId getRowId(final String parameterName) throws SQLException {
    return getStatement().getRowId(parameterName);
  }

  @Override
  public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
    getStatement().setRowId(parameterIndex, x);
  }

  @Override
  public void setNString(final int parameterIndex, final String value) throws SQLException {
    getStatement().setNString(parameterIndex, value);
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
    getStatement().setNCharacterStream(parameterIndex, value, length);
  }

  @Override
  public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
    getStatement().setNClob(parameterIndex, value);
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getStatement().setClob(parameterIndex, reader, length);
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
    getStatement().setBlob(parameterIndex, inputStream, length);
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getStatement().setNClob(parameterIndex, reader, length);
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
    getStatement().setSQLXML(parameterIndex, xmlObject);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getStatement().setAsciiStream(parameterIndex, x, length);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getStatement().setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getStatement().setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
    getStatement().setAsciiStream(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
    getStatement().setBinaryStream(parameterIndex, x);
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
    getStatement().setCharacterStream(parameterIndex, reader);
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
    getStatement().setNCharacterStream(parameterIndex, value);
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
    getStatement().setClob(parameterIndex, reader);
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
    getStatement().setBlob(parameterIndex, inputStream);
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
    getStatement().setNClob(parameterIndex, reader);
  }

  @Override
  public boolean isClosed() throws SQLException {
    return getStatement().isClosed();
  }

  @Override
  public void setPoolable(final boolean poolable) throws SQLException {
    getStatement().setPoolable(poolable);
  }

  @Override
  public boolean isPoolable() throws SQLException {
    return getStatement().isPoolable();
  }

  @Override
  public <T extends Object> T unwrap(final Class<T> iface) throws SQLException {
    return getStatement().unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return getStatement().isWrapperFor(iface);
  }

  @Override
  public void setRowId(final String parameterName, final RowId x) throws SQLException {
    getStatement().setRowId(parameterName, x);
  }

  @Override
  public void setNString(final String parameterName, final String value) throws SQLException {
    getStatement().setNString(parameterName, value);
  }

  @Override
  public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
    getStatement().setNCharacterStream(parameterName, value, length);
  }

  @Override
  public void setNClob(final String parameterName, final NClob value) throws SQLException {
    getStatement().setNClob(parameterName, value);
  }

  @Override
  public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
    getStatement().setClob(parameterName, reader);
  }

  @Override
  public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
    getStatement().setBlob(parameterName, inputStream);
  }

  @Override
  public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
    getStatement().setNClob(parameterName, reader, length);
  }

  @Override
  public NClob getNClob(final int parameterIndex) throws SQLException {
    return getStatement().getNClob(parameterIndex);
  }

  @Override
  public NClob getNClob(final String parameterName) throws SQLException {
    return getStatement().getNClob(parameterName);
  }

  @Override
  public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
    getStatement().setSQLXML(parameterName, xmlObject);
  }

  @Override
  public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
    return getStatement().getSQLXML(parameterIndex);
  }

  @Override
  public SQLXML getSQLXML(final String parameterName) throws SQLException {
    return getStatement().getSQLXML(parameterName);
  }

  @Override
  public String getNString(final int parameterIndex) throws SQLException {
    return getStatement().getNString(parameterIndex);
  }

  @Override
  public String getNString(final String parameterName) throws SQLException {
    return getStatement().getNString(parameterName);
  }

  @Override
  public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
    return getStatement().getNCharacterStream(parameterIndex);
  }

  @Override
  public Reader getNCharacterStream(final String parameterName) throws SQLException {
    return getStatement().getNCharacterStream(parameterName);
  }

  @Override
  public Reader getCharacterStream(final int parameterIndex) throws SQLException {
    return getStatement().getCharacterStream(parameterIndex);
  }

  @Override
  public Reader getCharacterStream(final String parameterName) throws SQLException {
    return getStatement().getCharacterStream(parameterName);
  }

  @Override
  public void setBlob(final String parameterName, final Blob x) throws SQLException {
    getStatement().setBlob(parameterName, x);
  }

  @Override
  public void setClob(final String parameterName, final Clob x) throws SQLException {
    getStatement().setClob(parameterName, x);
  }

  @Override
  public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
    getStatement().setAsciiStream(parameterName, x, length);
  }

  @Override
  public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
    getStatement().setBinaryStream(parameterName, x, length);
  }

  @Override
  public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
    getStatement().setCharacterStream(parameterName, reader, length);
  }

  @Override
  public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
    getStatement().setAsciiStream(parameterName, x);
  }

  @Override
  public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
    getStatement().setBinaryStream(parameterName, x);
  }

  @Override
  public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
    getStatement().setCharacterStream(parameterName, reader);
  }

  @Override
  public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
    getStatement().setNCharacterStream(parameterName, value);
  }

  @Override
  public void setClob(final String parameterName, final Reader reader) throws SQLException {
    getStatement().setClob(parameterName, reader);
  }

  @Override
  public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
    getStatement().setBlob(parameterName, inputStream);
  }

  @Override
  public void setNClob(final String parameterName, final Reader reader) throws SQLException {
    getStatement().setNClob(parameterName, reader);
  }

  @Override
  public <T>T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
    return getStatement().getObject(parameterIndex, type);
  }

  @Override
  public <T>T getObject(final String parameterName, final Class<T> type) throws SQLException {
    return getStatement().getObject(parameterName, type);
  }
}