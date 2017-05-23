/* Copyright (c) 2015 lib4j
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

import java.util.Arrays;
import java.util.StringTokenizer;

import org.lib4j.lang.Strings;

public final class SQLFormat {
  private static final String[] reserveds = new String[] {"ALL", "AND", "BY", "DISTINCT", "FROM", "GROUP", "HAVING", "JOIN", "LEFT", "ON", "OR", "ORDER", "OUTER", "SELECT", "WHERE"};

  public static String format(final String sql) {
    final String ws = " \t\n\r\f";
    final String delims = " \t\n\r\f(),";
    final StringTokenizer tokenizer = new StringTokenizer(sql, delims, true);
    int depth = 0;
    String out = "";
//    String prev = null;
    boolean lastReserved = true;
    boolean lastDelimNonWS = false;
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      final boolean delim = token.length() == 1 && delims.contains(token);
      if (delim) {
        if (")".equals(token))
          out += "\n" + Strings.padFixed("", depth * 2, true) + token;
        else if (!lastDelimNonWS)
          out += token;

        if (",".equals(token))
          out += "\n" + Strings.padFixed("", depth * 2, true);

        if (!ws.contains(token))
          lastDelimNonWS = delim;
      }
      else {
        lastDelimNonWS = false;
        final boolean reserved = Arrays.binarySearch(reserveds, token) >= 0;
        if (reserved) {
          if (!lastReserved) {
            --depth;
            out += "\n";
          }
        }
        else if (lastReserved) {
          ++depth;
          out += "\n" + Strings.padFixed("", depth * 2, true);
        }

        lastReserved = reserved;
        out += token;
      }
    }

    return out;
  }

  private SQLFormat() {
  }
}