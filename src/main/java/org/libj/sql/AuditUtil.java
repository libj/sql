/* Copyright (c) 2020 LibJ
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

import java.sql.Connection;

import org.libj.lang.ObjectUtil;

class AuditUtil {
  static StringBuilder log(final Object self, final String method, final Connection connection, final String sql) {
    return new StringBuilder("[").append(ObjectUtil.identityString(self)).append("].").append(method).append('(').append(ObjectUtil.simpleIdentityString(connection)).append(",\n ").append(sql);
  }

  static StringBuilder withResult(final StringBuilder b, final Object result, final long time) {
    b.append(" -> ").append(result);
    if (time != -1)
      b.append("\t\t").append(System.currentTimeMillis() - time).append("ms");

    return b;
  }
}