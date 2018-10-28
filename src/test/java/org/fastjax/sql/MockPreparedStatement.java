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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Mock implementation of {@link PreparedStatement}.
 */
public class MockPreparedStatement extends MockStatement implements PreparedStatement {
  @Override
  public ResultSet executeQuery() throws SQLException {
    return null;
  }

  @Override
  public int executeUpdate() throws SQLException {
    return 0;
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) throws SQLException {
  }

  @Override
  public void setShort(final int parameterIndex, final short x) throws SQLException {
  }

  @Override
  public void setInt(final int parameterIndex, final int x) throws SQLException {
  }

  @Override
  public void setLong(final int parameterIndex, final long x) throws SQLException {
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) throws SQLException {
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) throws SQLException {
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
  }

  @Override
  public void setString(final int parameterIndex, final String x) throws SQLException {
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) throws SQLException {
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) throws SQLException {
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
  }

  @Override
  public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
  }

  @Override
  public void clearParameters() throws SQLException {
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
  }

  @Override
  public void setObject(final int parameterIndex, final Object x) throws SQLException {
  }

  @Override
  public boolean execute() throws SQLException {
    return false;
  }

  @Override
  public void addBatch() throws SQLException {
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
  }

  @Override
  public void setRef(final int parameterIndex, final Ref x) throws SQLException {
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
  }

  @Override
  public void setClob(final int parameterIndex, final Clob x) throws SQLException {
  }

  @Override
  public void setArray(final int parameterIndex, final Array x) throws SQLException {
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return null;
  }

  @Override
  public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
  }

  @Override
  public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) throws SQLException {
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    return null;
  }

  @Override
  public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
  }

  @Override
  public void setNString(final int parameterIndex, final String value) throws SQLException {
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
  }

  @Override
  public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
  }
}