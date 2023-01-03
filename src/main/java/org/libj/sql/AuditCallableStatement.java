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

/**
 * A {@link CallableStatement} that delegates all method calls to another statement. This class overrides all execution methods in
 * order to log the SQL that is executed. When an "execute" method is invoked, a debug message with the executed SQL will be logged
 * to the logger associated with the {@link AuditCallableStatement} class.
 * <p>
 * This class overrides {@link Object#toString()} to return a detailed rendering of the prepared SQL statement with its parameters
 * applied.
 */
public class AuditCallableStatement extends AuditPreparedStatement implements DelegateCallableStatement {
  /**
   * Returns a {@link AuditCallableStatement} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   * {@link CallableStatement}.
   *
   * @param target The {@link CallableStatement} to wrap.
   * @param sql A SQL statement to be sent to the database; may contain one or more '?' parameters.
   * @return A {@link AuditCallableStatement} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   *         {@link CallableStatement}.
   */
  public static CallableStatement wrapIfDebugEnabled(final CallableStatement target, final String sql) {
    return logger.isDebugEnabled() ? new AuditCallableStatement(target, sql) : target;
  }

  /**
   * Creates a new {@link AuditCallableStatement} with the specified {@code target} to which all method calls will be delegated.
   *
   * @param target The {@link CallableStatement} to which all method calls will be delegated.
   * @param sql A SQL statement to be sent to the database; may contain one or more '?' parameters, or may contain one or more named
   *          parameters.
   */
  public AuditCallableStatement(final CallableStatement target, final String sql) {
    super(target, sql);
  }

  @Override
  public CallableStatement getTarget() {
    return (CallableStatement)super.getTarget();
  }

  @Override
  public void registerOutParameter(final int parameterIndex, final int sqlType) throws SQLException {
    getTarget().registerOutParameter(parameterIndex, sqlType);
  }

  @Override
  public void registerOutParameter(final int parameterIndex, final int sqlType, final int scale) throws SQLException {
    getTarget().registerOutParameter(parameterIndex, sqlType, scale);
  }

  @Override
  public void registerOutParameter(final int paramIndex, final int sqlType, final String typeName) throws SQLException {
    getTarget().registerOutParameter(paramIndex, sqlType, typeName);
  }

  @Override
  public void registerOutParameter(final String parameterName, final int sqlType) throws SQLException {
    getTarget().registerOutParameter(parameterName, sqlType);
  }

  @Override
  public void registerOutParameter(final String parameterName, final int sqlType, final int scale) throws SQLException {
    getTarget().registerOutParameter(parameterName, sqlType, scale);
  }

  @Override
  public void registerOutParameter(final String parameterName, final int sqlType, final String typeName) throws SQLException {
    getTarget().registerOutParameter(parameterName, sqlType, typeName);
  }

  @Override
  public boolean wasNull() throws SQLException {
    return getTarget().wasNull();
  }

  @Override
  public String getString(final int parameterIndex) throws SQLException {
    return getTarget().getString(parameterIndex);
  }

  @Override
  public boolean getBoolean(final int parameterIndex) throws SQLException {
    return getTarget().getBoolean(parameterIndex);
  }

  @Override
  public byte getByte(final int parameterIndex) throws SQLException {
    return getTarget().getByte(parameterIndex);
  }

  @Override
  public short getShort(final int parameterIndex) throws SQLException {
    return getTarget().getShort(parameterIndex);
  }

  @Override
  public int getInt(final int parameterIndex) throws SQLException {
    return getTarget().getInt(parameterIndex);
  }

  @Override
  public long getLong(final int parameterIndex) throws SQLException {
    return getTarget().getLong(parameterIndex);
  }

  @Override
  public float getFloat(final int parameterIndex) throws SQLException {
    return getTarget().getFloat(parameterIndex);
  }

  @Override
  public double getDouble(final int parameterIndex) throws SQLException {
    return getTarget().getDouble(parameterIndex);
  }

  @Override
  @Deprecated//(since="1.2")
  public BigDecimal getBigDecimal(final int parameterIndex, final int scale) throws SQLException {
    return getTarget().getBigDecimal(parameterIndex);
  }

  @Override
  public byte[] getBytes(final int parameterIndex) throws SQLException {
    return getTarget().getBytes(parameterIndex);
  }

  @Override
  public Date getDate(final int parameterIndex) throws SQLException {
    return getTarget().getDate(parameterIndex);
  }

