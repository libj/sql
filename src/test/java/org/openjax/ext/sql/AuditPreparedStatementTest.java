/* Copyright (c) 2018 OpenJAX
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

package org.openjax.standard.sql;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.junit.Test;

public class AuditPreparedStatementTest {
  private static AuditPreparedStatement prepareStatement(final String sql, final Object ... parameters) throws SQLException {
    final AuditPreparedStatement statement = new AuditPreparedStatement(new MockPreparedStatement(), sql);
    for (int i = 0; i < parameters.length ; ++i) {
      final Object parameter = parameters[i];
      if (parameter == null)
        statement.setNull(i + 1, Types.VARCHAR);
      else if (parameter instanceof String)
        statement.setString(i + 1, (String)parameter);
      else if (parameter instanceof byte[])
        statement.setBytes(i + 1, (byte[])parameter);
      else if (parameter instanceof Date)
        statement.setDate(i + 1, (Date)parameter);
      else if (parameter instanceof Time)
        statement.setTime(i + 1, (Time)parameter);
      else if (parameter instanceof Timestamp)
        statement.setTimestamp(i + 1, (Timestamp)parameter);
      else if (parameter instanceof Boolean)
        statement.setBoolean(i + 1, (Boolean)parameter);
      else if (parameter instanceof Byte)
        statement.setByte(i + 1, (Byte)parameter);
      else if (parameter instanceof Short)
        statement.setShort(i + 1, (Short)parameter);
      else if (parameter instanceof Integer)
        statement.setInt(i + 1, (Integer)parameter);
      else if (parameter instanceof Long)
        statement.setLong(i + 1, (Long)parameter);
      else if (parameter instanceof Float)
        statement.setFloat(i + 1, (Float)parameter);
      else if (parameter instanceof Double)
        statement.setDouble(i + 1, (Double)parameter);
      else if (parameter instanceof BigDecimal)
        statement.setBigDecimal(i + 1, (BigDecimal)parameter);
      else if (parameter instanceof URL)
        statement.setURL(i + 1, (URL)parameter);
    }

    return statement;
  }

  @Test
  public void testString() throws SQLException {
    assertEquals("SELECT * FROM foo WHERE a = 'bar'", prepareStatement("SELECT * FROM foo WHERE a = ?", "bar").toString());
    assertEquals("SELECT * FROM foo WHERE a = 'b'", prepareStatement("SELECT * FROM foo WHERE a = ?", (byte)'b').toString());
  }

  @Test
  public void testNumber() throws SQLException {
    assertEquals("SELECT * FROM foo WHERE a = TRUE", prepareStatement("SELECT * FROM foo WHERE a = ?", true).toString());
    assertEquals("SELECT * FROM foo WHERE a = FALSE", prepareStatement("SELECT * FROM foo WHERE a = ?", false).toString());
    assertEquals("SELECT * FROM foo WHERE a = 0xFF", prepareStatement("SELECT * FROM foo WHERE a = ?", (byte)0xFF).toString());
    assertEquals("SELECT * FROM foo WHERE a = 7465", prepareStatement("SELECT * FROM foo WHERE a = ?", (short)7465).toString());
    assertEquals("SELECT * FROM foo WHERE a = 1", prepareStatement("SELECT * FROM foo WHERE a = ?", 1).toString());
    assertEquals("SELECT * FROM foo WHERE a = 2.5", prepareStatement("SELECT * FROM foo WHERE a = ?", 2.5f).toString());
    assertEquals("SELECT * FROM foo WHERE a = 821492734784237", prepareStatement("SELECT * FROM foo WHERE a = ?", BigDecimal.valueOf(821492734784237l)).toString());
  }

  @Test
  public void testURL() throws MalformedURLException, SQLException {
    assertEquals("SELECT * FROM foo WHERE a = 'https://www.google.co.th/search?q=ascii+table&rlz=1C5CHFA_enUS771US772&oq=acscii+ta&aqs=chrome.1.69i57j0l5.2784j1j7&sourceid=chrome&ie=UTF-8'", prepareStatement("SELECT * FROM foo WHERE a = ?", new URL("https://www.google.co.th/search?q=ascii+table&rlz=1C5CHFA_enUS771US772&oq=acscii+ta&aqs=chrome.1.69i57j0l5.2784j1j7&sourceid=chrome&ie=UTF-8")).toString());
  }

  @Test
  public void testDateTime() throws SQLException {
    assertEquals("SELECT * FROM foo WHERE a = '2018-10-28'", prepareStatement("SELECT * FROM foo WHERE a = ?", Date.valueOf("2018-10-28")).toString());
    assertEquals("SELECT * FROM foo WHERE a = '18:12:32'", prepareStatement("SELECT * FROM foo WHERE a = ?", Time.valueOf("18:12:32")).toString());
    assertEquals("SELECT * FROM foo WHERE a = '2018-10-28 18:12:32'", prepareStatement("SELECT * FROM foo WHERE a = ?", Timestamp.valueOf("2018-10-28 18:12:32")).toString());
  }

  @Test
  public void testEscapes() throws SQLException {
    assertEquals("SELECT \"He said? \"\"Boo?!\"\"\", \"And I said? \"Yikes?!\"", prepareStatement("SELECT \"He said? \"\"Boo?!\"\"\", \"And I said? \"Yikes?!\"").toString());
    assertEquals("SELECT 'I''?m asleep', 'I\'m wide awake?'", prepareStatement("SELECT 'I''?m asleep', 'I\'m wide awake?'").toString());
    assertEquals("SELECT * FROM foo WHERE a = TRUE AND b = 4 AND c = 'bar'", prepareStatement("SELECT * FROM foo WHERE a = ? AND b = ? AND c = ?", true, 4, "bar").toString());
    assertEquals("SELECT * FROM foo WHERE a = 'hell?o' AND b = TRUE AND c = \"oop?s\"", prepareStatement("SELECT * FROM foo WHERE a = 'hell?o' AND b = ? AND c = \"oop?s\"", true).toString());
    assertEquals("SELECT * FROM foo WHERE a = 'hell\''?o' AND b = TRUE AND c = \"oop'?'s\"", prepareStatement("SELECT * FROM foo WHERE a = 'hell\''?o' AND b = ? AND c = \"oop'?'s\"", true).toString());
    assertEquals("SELECT * FROM foo WHERE a = 'hell\\'?o' AND b = TRUE AND c = \"oop\\\"?'s\"", prepareStatement("SELECT * FROM foo WHERE a = 'hell\\'?o' AND b = ? AND c = \"oop\\\"?'s\"", true).toString());
  }
}