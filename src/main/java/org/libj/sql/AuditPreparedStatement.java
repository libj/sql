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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
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
import java.util.Map;

import org.libj.lang.Hexadecimal;
import org.libj.util.DecimalFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link PreparedStatement} that delegates all method calls to another statement. This class overrides all execution methods in
 * order to log the SQL that is executed. When an "execute" method is invoked, a debug message with the executed SQL will be logged
 * to the logger associated with the {@link AuditPreparedStatement} class.
 * <p>
 * This class overrides {@link Object#toString()} to return a detailed rendering of the prepared SQL statement with its parameters
 * applied.
 */
public class AuditPreparedStatement extends AuditStatement implements DelegatePreparedStatement {
  static final Logger logger = LoggerFactory.getLogger(AuditPreparedStatement.class);

  protected static final String NULL = "NULL";

  private static String toString(final Object value) {
    if (value == NULL)
      return "NULL";

    if (value instanceof byte[])
      return "X'" + new Hexadecimal((byte[])value) + "'";

    if (value instanceof InputStream) {
      final InputStream in = (InputStream)value;
      try {
        int by = in.read();
        if (by == -1) {
          try {
            in.reset();
          }
          catch (final IOException ignore) {
            return value.toString();
          }
        }

        if (in.markSupported())
          in.mark(Integer.MAX_VALUE);

        by = in.read();
        if (by != -1) {
          final ByteArrayOutputStream out = new ByteArrayOutputStream();
          out.write(by);
          final byte[] buf = new byte[1024];
          for (int len; (len = in.read(buf)) != -1; out.write(buf, 0, len)); // [ST]
          if (in.markSupported())
            in.reset();

          return "X'" + new Hexadecimal(out.toByteArray()) + "'";
        }
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    if (value instanceof Reader) {
      final Reader in = (Reader)value;
      try {
        int ch = in.read();
        if (ch == -1) {
          try {
            in.reset();
          }
          catch (final IOException ignore) {
            return value.toString();
          }
        }

        if (in.markSupported())
          in.mark(Integer.MAX_VALUE);

        ch = in.read();
        if (ch != -1) {
          final StringBuilder b = new StringBuilder();
          b.append('\'').append(ch);
          final char[] buf = new char[1024];
          for (int len; (len = in.read(buf)) != -1; b.append(buf, 0, len)); // [ST]
          if (in.markSupported())
            in.reset();

          b.append('\'');
          return b.toString();
        }
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    if (value instanceof Date)
      return "'" + ((Date)value).toLocalDate().format(dateFormat) + "'";

    if (value instanceof Time)
      return "'" + DateTimes.toLocalTime((Time)value).format(timeFormat) + "'";

    if (value instanceof Timestamp)
      return "'" + ((Timestamp)value).toLocalDateTime().format(timestampFormat) + "'";

    if (value instanceof String || value instanceof URL)
      return "'" + value + "'";

    // if (value instanceof Byte) {
    // final byte ch = (Byte)value;
    // return ' ' < ch && ch < '~' ? "'" + (char)ch + "'" : ("0x" + Integer.toHexString(ch & 0xFF).toUpperCase());
    // }

    if (value instanceof Number)
      return numberFormat.get().format(value);

    if (value instanceof Boolean)
      return (Boolean)value ? "TRUE" : "FALSE";

    if (value == null)
      return "?";

    // FIXME: Need to abstract this!
    if ("oracle.sql.INTERVALDS".equals(value.getClass().getName())) {
      final String str = value.toString();
      return "'" + str.substring(str.indexOf(' ') + 1) + "'";
    }

    return value.toString();
  }

  private static int writeParameter(final StringBuilder b, final int start, final int end, final Object obj) {
    final int len = b.length();
    b.replace(start, end, toString(obj));
    return b.length() - len;
  }

  // FIXME: Add support for "foo => ?" syntax
  private static String toString(final String sql, final Map<Object,Object> parameterMap) {
    int index = 0;
    boolean escaped = false;
    char inQuote = '\0';
    int colon = -1;
    boolean namedQuoted = false;
    final StringBuilder b = new StringBuilder(sql);
    for (int i = 0; i < b.length(); ++i) { // [$]
      final char ch = b.charAt(i);
      if (colon != -1) {
        if (colon == i - 1 && ch == '"') {
          namedQuoted = true;
        }
        else if (ch == '"' && namedQuoted || ch != '#' && ch != '$' && (ch < '0' || '9' < ch) && (ch < '@' || 'Z' < ch) && ch != '_' && (ch < 'a' || 'z' < ch)) {
          i += writeParameter(b, colon, i, parameterMap.get(b.substring(colon + 1, i)));
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
            i += writeParameter(b, i, i + 1, parameterMap.get(++index));
        }
      }

      escaped = false;
    }

    return b.toString();
  }

  /**
   * Returns a {@link AuditPreparedStatement} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   * {@link PreparedStatement}.
   *
   * @param target The {@link PreparedStatement} to wrap.
   * @param sql A SQL statement to be sent to the database; may contain one or more '?' parameters.
   * @return A {@link AuditPreparedStatement} if {@code DEBUG} level logging is enabled. Otherwise, returns the provided target
   *         {@link PreparedStatement}.
   */
  public static PreparedStatement wrapIfDebugEnabled(final PreparedStatement target, final String sql) {
    return logger.isDebugEnabled() ? new AuditPreparedStatement(target, sql) : target;
  }

  private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final DateTimeFormatter timeFormat = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").appendFraction(ChronoField.MILLI_OF_SECOND, 0, 6, true).toFormatter();
  private static final DateTimeFormatter timestampFormat = new DateTimeFormatterBuilder().append(dateFormat).appendLiteral(' ').append(timeFormat).toFormatter();
  private static final ThreadLocal<DecimalFormat> numberFormat = DecimalFormatter.createDecimalFormat("###############.###############;-###############.###############");

  private final String sql;
  private ArrayList<HashMap<Object,Object>> parameterMaps;

  /**
   * Creates a new {@link AuditPreparedStatement} with the specified {@code target} to which all method calls will be delegated.
   *
   * @param target The {@link PreparedStatement} to which all method calls will be delegated.
   * @param sql A SQL statement to be sent to the database; may contain one or more '?' parameters.
   */
  public AuditPreparedStatement(final PreparedStatement target, final String sql) {
    super(target);
    this.sql = sql;
  }

  @Override
  protected boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  protected boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  protected void trace(final StatementType statementType, final String detail, final String sql) {
    if (detail != null)
      logger.trace(detail);
  }

  @Override
  protected void debug(final StatementType statementType, final String detail, final String sql, final Throwable exception) {
    if (detail != null)
      logger.debug(detail);
  }

  /**
   * Adds a parameter with the provided {@code key} and {@code value} to the parameter map maintained in this instance for the purpose
   * of audit logging.
   *
   * @param enabled Whether the addition of parameter is enabled.
   * @param key The parameter key.
   * @param value The parameter value.
   */
  protected void addParameter(final boolean enabled, final Object key, final Object value) {
    ArrayList<HashMap<Object,Object>> parameterMaps = this.parameterMaps;
    final HashMap<Object,Object> parameterMap;
    if (parameterMaps != null)
      parameterMap = parameterMaps.get(parameterMaps.size() - 1);
    else if (enabled)
      (parameterMaps = this.parameterMaps = new ArrayList<>()).add(parameterMap = new HashMap<>());
    else
      return;

    parameterMap.put(key, value);
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note</b>: For the provided {@code sql} to be added, {@link #addParameter(boolean,Object,Object)} must have been called with
   * {@code enabled=true} in order to initialize the parameter map maintained in this instance for the purpose of audit logging.
   */
  @Override
  protected void logAddBatch(final boolean enabled, final String sql) {
    if (!enabled)
      return;

    final ArrayList<HashMap<Object,Object>> parameterMaps = this.parameterMaps;
    if (parameterMaps != null)
      parameterMaps.add(new HashMap<>());
  }

  @Override
  public PreparedStatement getTarget() {
    return (PreparedStatement)super.getTarget();
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    final PreparedStatement statement = getTarget();
    int size = -1;
    long time = -1;
    Throwable exception = null;
    final boolean isDebugEnabled = isDebugEnabled();
    try {
      trace(StatementType.QUERY, log(isTraceEnabled(), "executeQuery", toString(), Integer.MIN_VALUE, null, null, null, -1), sql);

      if (isDebugEnabled)
        time = System.currentTimeMillis();

      final ResultSet resultSet = statement.executeQuery();
      if (isDebugEnabled)
        size = ResultSets.getSize(resultSet);

      return resultSet;
    }
    catch (final Throwable t) {
      exception = t;
      throw t;
    }
    finally {
      debug(StatementType.QUERY, log(isDebugEnabled, "executeQuery", toString(), Integer.MIN_VALUE, null, null, size, time), sql, exception);
    }
  }

  @Override
  public int executeUpdate() throws SQLException {
    long time = -1;
    int count = -1;
    Throwable exception = null;
    final boolean isDebugEnabled = isDebugEnabled();
    try {
      trace(StatementType.UPDATE, log(isTraceEnabled(), "executeUpdate", toString(), Integer.MIN_VALUE, null, null, null, -1), sql);

      if (isDebugEnabled)
        time = System.currentTimeMillis();

      return count = getTarget().executeUpdate();
    }
    catch (final Throwable t) {
      exception = t;
      throw t;
    }
    finally {
      debug(StatementType.UPDATE, log(isDebugEnabled, "executeUpdate", toString(), Integer.MIN_VALUE, null, null, count, time), sql, exception);
    }
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
    getTarget().setNull(parameterIndex, sqlType);
    addParameter(isDebugEnabled(), parameterIndex, NULL);
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
    getTarget().setBoolean(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) throws SQLException {
    getTarget().setByte(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setShort(final int parameterIndex, final short x) throws SQLException {
    getTarget().setShort(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setInt(final int parameterIndex, final int x) throws SQLException {
    getTarget().setInt(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setLong(final int parameterIndex, final long x) throws SQLException {
    getTarget().setLong(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) throws SQLException {
    getTarget().setFloat(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) throws SQLException {
    getTarget().setDouble(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
    getTarget().setBigDecimal(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setString(final int parameterIndex, final String x) throws SQLException {
    getTarget().setString(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
    getTarget().setBytes(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) throws SQLException {
    getTarget().setDate(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) throws SQLException {
    getTarget().setTime(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
    getTarget().setTimestamp(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x, length);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  @Deprecated// (since="1.2")
  public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setUnicodeStream(parameterIndex, x, length);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x, length);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void clearParameters() throws SQLException {
    getTarget().clearParameters();
    if (parameterMaps != null)
      parameterMaps.get(parameterMaps.size() - 1).clear();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scale) throws SQLException {
    getTarget().setObject(parameterIndex, x, targetSqlType, scale);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
    getTarget().setObject(parameterIndex, x, targetSqlType);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setObject(final int parameterIndex, final Object x) throws SQLException {
    getTarget().setObject(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public boolean execute() throws SQLException {
    long time = -1;
    boolean result = false;
    Throwable exception = null;
    final boolean isDebugEnabled = isDebugEnabled();
    try {
      trace(StatementType.MULTIPLE, log(isTraceEnabled(), "execute", toString(), Integer.MIN_VALUE, null, null, null, -1), sql);

      if (isDebugEnabled)
        time = System.currentTimeMillis();

      return result = getTarget().execute();
    }
    catch (final Throwable t) {
      exception = t;
      throw t;
    }
    finally {
      debug(StatementType.MULTIPLE, log(isDebugEnabled, "execute", toString(), Integer.MIN_VALUE, null, null, result, time), sql, exception);
    }
  }

  @Override
  public void addBatch() throws SQLException {
    logAddBatch(isDebugEnabled(), sql);
    getTarget().addBatch();
  }

  @Override
  public void setRef(final int parameterIndex, final Ref x) throws SQLException {
    getTarget().setRef(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
    getTarget().setBlob(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setClob(final int parameterIndex, final Clob x) throws SQLException {
    getTarget().setClob(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setArray(final int parameterIndex, final Array x) throws SQLException {
    getTarget().setArray(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
    getTarget().setDate(parameterIndex, x, cal);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
    getTarget().setTime(parameterIndex, x, cal);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
    getTarget().setTimestamp(parameterIndex, x, cal);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
    getTarget().setNull(parameterIndex, sqlType, typeName);
    addParameter(isDebugEnabled(), parameterIndex, NULL);
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) throws SQLException {
    getTarget().setURL(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
    getTarget().setRowId(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setNString(final int parameterIndex, final String value) throws SQLException {
    getTarget().setNString(parameterIndex, value);
    addParameter(isDebugEnabled(), parameterIndex, value);
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
    getTarget().setNCharacterStream(parameterIndex, value, length);
    addParameter(isDebugEnabled(), parameterIndex, value);
  }

  @Override
  public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
    getTarget().setNClob(parameterIndex, value);
    addParameter(isDebugEnabled(), parameterIndex, value);
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setClob(parameterIndex, reader, length);
    addParameter(isDebugEnabled(), parameterIndex, reader);
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
    getTarget().setBlob(parameterIndex, inputStream, length);
    addParameter(isDebugEnabled(), parameterIndex, inputStream);
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setNClob(parameterIndex, reader, length);
    addParameter(isDebugEnabled(), parameterIndex, reader);
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
    getTarget().setSQLXML(parameterIndex, xmlObject);
    addParameter(isDebugEnabled(), parameterIndex, xmlObject);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x, length);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x, length);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader, length);
    addParameter(isDebugEnabled(), parameterIndex, reader);
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
    getTarget().setAsciiStream(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
    getTarget().setBinaryStream(parameterIndex, x);
    addParameter(isDebugEnabled(), parameterIndex, x);
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setCharacterStream(parameterIndex, reader);
    addParameter(isDebugEnabled(), parameterIndex, reader);
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
    getTarget().setNCharacterStream(parameterIndex, value);
    addParameter(isDebugEnabled(), parameterIndex, value);
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setClob(parameterIndex, reader);
    addParameter(isDebugEnabled(), parameterIndex, reader);
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
    getTarget().setBlob(parameterIndex, inputStream);
    addParameter(isDebugEnabled(), parameterIndex, inputStream);
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
    getTarget().setNClob(parameterIndex, reader);
    addParameter(isDebugEnabled(), parameterIndex, reader);
  }

  @Override
  public void close() throws SQLException {
    if (parameterMaps != null) {
      parameterMaps.clear();
      parameterMaps = null;
    }

    super.close();
  }

  /**
   * Returns a string representation of this instance's prepared SQL statement with its parameters applied.
   *
   * @return A string representation of this instance's prepared SQL statement with its parameters applied.
   */
  @Override
  public String toString() {
    if (parameterMaps == null)
      return sql;

    final StringBuilder b = new StringBuilder();
    for (int i = 0, i$ = parameterMaps.size(); i < i$; ++i) { // [RA]
      if (i > 0)
        b.append('\n');

      b.append(toString(sql, parameterMaps.get(i)));
    }

    return b.toString();
  }
}