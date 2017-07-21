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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ResultSetProxy implements ResultSet {
  public static void close(final ResultSet resultSet) {
    try {
      if (resultSet != null && !resultSet.isClosed())
        resultSet.close();
    }
    catch (final SQLException e) {
    }
  }

  private final ResultSet resultSet;
  private volatile boolean mutex = false;

  public ResultSetProxy(final ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  private void checkMutex() {
    if (!mutex)
      return;

    try {
      synchronized (resultSet) {
        resultSet.wait();
      }
    }
    catch (final InterruptedException e) {
    }
  }

  protected Integer getSize() throws SQLException {
    if (resultSet == null)
      return null;

    if (resultSet.getType() <= ResultSet.TYPE_FORWARD_ONLY)
      return -1;

    mutex = true;
    resultSet.last();
    final int size = resultSet.getRow();
    resultSet.beforeFirst();
    mutex = false;
    synchronized (resultSet) {
      resultSet.notifyAll();
    }
    return size;
  }

  @Override
  public boolean next() throws SQLException {
    checkMutex();
    return resultSet.next();
  }

  @Override
  public void close() throws SQLException {
    resultSet.close();
  }

  @Override
  public boolean wasNull() throws SQLException {
    checkMutex();
    return resultSet.wasNull();
  }

  @Override
  public String getString(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getString(columnIndex);
  }

  @Override
  public boolean getBoolean(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getBoolean(columnIndex);
  }

  @Override
  public byte getByte(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getByte(columnIndex);
  }

  @Override
  public short getShort(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getShort(columnIndex);
  }

  @Override
  public int getInt(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getInt(columnIndex);
  }

  @Override
  public long getLong(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getLong(columnIndex);
  }

  @Override
  public float getFloat(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getFloat(columnIndex);
  }

  @Override
  public double getDouble(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getDouble(columnIndex);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
    checkMutex();
    return resultSet.getBigDecimal(columnIndex, scale);
  }

  @Override
  public byte[] getBytes(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getBytes(columnIndex);
  }

  @Override
  public Date getDate(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getDate(columnIndex);
  }

  @Override
  public Time getTime(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getTime(columnIndex);
  }

  @Override
  public Timestamp getTimestamp(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getTimestamp(columnIndex);
  }

  @Override
  public InputStream getAsciiStream(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getAsciiStream(columnIndex);
  }

  @Override
  @Deprecated
  public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getUnicodeStream(columnIndex);
  }

  @Override
  public InputStream getBinaryStream(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getBinaryStream(columnIndex);
  }

  @Override
  public String getString(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getString(columnName);
  }

  @Override
  public boolean getBoolean(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getBoolean(columnName);
  }

  @Override
  public byte getByte(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getByte(columnName);
  }

  @Override
  public short getShort(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getShort(columnName);
  }

  @Override
  public int getInt(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getInt(columnName);
  }

  @Override
  public long getLong(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getLong(columnName);
  }

  @Override
  public float getFloat(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getFloat(columnName);
  }

  @Override
  public double getDouble(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getDouble(columnName);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
    checkMutex();
    return resultSet.getBigDecimal(columnName, scale);
  }

  @Override
  public byte[] getBytes(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getBytes(columnName);
  }

  @Override
  public Date getDate(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getDate(columnName);
  }

  @Override
  public Time getTime(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getTime(columnName);
  }

  @Override
  public Timestamp getTimestamp(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getTimestamp(columnName);
  }

  @Override
  public InputStream getAsciiStream(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getAsciiStream(columnName);
  }

  @Override
  @Deprecated
  public InputStream getUnicodeStream(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getUnicodeStream(columnName);
  }

  @Override
  public InputStream getBinaryStream(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getBinaryStream(columnName);
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return resultSet.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    resultSet.clearWarnings();
  }

  @Override
  public String getCursorName() throws SQLException {
    return resultSet.getCursorName();
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return resultSet.getMetaData();
  }

  @Override
  public Object getObject(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getObject(columnIndex);
  }

  @Override
  public Object getObject(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getObject(columnName);
  }

  @Override
  public int findColumn(final String columnName) throws SQLException {
    return resultSet.findColumn(columnName);
  }

  @Override
  public Reader getCharacterStream(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getCharacterStream(columnIndex);
  }

  @Override
  public Reader getCharacterStream(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getCharacterStream(columnName);
  }

  @Override
  public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getBigDecimal(columnName);
  }

  @Override
  public boolean isBeforeFirst() throws SQLException {
    checkMutex();
    return resultSet.isBeforeFirst();
  }

  @Override
  public boolean isAfterLast() throws SQLException {
    checkMutex();
    return resultSet.isAfterLast();
  }

  @Override
  public boolean isFirst() throws SQLException {
    checkMutex();
    return resultSet.isFirst();
  }

  @Override
  public boolean isLast() throws SQLException {
    checkMutex();
    return resultSet.isLast();
  }

  @Override
  public void beforeFirst() throws SQLException {
    checkMutex();
    resultSet.beforeFirst();
  }

  @Override
  public void afterLast() throws SQLException {
    checkMutex();
    resultSet.afterLast();
  }

  @Override
  public boolean first() throws SQLException {
    checkMutex();
    return resultSet.first();
  }

  @Override
  public boolean last() throws SQLException {
    checkMutex();
    return resultSet.last();
  }

  @Override
  public int getRow() throws SQLException {
    checkMutex();
    return resultSet.getRow();
  }

  @Override
  public boolean absolute(final int row) throws SQLException {
    checkMutex();
    return resultSet.absolute(row);
  }

  @Override
  public boolean relative(final int rows) throws SQLException {
    checkMutex();
    return resultSet.relative(rows);
  }

  @Override
  public boolean previous() throws SQLException {
    checkMutex();
    return resultSet.previous();
  }

  @Override
  public void setFetchDirection(final int direction) throws SQLException {
    checkMutex();
    resultSet.setFetchDirection(direction);
  }

  @Override
  public int getFetchDirection() throws SQLException {
    checkMutex();
    return resultSet.getFetchDirection();
  }

  @Override
  public void setFetchSize(final int rows) throws SQLException {
    checkMutex();
    resultSet.setFetchSize(rows);
  }

  @Override
  public int getFetchSize() throws SQLException {
    checkMutex();
    return resultSet.getFetchSize();
  }

  @Override
  public int getType() throws SQLException {
    return resultSet.getType();
  }

  @Override
  public int getConcurrency() throws SQLException {
    return resultSet.getConcurrency();
  }

  @Override
  public boolean rowUpdated() throws SQLException {
    checkMutex();
    return resultSet.rowUpdated();
  }

  @Override
  public boolean rowInserted() throws SQLException {
    checkMutex();
    return resultSet.rowInserted();
  }

  @Override
  public boolean rowDeleted() throws SQLException {
    checkMutex();
    return resultSet.rowDeleted();
  }

  @Override
  public void updateNull(final int columnIndex) throws SQLException {
    checkMutex();
    resultSet.updateNull(columnIndex);
  }

  @Override
  public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
    checkMutex();
    resultSet.updateBoolean(columnIndex, x);
  }

  @Override
  public void updateByte(final int columnIndex, final byte x) throws SQLException {
    checkMutex();
    resultSet.updateByte(columnIndex, x);
  }

  @Override
  public void updateShort(final int columnIndex, final short x) throws SQLException {
    checkMutex();
    resultSet.updateShort(columnIndex, x);
  }

  @Override
  public void updateInt(final int columnIndex, final int x) throws SQLException {
    checkMutex();
    resultSet.updateInt(columnIndex, x);
  }

  @Override
  public void updateLong(final int columnIndex, final long x) throws SQLException {
    checkMutex();
    resultSet.updateLong(columnIndex, x);
  }

  @Override
  public void updateFloat(final int columnIndex, final float x) throws SQLException {
    checkMutex();
    resultSet.updateFloat(columnIndex, x);
  }

  @Override
  public void updateDouble(final int columnIndex, final double x) throws SQLException {
    checkMutex();
    resultSet.updateDouble(columnIndex, x);
  }

  @Override
  public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
    checkMutex();
    resultSet.updateBigDecimal(columnIndex, x);
  }

  @Override
  public void updateString(final int columnIndex, final String x) throws SQLException {
    checkMutex();
    resultSet.updateString(columnIndex, x);
  }

  @Override
  public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
    checkMutex();
    resultSet.updateBytes(columnIndex, x);
  }

  @Override
  public void updateDate(final int columnIndex, final Date x) throws SQLException {
    checkMutex();
    resultSet.updateDate(columnIndex, x);
  }

  @Override
  public void updateTime(final int columnIndex, final Time x) throws SQLException {
    checkMutex();
    resultSet.updateTime(columnIndex, x);
  }

  @Override
  public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
    checkMutex();
    resultSet.updateTimestamp(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
    checkMutex();
    resultSet.updateAsciiStream(columnIndex, x, length);
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
    checkMutex();
    resultSet.updateBinaryStream(columnIndex, x, length);
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
    checkMutex();
    resultSet.updateCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateObject(final int columnIndex, final Object x, final int scale) throws SQLException {
    checkMutex();
    resultSet.updateObject(columnIndex, x, scale);
  }

  @Override
  public void updateObject(final int columnIndex, final Object x) throws SQLException {
    checkMutex();
    resultSet.updateObject(columnIndex, x);
  }

  @Override
  public void updateNull(final String columnName) throws SQLException {
    checkMutex();
    resultSet.updateNull(columnName);
  }

  @Override
  public void updateBoolean(final String columnName, final boolean x) throws SQLException {
    checkMutex();
    resultSet.updateBoolean(columnName, x);
  }

  @Override
  public void updateByte(final String columnName, final byte x) throws SQLException {
    checkMutex();
    resultSet.updateByte(columnName, x);
  }

  @Override
  public void updateShort(final String columnName, final short x) throws SQLException {
    checkMutex();
    resultSet.updateShort(columnName, x);
  }

  @Override
  public void updateInt(final String columnName, final int x) throws SQLException {
    checkMutex();
    resultSet.updateInt(columnName, x);
  }

  @Override
  public void updateLong(final String columnName, final long x) throws SQLException {
    checkMutex();
    resultSet.updateLong(columnName, x);
  }

  @Override
  public void updateFloat(final String columnName, final float x) throws SQLException {
    checkMutex();
    resultSet.updateFloat(columnName, x);
  }

  @Override
  public void updateDouble(final String columnName, final double x) throws SQLException {
    checkMutex();
    resultSet.updateDouble(columnName, x);
  }

  @Override
  public void updateBigDecimal(final String columnName, final BigDecimal x) throws SQLException {
    checkMutex();
    resultSet.updateBigDecimal(columnName, x);
  }

  @Override
  public void updateString(final String columnName, final String x) throws SQLException {
    checkMutex();
    resultSet.updateString(columnName, x);
  }

  @Override
  public void updateBytes(final String columnName, final byte[] x) throws SQLException {
    checkMutex();
    resultSet.updateBytes(columnName, x);
  }

  @Override
  public void updateDate(final String columnName, final Date x) throws SQLException {
    checkMutex();
    resultSet.updateDate(columnName, x);
  }

  @Override
  public void updateTime(final String columnName, final Time x) throws SQLException {
    checkMutex();
    resultSet.updateTime(columnName, x);
  }

  @Override
  public void updateTimestamp(final String columnName, final Timestamp x) throws SQLException {
    checkMutex();
    resultSet.updateTimestamp(columnName, x);
  }

  @Override
  public void updateAsciiStream(final String columnName, final InputStream x, final int length) throws SQLException {
    checkMutex();
    resultSet.updateAsciiStream(columnName, x, length);
  }

  @Override
  public void updateBinaryStream(final String columnName, final InputStream x, final int length) throws SQLException {
    checkMutex();
    resultSet.updateBinaryStream(columnName, x, length);
  }

  @Override
  public void updateCharacterStream(final String columnName, final Reader reader, final int length) throws SQLException {
    checkMutex();
    resultSet.updateCharacterStream(columnName, reader, length);
  }

  @Override
  public void updateObject(final String columnName, final Object x, final int scale) throws SQLException {
    checkMutex();
    resultSet.updateObject(columnName, x, scale);
  }

  @Override
  public void updateObject(final String columnName, final Object x) throws SQLException {
    checkMutex();
    resultSet.updateObject(columnName, x);
  }

  @Override
  public void insertRow() throws SQLException {
    checkMutex();
    resultSet.insertRow();
  }

  @Override
  public void updateRow() throws SQLException {
    checkMutex();
    resultSet.updateRow();
  }

  @Override
  public void deleteRow() throws SQLException {
    checkMutex();
    resultSet.deleteRow();
  }

  @Override
  public void refreshRow() throws SQLException {
    checkMutex();
    resultSet.refreshRow();
  }

  @Override
  public void cancelRowUpdates() throws SQLException {
    checkMutex();
    resultSet.cancelRowUpdates();
  }

  @Override
  public void moveToInsertRow() throws SQLException {
    checkMutex();
    resultSet.moveToInsertRow();
  }

  @Override
  public void moveToCurrentRow() throws SQLException {
    checkMutex();
    resultSet.moveToCurrentRow();
  }

  @Override
  public Statement getStatement() throws SQLException {
    return resultSet.getStatement();
  }

  @Override
  public Object getObject(final int i, final Map<String,Class<?>> map) throws SQLException {
    checkMutex();
    return resultSet.getObject(i, map);
  }

  @Override
  public Ref getRef(final int i) throws SQLException {
    checkMutex();
    return resultSet.getRef(i);
  }

  @Override
  public Blob getBlob(final int i) throws SQLException {
    checkMutex();
    return resultSet.getBlob(i);
  }

  @Override
  public Clob getClob(final int i) throws SQLException {
    checkMutex();
    return resultSet.getClob(i);
  }

  @Override
  public Array getArray(final int i) throws SQLException {
    checkMutex();
    return resultSet.getArray(i);
  }

  @Override
  public Object getObject(final String colName, final Map<String,Class<?>> map) throws SQLException {
    checkMutex();
    return resultSet.getObject(colName, map);
  }

  @Override
  public Ref getRef(final String colName) throws SQLException {
    checkMutex();
    return resultSet.getRef(colName);
  }

  @Override
  public Blob getBlob(final String colName) throws SQLException {
    checkMutex();
    return resultSet.getBlob(colName);
  }

  @Override
  public Clob getClob(final String colName) throws SQLException {
    checkMutex();
    return resultSet.getClob(colName);
  }

  @Override
  public Array getArray(final String colName) throws SQLException {
    checkMutex();
    return resultSet.getArray(colName);
  }

  @Override
  public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
    checkMutex();
    return resultSet.getDate(columnIndex, cal);
  }

  @Override
  public Date getDate(final String columnName, final Calendar cal) throws SQLException {
    checkMutex();
    return resultSet.getDate(columnName, cal);
  }

  @Override
  public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
    checkMutex();
    return resultSet.getTime(columnIndex, cal);
  }

  @Override
  public Time getTime(final String columnName, final Calendar cal) throws SQLException {
    checkMutex();
    return resultSet.getTime(columnName, cal);
  }

  @Override
  public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
    checkMutex();
    return resultSet.getTimestamp(columnIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(final String columnName, final Calendar cal) throws SQLException {
    checkMutex();
    return resultSet.getTimestamp(columnName, cal);
  }

  @Override
  public URL getURL(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getURL(columnIndex);
  }

  @Override
  public URL getURL(final String columnName) throws SQLException {
    checkMutex();
    return resultSet.getURL(columnName);
  }

  @Override
  public void updateRef(final int columnIndex, final Ref x) throws SQLException {
    checkMutex();
    resultSet.updateRef(columnIndex, x);
  }

  @Override
  public void updateRef(final String columnName, final Ref x) throws SQLException {
    checkMutex();
    resultSet.updateRef(columnName, x);
  }

  @Override
  public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
    checkMutex();
    resultSet.updateBlob(columnIndex, x);
  }

  @Override
  public void updateBlob(final String columnName, final Blob x) throws SQLException {
    checkMutex();
    resultSet.updateBlob(columnName, x);
  }

  @Override
  public void updateClob(final int columnIndex, final Clob x) throws SQLException {
    checkMutex();
    resultSet.updateClob(columnIndex, x);
  }

  @Override
  public void updateClob(final String columnName, final Clob x) throws SQLException {
    checkMutex();
    resultSet.updateClob(columnName, x);
  }

  @Override
  public void updateArray(final int columnIndex, final Array x) throws SQLException {
    checkMutex();
    resultSet.updateArray(columnIndex, x);
  }

  @Override
  public void updateArray(final String columnName, final Array x) throws SQLException {
    checkMutex();
    resultSet.updateArray(columnName, x);
  }

  @Override
  public RowId getRowId(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getRowId(columnIndex);
  }

  @Override
  public RowId getRowId(final String columnLabel) throws SQLException {
    checkMutex();
    return resultSet.getRowId(columnLabel);
  }

  @Override
  public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
    checkMutex();
    resultSet.updateRowId(columnIndex, x);
  }

  @Override
  public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
    checkMutex();
    resultSet.updateRowId(columnLabel, x);
  }

  @Override
  public int getHoldability() throws SQLException {
    return resultSet.getHoldability();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return resultSet.isClosed();
  }

  @Override
  public void updateNString(final int columnIndex, final String nString) throws SQLException {
    checkMutex();
    resultSet.updateNString(columnIndex, nString);
  }

  @Override
  public void updateNString(final String columnLabel, final String nString) throws SQLException {
    checkMutex();
    resultSet.updateNString(columnLabel, nString);
  }

  @Override
  public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
    checkMutex();
    resultSet.updateNClob(columnIndex, nClob);
  }

  @Override
  public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
    checkMutex();
    resultSet.updateNClob(columnLabel, nClob);
  }

  @Override
  public NClob getNClob(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getNClob(columnIndex);
  }

  @Override
  public NClob getNClob(final String columnLabel) throws SQLException {
    checkMutex();
    return resultSet.getNClob(columnLabel);
  }

  @Override
  public SQLXML getSQLXML(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getSQLXML(columnIndex);
  }

  @Override
  public SQLXML getSQLXML(final String columnLabel) throws SQLException {
    checkMutex();
    return resultSet.getSQLXML(columnLabel);
  }

  @Override
  public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
    checkMutex();
    resultSet.updateSQLXML(columnIndex, xmlObject);
  }

  @Override
  public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
    checkMutex();
    resultSet.updateSQLXML(columnLabel, xmlObject);
  }

  @Override
  public String getNString(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getNString(columnIndex);
  }

  @Override
  public String getNString(final String columnLabel) throws SQLException {
    checkMutex();
    return resultSet.getNString(columnLabel);
  }

  @Override
  public Reader getNCharacterStream(final int columnIndex) throws SQLException {
    checkMutex();
    return resultSet.getNCharacterStream(columnIndex);
  }

  @Override
  public Reader getNCharacterStream(final String columnLabel) throws SQLException {
    checkMutex();
    return resultSet.getNCharacterStream(columnLabel);
  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
    checkMutex();
    resultSet.updateNCharacterStream(columnIndex, x, length);
  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
    checkMutex();
    resultSet.updateNCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
    checkMutex();
    resultSet.updateAsciiStream(columnIndex, x);
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
    checkMutex();
    resultSet.updateBinaryStream(columnIndex, x);
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
    checkMutex();
    resultSet.updateCharacterStream(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
    checkMutex();
    resultSet.updateAsciiStream(columnLabel, x);
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
    checkMutex();
    resultSet.updateBinaryStream(columnLabel, x);
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
    checkMutex();
    resultSet.updateCharacterStream(columnLabel, reader, length);
  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
    checkMutex();
    resultSet.updateBlob(columnIndex, inputStream);
  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
    checkMutex();
    resultSet.updateBlob(columnLabel, inputStream, length);
  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
    checkMutex();
    resultSet.updateClob(columnIndex, reader, length);
  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
    checkMutex();
    resultSet.updateClob(columnLabel, reader, length);
  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
    checkMutex();
    resultSet.updateNClob(columnIndex, reader, length);
  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
    checkMutex();
    resultSet.updateNClob(columnLabel, reader, length);
  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
    checkMutex();
    resultSet.updateNCharacterStream(columnIndex, x);
  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
    checkMutex();
    resultSet.updateNCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
    checkMutex();
    resultSet.updateAsciiStream(columnIndex, x);
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
    checkMutex();
    resultSet.updateBinaryStream(columnIndex, x);
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
    checkMutex();
    resultSet.updateCharacterStream(columnIndex, x);
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
    checkMutex();
    resultSet.updateAsciiStream(columnLabel, x);
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
    checkMutex();
    resultSet.updateBinaryStream(columnLabel, x);
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
    checkMutex();
    resultSet.updateCharacterStream(columnLabel, reader);
  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
    checkMutex();
    resultSet.updateBlob(columnIndex, inputStream);
  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
    checkMutex();
    resultSet.updateBlob(columnLabel, inputStream);
  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
    checkMutex();
    resultSet.updateClob(columnIndex, reader);
  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
    checkMutex();
    resultSet.updateClob(columnLabel, reader);
  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
    checkMutex();
    resultSet.updateNClob(columnIndex, reader);
  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
    checkMutex();
    resultSet.updateNClob(columnLabel, reader);
  }

  @Override
  public <T extends Object>T unwrap(final Class<T> iface) throws SQLException {
    return resultSet.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return resultSet.isWrapperFor(iface);
  }

  @Override
  public <T>T getObject(final int columnIndex, final Class<T> type) throws SQLException {
    return resultSet.getObject(columnIndex, type);
  }

  @Override
  public <T>T getObject(final String columnLabel, final Class<T> type) throws SQLException {
    return resultSet.getObject(columnLabel, type);
  }
}