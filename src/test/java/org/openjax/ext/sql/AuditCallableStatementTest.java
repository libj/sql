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

package org.openjax.ext.sql;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.junit.Test;

public class AuditCallableStatementTest {
  private static AuditCallableStatement prepareCall(final String sql, final String[] names, final Object[] parameters) throws SQLException {
    final AuditCallableStatement statement = new AuditCallableStatement(new MockCallableStatement(), sql);
    for (int i = 0; i < parameters.length ; ++i) {
      final Object parameter = parameters[i];
      if (parameter == null)
        statement.setNull(names[i], Types.NULL);
      else if (parameter instanceof String)
        statement.setString(names[i], (String)parameter);
      else if (parameter instanceof byte[])
        statement.setBytes(names[i], (byte[])parameter);
      else if (parameter instanceof Date)
        statement.setDate(names[i], (Date)parameter);
      else if (parameter instanceof Time)
        statement.setTime(names[i], (Time)parameter);
      else if (parameter instanceof Timestamp)
        statement.setTimestamp(names[i], (Timestamp)parameter);
      else if (parameter instanceof Boolean)
        statement.setBoolean(names[i], (Boolean)parameter);
      else if (parameter instanceof Byte)
        statement.setByte(names[i], (Byte)parameter);
      else if (parameter instanceof Short)
        statement.setShort(names[i], (Short)parameter);
      else if (parameter instanceof Integer)
        statement.setInt(names[i], (Integer)parameter);
      else if (parameter instanceof Long)
        statement.setLong(names[i], (Long)parameter);
      else if (parameter instanceof Float)
        statement.setFloat(names[i], (Float)parameter);
      else if (parameter instanceof Double)
        statement.setDouble(names[i], (Double)parameter);
      else if (parameter instanceof BigDecimal)
        statement.setBigDecimal(names[i], (BigDecimal)parameter);
      else if (parameter instanceof URL)
        statement.setURL(names[i], (URL)parameter);
    }

    return statement;
  }

  @Test
  public void testCall() throws SQLException {
    assertEquals("{CALL foo(TRUE, 3, 4.5, 'bar', NULL)}", prepareCall("{CALL foo(:$one$, :#two#, :@three@, :_four_, :$#@_)}", new String[] {"$one$", "#two#", "@three@", "_four_", "$#@_"}, new Object[] {true, 3, 4.5, "bar", null}).toString());
  }
}