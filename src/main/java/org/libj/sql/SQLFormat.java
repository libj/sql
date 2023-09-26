/* Copyright (c) 2015 LibJ
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

import static org.libj.lang.Strings.Align.*;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.libj.lang.Strings;

/**
 * Utility for formatting SQL.
 *
 * @implNote This class is a work in progress!
 */
public final class SQLFormat {
  private static final String[] reserveds = {"ALL", "AND", "BY", "DISTINCT", "FROM", "GROUP", "HAVING", "JOIN", "LEFT", "ON", "OR", "ORDER", "OUTER", "SELECT", "WHERE"};

  /**
   * Format the specified SQL (i.e. "pretty print").
   *
   * @param sql The SQL string.
   * @return The formatted SQL string.
   */
  public static String format(final String sql) {
    final String ws = " \t\n\r\f";
    final String delims = " \t\n\r\f(),";
    final StringTokenizer tokenizer = new StringTokenizer(sql, delims, true);
    int depth = 0;
    final StringBuilder out = new StringBuilder();
    // String prev = null;
    boolean lastReserved = true;
    boolean lastDelimNonWS = false;
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      final boolean delim = token.length() == 1 && delims.contains(token);
      if (delim) {
        if (")".equals(token))
          out.append('\n').append(Strings.pad("", RIGHT, depth * 2)).append(token);
        else if (!lastDelimNonWS)
          out.append(token);

        if (",".equals(token))
          out.append('\n').append(Strings.pad("", RIGHT, depth * 2));

        if (!ws.contains(token))
          lastDelimNonWS = delim;
      }
      else {
        lastDelimNonWS = false;
        final boolean reserved = Arrays.binarySearch(reserveds, token) >= 0;
        if (reserved) {
          if (!lastReserved) {
            --depth;
            out.append('\n');
          }
        }
        else if (lastReserved) {
          ++depth;
          out.append('\n').append(Strings.pad("", RIGHT, depth * 2));
        }

        lastReserved = reserved;
        out.append(token);
      }
    }

    return out.toString();
  }

  private SQLFormat() {
  }
}