  @Override
  public Time getTime(final int parameterIndex) throws SQLException {
    return getTarget().getTime(parameterIndex);
  }

  @Override
  public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
    return getTarget().getTimestamp(parameterIndex);
  }

  @Override
  public Object getObject(final int parameterIndex) throws SQLException {
    return getTarget().getObject(parameterIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
    return getTarget().getBigDecimal(parameterIndex);
  }

  @Override
  public Object getObject(final int i, final Map<String,Class<?>> map) throws SQLException {
    return getTarget().getObject(i, map);
  }

  @Override
  public Ref getRef(final int i) throws SQLException {
    return getTarget().getRef(i);
  }

  @Override
  public Blob getBlob(final int i) throws SQLException {
    return getTarget().getBlob(i);
  }

  @Override
  public Clob getClob(final int i) throws SQLException {
    return getTarget().getClob(i);
  }

  @Override
  public Array getArray(final int i) throws SQLException {
    return getTarget().getArray(i);
  }

  @Override
  public Date getDate(final int parameterIndex, final Calendar cal) throws SQLException {
    return getTarget().getDate(parameterIndex, cal);
  }

  @Override
  public Time getTime(final int parameterIndex, final Calendar cal) throws SQLException {
    return getTarget().getTime(parameterIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(final int parameterIndex, final Calendar cal) throws SQLException {
    return getTarget().getTimestamp(parameterIndex, cal);
  }

  @Override
  public URL getURL(final int parameterIndex) throws SQLException {
    return getTarget().getURL(parameterIndex);
  }

  @Override
  public void setURL(final String parameterName, final URL val) throws SQLException {
    getTarget().setURL(parameterName, val);
    getCurrentParameterMap().put(parameterName, val);

  }

  @Override
  public void setNull(final String parameterName, final int sqlType) throws SQLException {
    getTarget().setNull(parameterName, sqlType);
    getCurrentParameterMap().put(parameterName, NULL);
  }

  @Override
  public void setBoolean(final String parameterName, final boolean x) throws SQLException {
    getTarget().setBoolean(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setByte(final String parameterName, final byte x) throws SQLException {
    getTarget().setByte(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setShort(final String parameterName, final short x) throws SQLException {
    getTarget().setShort(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setInt(final String parameterName, final int x) throws SQLException {
    getTarget().setInt(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setLong(final String parameterName, final long x) throws SQLException {
    getTarget().setLong(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setFloat(final String parameterName, final float x) throws SQLException {
    getTarget().setFloat(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setDouble(final String parameterName, final double x) throws SQLException {
    getTarget().setDouble(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setBigDecimal(final String parameterName, final BigDecimal x) throws SQLException {
    getTarget().setBigDecimal(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setString(final String parameterName, final String x) throws SQLException {
    getTarget().setString(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setBytes(final String parameterName, final byte[] x) throws SQLException {
    getTarget().setBytes(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setDate(final String parameterName, final Date x) throws SQLException {
    getTarget().setDate(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setTime(final String parameterName, final Time x) throws SQLException {
    getTarget().setTime(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setTimestamp(final String parameterName, final Timestamp x) throws SQLException {
    getTarget().setTimestamp(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setAsciiStream(final String parameterName, final InputStream x, final int length) throws SQLException {
    getTarget().setAsciiStream(parameterName, x, length);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setBinaryStream(final String parameterName, final InputStream x, final int length) throws SQLException {
    getTarget().setBinaryStream(parameterName, x, length);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setObject(final String parameterName, final Object x, final int targetSqlType, final int scale) throws SQLException {
    getTarget().setObject(parameterName, x, targetSqlType, scale);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setObject(final String parameterName, final Object x, final int targetSqlType) throws SQLException {
    getTarget().setObject(parameterName, x, targetSqlType);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setObject(final String parameterName, final Object x) throws SQLException {
    getTarget().setObject(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setCharacterStream(final String parameterName, final Reader reader, final int length) throws SQLException {
    getTarget().setCharacterStream(parameterName, reader, length);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public void setDate(final String parameterName, final Date x, final Calendar cal) throws SQLException {
    getTarget().setDate(parameterName, x, cal);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setTime(final String parameterName, final Time x, final Calendar cal) throws SQLException {
    getTarget().setTime(parameterName, x, cal);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setTimestamp(final String parameterName, final Timestamp x, final Calendar cal) throws SQLException {
    getTarget().setTimestamp(parameterName, x, cal);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setNull(final String parameterName, final int sqlType, final String typeName) throws SQLException {
    getTarget().setNull(parameterName, sqlType, typeName);
    getCurrentParameterMap().put(parameterName, NULL);
  }

  @Override
  public String getString(final String parameterName) throws SQLException {
    return getTarget().getString(parameterName);
  }

  @Override
  public boolean getBoolean(final String parameterName) throws SQLException {
    return getTarget().getBoolean(parameterName);
  }

  @Override
  public byte getByte(final String parameterName) throws SQLException {
    return getTarget().getByte(parameterName);
  }

  @Override
  public short getShort(final String parameterName) throws SQLException {
    return getTarget().getShort(parameterName);
  }

  @Override
  public int getInt(final String parameterName) throws SQLException {
    return getTarget().getInt(parameterName);
  }

  @Override
  public long getLong(final String parameterName) throws SQLException {
    return getTarget().getLong(parameterName);
  }

  @Override
  public float getFloat(final String parameterName) throws SQLException {
    return getTarget().getFloat(parameterName);
  }

  @Override
  public double getDouble(final String parameterName) throws SQLException {
    return getTarget().getDouble(parameterName);
  }

  @Override
  public byte[] getBytes(final String parameterName) throws SQLException {
    return getTarget().getBytes(parameterName);
  }

  @Override
  public Date getDate(final String parameterName) throws SQLException {
    return getTarget().getDate(parameterName);
  }

  @Override
  public Time getTime(final String parameterName) throws SQLException {
    return getTarget().getTime(parameterName);
  }

  @Override
  public Timestamp getTimestamp(final String parameterName) throws SQLException {
    return getTarget().getTimestamp(parameterName);
  }

  @Override
  public Object getObject(final String parameterName) throws SQLException {
    return getTarget().getObject(parameterName);
  }

  @Override
  public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
    return getTarget().getBigDecimal(parameterName);
  }

  @Override
  public Object getObject(final String parameterName, final Map<String,Class<?>> map) throws SQLException {
    return getTarget().getObject(parameterName, map);
  }

  @Override
  public Ref getRef(final String parameterName) throws SQLException {
    return getTarget().getRef(parameterName);
  }

  @Override
  public Blob getBlob(final String parameterName) throws SQLException {
    return getTarget().getBlob(parameterName);
  }

  @Override
  public Clob getClob(final String parameterName) throws SQLException {
    return getTarget().getClob(parameterName);
  }

  @Override
  public Array getArray(final String parameterName) throws SQLException {
    return getTarget().getArray(parameterName);
  }

  @Override
  public Date getDate(final String parameterName, final Calendar cal) throws SQLException {
    return getTarget().getDate(parameterName, cal);
  }

  @Override
  public Time getTime(final String parameterName, final Calendar cal) throws SQLException {
    return getTarget().getTime(parameterName, cal);
  }

  @Override
  public Timestamp getTimestamp(final String parameterName, final Calendar cal) throws SQLException {
    return getTarget().getTimestamp(parameterName, cal);
  }

  @Override
  public URL getURL(final String parameterName) throws SQLException {
    return getTarget().getURL(parameterName);
  }

  @Override
  public RowId getRowId(final int parameterIndex) throws SQLException {
    return getTarget().getRowId(parameterIndex);
  }

  @Override
  public RowId getRowId(final String parameterName) throws SQLException {
    return getTarget().getRowId(parameterName);
  }

  @Override
  public void setRowId(final String parameterName, final RowId x) throws SQLException {
    getTarget().setRowId(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setNString(final String parameterName, final String value) throws SQLException {
    getTarget().setNString(parameterName, value);
    getCurrentParameterMap().put(parameterName, value);
  }

  @Override
  public void setNCharacterStream(final String parameterName, final Reader value, final long length) throws SQLException {
    getTarget().setNCharacterStream(parameterName, value, length);
    getCurrentParameterMap().put(parameterName, value);
  }

  @Override
  public void setNClob(final String parameterName, final NClob value) throws SQLException {
    getTarget().setNClob(parameterName, value);
    getCurrentParameterMap().put(parameterName, value);
  }

  @Override
  public void setClob(final String parameterName, final Reader reader, final long length) throws SQLException {
    getTarget().setClob(parameterName, reader);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public void setBlob(final String parameterName, final InputStream inputStream, final long length) throws SQLException {
    getTarget().setBlob(parameterName, inputStream);
    getCurrentParameterMap().put(parameterName, inputStream);
  }

  @Override
  public void setNClob(final String parameterName, final Reader reader, final long length) throws SQLException {
    getTarget().setNClob(parameterName, reader, length);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public NClob getNClob(final int parameterIndex) throws SQLException {
    return getTarget().getNClob(parameterIndex);
  }

  @Override
  public NClob getNClob(final String parameterName) throws SQLException {
    return getTarget().getNClob(parameterName);
  }

  @Override
  public void setSQLXML(final String parameterName, final SQLXML xmlObject) throws SQLException {
    getTarget().setSQLXML(parameterName, xmlObject);
    getCurrentParameterMap().put(parameterName, xmlObject);
  }

  @Override
  public SQLXML getSQLXML(final int parameterIndex) throws SQLException {
    return getTarget().getSQLXML(parameterIndex);
  }

  @Override
  public SQLXML getSQLXML(final String parameterName) throws SQLException {
    return getTarget().getSQLXML(parameterName);
  }

  @Override
  public String getNString(final int parameterIndex) throws SQLException {
    return getTarget().getNString(parameterIndex);
  }

  @Override
  public String getNString(final String parameterName) throws SQLException {
    return getTarget().getNString(parameterName);
  }

  @Override
  public Reader getNCharacterStream(final int parameterIndex) throws SQLException {
    return getTarget().getNCharacterStream(parameterIndex);
  }

  @Override
  public Reader getNCharacterStream(final String parameterName) throws SQLException {
    return getTarget().getNCharacterStream(parameterName);
  }

  @Override
  public Reader getCharacterStream(final int parameterIndex) throws SQLException {
    return getTarget().getCharacterStream(parameterIndex);
  }

  @Override
  public Reader getCharacterStream(final String parameterName) throws SQLException {
    return getTarget().getCharacterStream(parameterName);
  }

  @Override
  public void setBlob(final String parameterName, final Blob x) throws SQLException {
    getTarget().setBlob(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setClob(final String parameterName, final Clob x) throws SQLException {
    getTarget().setClob(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setAsciiStream(final String parameterName, final InputStream x, final long length) throws SQLException {
    getTarget().setAsciiStream(parameterName, x, length);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setBinaryStream(final String parameterName, final InputStream x, final long length) throws SQLException {
    getTarget().setBinaryStream(parameterName, x, length);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setCharacterStream(final String parameterName, final Reader reader, final long length) throws SQLException {
    getTarget().setCharacterStream(parameterName, reader, length);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public void setAsciiStream(final String parameterName, final InputStream x) throws SQLException {
    getTarget().setAsciiStream(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setBinaryStream(final String parameterName, final InputStream x) throws SQLException {
    getTarget().setBinaryStream(parameterName, x);
    getCurrentParameterMap().put(parameterName, x);
  }

  @Override
  public void setCharacterStream(final String parameterName, final Reader reader) throws SQLException {
    getTarget().setCharacterStream(parameterName, reader);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public void setNCharacterStream(final String parameterName, final Reader value) throws SQLException {
    getTarget().setNCharacterStream(parameterName, value);
    getCurrentParameterMap().put(parameterName, value);
  }

  @Override
  public void setClob(final String parameterName, final Reader reader) throws SQLException {
    getTarget().setClob(parameterName, reader);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public void setBlob(final String parameterName, final InputStream inputStream) throws SQLException {
    getTarget().setBlob(parameterName, inputStream);
    getCurrentParameterMap().put(parameterName, inputStream);
  }

  @Override
  public void setNClob(final String parameterName, final Reader reader) throws SQLException {
    getTarget().setNClob(parameterName, reader);
    getCurrentParameterMap().put(parameterName, reader);
  }

  @Override
  public <T>T getObject(final int parameterIndex, final Class<T> type) throws SQLException {
    return getTarget().getObject(parameterIndex, type);
  }

  @Override
  public <T>T getObject(final String parameterName, final Class<T> type) throws SQLException {
    return getTarget().getObject(parameterName, type);
  }

  @Override
  public boolean equals(final Object obj) {
    return getTarget().equals(obj);
  }

  @Override
  public int hashCode() {
    return getTarget().hashCode();
  }
}