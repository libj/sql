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

import static org.libj.sql.Util.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.libj.lang.Hexadecimal;
import org.libj.util.DecimalFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link PreparedStatement} that delegates all method calls to another
 * statement. This class overrides all execution methods in order to log the SQL
 * that is executed. When an "execute" method is invoked, a debug message with
 * the executed SQL will be logged to the logger associated with the
 * {@link AuditPreparedStatement} class.
 * <p>
 * This class overrides {@link Object#toString()} to return a detailed rendering
 * of the prepared SQL statement with its parameters applied.
 */
public class AuditPreparedStatement extends AuditStatement implements DelegatePreparedStatement {
  private static final Logger logger = LoggerFactory.getLogger(AuditPreparedStatement.class);

  protected static final String NULL = "NULL";

  private static String toString(final Object value) {
    if (value == NULL)
      return "NULL";

    if (value instanceof byte[])
      return "X'" + new Hexadecimal((byte[])value) + "'";

    if (value instanceof Date)
      return "'" + ((Date)value).toLocalDate().format(dateFormat) + "'";

    if (value instanceof Time)
      return "'" + DateTimes.toLocalTime((Time)value).format(timeFormat) + "'";

    if (value instanceof Timestamp)
      return "'" + ((Timestamp)value).toLocalDateTime().format(timestampFormat) + "'";

    if (value instanceof String || value instanceof URL)
      return "'" + value + "'";

    if (value instanceof Byte) {
      final byte ch = (Byte)value;
      return ' ' < ch && ch < '~' ? "'" + (char)ch + "'" : ("0x" + Integer.toHexString(ch & 0xFF).toUpperCase());
    }

    if (value instanceof Number)
      return numberFormat.get().format(value);

    if (value instanceof Boolean)
      return (Boolean)value ? "TRUE" : "FALSE";

    if (value != null)
      return value.toString();

    return "?";
  }

  private static int writeParameter(final StringBuilder builder, final int start, final int end, final Object obj) {
    final int len = builder.length();
    builder.replace(start, end, toString(obj));
    return builder.length() - len;
  }

  // FIXME: Add support for "foo => ?" syntax
  private static String toString(final String sql, final Map<Object,Object> parameterMap) {
    int index = 0;
    boolean escaped = false;
    char inQuote = '\0';
    int colon = -1;
    boolean namedQuoted = false;
    final StringBuilder builder = new StringBuilder(sql);
    for (int i = 0; i < builder.length(); ++i) {
      final char ch = builder.charAt(i);
      if (colon != -1) {
        if (colon == i - 1 && ch == '"') {
          namedQuoted = true;
        }
        else if (ch == '"' && namedQuoted || ch != '#' && ch != '$' && (ch < '0' || '9' < ch) && (ch < '@' || 'Z' < ch) && ch != '_' && (ch < 'a' || 'z' < ch)) {
          i += writeParameter(builder, colon, i, parameterMap.get(builder.substring(colon + 1, i)));
          colon = -1;
        }

        continue;
      }

      if (ch == '\\') {
        escaped = true;
        continue;
      }

      if (inQuote == '\0' || !escaped) {
        if (ch == '\'' || ch == '"') {
          inQuote = inQuote == ch ? '\0' : ch;
        }
        else if (inQuote == '\0') {
          if (ch == ':')
            colon = i;
          else if (ch == '?')
            i += writeParameter(builder, i, i + 1, parameterMap.get(++index));
        }
      }

      escaped = false;
    }

    return builder.toString();
  }

  private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final DateTimeFormatter timeFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").appendFraction(ChronoField.MILLI_OF_SECOND, 0, 6, true).toFormatter();
  private static final DateTimeFormatter timestampFormat = new DateTimeFormatterBuilder().append(dateFormat).appendLiteral(' ').append(timeFormat).toFormatter();
  private static final ThreadLocal<DecimalFormat> numberFormat = DecimalFormatter.createDecimalFormat("###############.###############;-###############.###############");

  private final List<Map<Object,Object>> parameterMaps = new ArrayList<>();
  private final String sql;

  /**
   * Creates a new {@link AuditPreparedStatement} with the specified
   * {@code target} to which all method calls will be delegated.
   *
   * @param target The {@link PreparedStatement} to which all method calls will
   *          be delegated.
   * @param sql A SQL statement to be sent to the database; may contain one or
   *          more '?' parameters.
   */
  public AuditPreparedStatement(final PreparedStatement target, final String sql) {
    super(target);
    this.sql = sql;
    parameterMaps.add(new HashMap<>());
  }

  @Override
  public PreparedStatement getTarget() {
    return (PreparedStatement)super.getTarget();
  }

  /**
   * Returns the map of index-to-value parameters for the current batch.
   *
   * @return The map of index-to-value parameters for the current batch.
   */
  protected Map<Object,Object> getCurrentParameterMap() {
    return parameterMaps.get(parameterMaps.size() - 1);
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    final PreparedStatement statement = getTarget();
    int size = -1;
    final long time = System.currentTimeMillis();
    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "executeQuery", toString()).toString());

      final ResultSet resultSet = statement.executeQuery();
      if (logger.isDebugEnabled())
        size = ResultSets.getSize(resultSet);

      return resultSet;
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(withResult(log(this, "executeQuery", toString()), size, time).toString());
    }
  }

  @Override
  public int executeUpdate() throws SQLException {
    final long time = System.currentTimeMillis();
    int count = -1;
    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "executeUpdate", toString()).toString());

      return count = getTarget().executeUpdate();
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(withResult(log(this, "executeUpdate", toString()), count, time).toString());
    }
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
    getTarget().setNull(parameterIndex, sqlType);
    getCurrentParameterMap().put(parameterIndex, NULL);
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
    getTarget().setBoolean(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) throws SQLException {
    getTarget().setByte(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setShort(final int parameterIndex, final short x) throws SQLException {
    getTarget().setShort(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setInt(final int parameterIndex, final int x) throws SQLException {
    getTarget().setInt(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setLong(final int parameterIndex, final long x) throws SQLException {
    getTarget().setLong(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) throws SQLException {
    getTarget().setFloat(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) throws SQLException {
    getTarget().setDouble(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
    getTarget().setBigDecimal(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setString(final int parameterIndex, final String x) throws SQLException {
    getTarget().setString(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
    getTarget().setBytes(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) throws SQLException {
    getTarget().setDate(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) throws SQLException {
    getTarget().setTime(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
    getTarget().setTimestamp(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x, length);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  @Deprecated//(since="1.2")
  public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setUnicodeStream(parameterIndex, x, length);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x, length);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void clearParameters() throws SQLException {
    getTarget().clearParameters();
    getCurrentParameterMap().clear();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scale) throws SQLException {
    getTarget().setObject(parameterIndex, x, targetSqlType, scale);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
    getTarget().setObject(parameterIndex, x, targetSqlType);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setObject(final int parameterIndex, final Object x) throws SQLException {
    getTarget().setObject(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public boolean execute() throws SQLException {
    final long time = System.currentTimeMillis();
    boolean result = false;
    try {
      if (logger.isTraceEnabled())
        logger.trace(log(this, "execute", toString()).toString());

      return result = getTarget().execute();
    }
    finally {
      if (logger.isDebugEnabled())
        logger.debug(withResult(log(this, "execute", toString()), result, time).toString());
    }
  }

  @Override
  public void addBatch() throws SQLException {
    addBatch0(toString(sql, getCurrentParameterMap()));
    parameterMaps.add(new HashMap<>());
    getTarget().addBatch();
  }

  @Override
  public void setRef(final int parameterIndex, final Ref x) throws SQLException {
    getTarget().setRef(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
    getTarget().setBlob(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setClob(final int parameterIndex, final Clob x) throws SQLException {
    getTarget().setClob(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setArray(final int parameterIndex, final Array x) throws SQLException {
    getTarget().setArray(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
    getTarget().setDate(parameterIndex, x, cal);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
    getTarget().setTime(parameterIndex, x, cal);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
    getTarget().setTimestamp(parameterIndex, x, cal);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
    getTarget().setNull(parameterIndex, sqlType, typeName);
    getCurrentParameterMap().put(parameterIndex, NULL);
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) throws SQLException {
    getTarget().setURL(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
    getTarget().setRowId(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setNString(final int parameterIndex, final String value) throws SQLException {
    getTarget().setNString(parameterIndex, value);
    getCurrentParameterMap().put(parameterIndex, value);
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
    getTarget().setNCharacterStream(parameterIndex, value, length);
    getCurrentParameterMap().put(parameterIndex, value);
  }

  @Override
  public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
    getTarget().setNClob(parameterIndex, value);
    getCurrentParameterMap().put(parameterIndex, value);
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setClob(parameterIndex, reader, length);
    getCurrentParameterMap().put(parameterIndex, reader);
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
    getTarget().setBlob(parameterIndex, inputStream, length);
    getCurrentParameterMap().put(parameterIndex, inputStream);
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setNClob(parameterIndex, reader, length);
    getCurrentParameterMap().put(parameterIndex, reader);
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
    getTarget().setSQLXML(parameterIndex, xmlObject);
    getCurrentParameterMap().put(parameterIndex, xmlObject);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x, length);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x, length);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader, length);
    getCurrentParameterMap().put(parameterIndex, reader);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x);
    getCurrentParameterMap().put(parameterIndex, x);
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader);
    getCurrentParameterMap().put(parameterIndex, reader);
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
    getTarget().setNCharacterStream(parameterIndex, value);
    getCurrentParameterMap().put(parameterIndex, value);
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setClob(parameterIndex, reader);
    getCurrentParameterMap().put(parameterIndex, reader);
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
    getTarget().setBlob(parameterIndex, inputStream);
    getCurrentParameterMap().put(parameterIndex, inputStream);
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setNClob(parameterIndex, reader);
    getCurrentParameterMap().put(parameterIndex, reader);
  }

  /**
   * Returns a string representation of this instance's prepared SQL statement
   * with its parameters applied.
   *
   * @return A string representation of this instance's prepared SQL statement
   *         with its parameters applied.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    final Iterator<Map<Object,Object>> iterator = parameterMaps.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      if (i > 0)
        builder.append('\n');

      builder.append(toString(sql, iterator.next()));
    }

    return builder.toString();
  }
}