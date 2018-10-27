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

package org.fastjax.sql.exception;

import java.sql.SQLException;

public final class SQLExceptions {
  public static SQLException clone(final SQLException e, final String message) {
    return clone(e, message, e.getSQLState(), e.getErrorCode(), e.getCause(), e.getStackTrace(), e.getSuppressed(), e.getNextException());
  }

  public static SQLException clone(final SQLException e, final Throwable cause) {
    return clone(e, e.getMessage(), e.getSQLState(), e.getErrorCode(), cause, e.getStackTrace(), e.getSuppressed(), e.getNextException());
  }

  public static SQLException clone(final SQLException e, final String message, final Throwable cause) {
    return clone(e, message, e.getSQLState(), e.getErrorCode(), cause, e.getStackTrace(), e.getSuppressed(), e.getNextException());
  }

  public static SQLException clone(final SQLException e, final String message, final String sqlState, final Throwable cause) {
    return clone(e, message, sqlState, e.getErrorCode(), cause, e.getStackTrace(), e.getSuppressed(), e.getNextException());
  }

  public static SQLException clone(final SQLException e, final String message, final String sqlState, final int vendorCode, final Throwable cause) {
    return clone(e, message, sqlState, vendorCode, cause, e.getStackTrace(), e.getSuppressed(), e.getNextException());
  }

  public static SQLException clone(final SQLException e, final String message, final String sqlState, final int vendorCode, final Throwable cause, final StackTraceElement[] stackTrace, final Throwable[] suppressed, final SQLException nextException) {
    final SQLException se = new SQLException(message, sqlState, vendorCode, cause);
    se.setNextException(nextException);
    se.setStackTrace(stackTrace);
    if (suppressed != null)
      for (final Throwable throwable : suppressed)
        se.addSuppressed(throwable);

    return se;
  }

  private SQLExceptions() {
  }
}