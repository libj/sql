/* Copyright (c) 2016 LibJ
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

package org.libj.sql.exception;

import java.lang.reflect.Constructor;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLSyntaxErrorException;

import org.libj.lang.Throwables;

/**
 * A catalog of strong exception types that can be dereferenced by the
 * {@link SQLException#getSQLState()} via {@link #toStrongType(SQLException)}.
 */
public final class SQLExceptions {
  private static SQLException newInstance(final Class<? extends SQLException> cls, final String reason, final String sqlState, final int vendorCode) {
    try {
      final Constructor<? extends SQLException> constructor = cls.getConstructor(String.class, String.class, int.class);
      return constructor.newInstance(reason, sqlState, vendorCode);
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException("Unsupported SQLException class: " + cls.getName(), e);
    }
  }

  /**
   * Returns the strong exception type for the specified {@link SQLException},
   * or {@code null} if one is not registered. The specified exception's
   * {@link SQLException#getSQLState()} method is used to dereference the
   * appropriate strong exception type.
   *
   * @param exception The {@link SQLException}.
   * @return The strong exception type for the specified {@link SQLException},
   *         or {@code null} if one is not registered.
   */
  public static SQLException toStrongType(final SQLException exception) {
    final String sqlState = exception.getSQLState();
    if (sqlState == null || sqlState.length() < 2)
      return exception;

    final int category = Integer.parseInt(sqlState.substring(0, 2), 16);
    final SQLException e;
    if (category == 0x08) {
      if (exception instanceof SQLConnectionException)
        return exception;

      e = new SQLConnectionException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x0A) {
      if (exception instanceof SQLFeatureNotSupportedException)
        return exception;

      return newInstance(SQLFeatureNotSupportedException.class, exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x02) {
      if (exception instanceof SQLNoDataException)
        return exception;

      e = new SQLNoDataException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x07) {
      if (exception instanceof SQLDynamicErrorException)
        return exception;

      e = new SQLDynamicErrorException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x08) {
      if (exception instanceof SQLConnectionException)
        return exception;

      e = new SQLConnectionException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x0A) {
      if (exception instanceof SQLFeatureNotSupportedException)
        return exception;

      return newInstance(SQLFeatureNotSupportedException.class, exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x21) {
      if (exception instanceof SQLCardinalityException)
        return exception;

      e = new SQLCardinalityException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x22) {
      if (exception instanceof SQLDataException)
        return exception;

      return newInstance(SQLDataException.class, exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x23) {
      if (exception instanceof SQLIntegrityConstraintViolationException)
        return exception;

      return newInstance(SQLIntegrityConstraintViolationException.class, exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x24) {
      if (exception instanceof SQLInvalidCursorStateException)
        return exception;

      e = new SQLInvalidCursorStateException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x25) {
      if (exception instanceof SQLInvalidTransactionStateException)
        return exception;

      e = new SQLInvalidTransactionStateException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x26) {
      if (exception instanceof SQLInvalidStatementNameException)
        return exception;

      e = new SQLInvalidStatementNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x28) {
      if (exception instanceof SQLInvalidAuthorizationSpecException)
        return exception;

      return newInstance(SQLInvalidAuthorizationSpecException.class, exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x2B) {
      if (exception instanceof SQLDependentPrivilegeDescriptorsException)
        return exception;

      e = new SQLDependentPrivilegeDescriptorsException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x2C) {
      if (exception instanceof SQLInvalidCharacterSetNameException)
        return exception;

      e = new SQLInvalidCharacterSetNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x2D) {
      if (exception instanceof SQLInvalidTransactionTerminationException)
        return exception;

      e = new SQLInvalidTransactionTerminationException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x2E) {
      if (exception instanceof SQLInvalidConnectionNameException)
        return exception;

      e = new SQLInvalidConnectionNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x33) {
      if (exception instanceof SQLInvalidDescriptorNameException)
        return exception;

      e = new SQLInvalidDescriptorNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x34) {
      if (exception instanceof SQLInvalidCursorNameException)
        return exception;

      e = new SQLInvalidCursorNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x35) {
      if (exception instanceof SQLInvalidConditionNumberException)
        return exception;

      e = new SQLInvalidConditionNumberException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x3C) {
      if (exception instanceof SQLAmbiguousCursorNameException)
        return exception;

      e = new SQLAmbiguousCursorNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x3D) {
      if (exception instanceof SQLInvalidCatalogNameException)
        return exception;

      e = new SQLInvalidCatalogNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x3F) {
      if (exception instanceof SQLInvalidSchemaNameException)
        return exception;

      e = new SQLInvalidSchemaNameException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x40) {
      if (exception instanceof SQLTransactionException)
        return exception;

      e = new SQLTransactionException(exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else if (category == 0x42) {
      if (exception instanceof SQLSyntaxErrorException)
        return exception;

      return newInstance(SQLSyntaxErrorException.class, exception.getMessage(), sqlState, exception.getErrorCode());
    }
    else {
      throw new UnsupportedOperationException("Unsupported category: " + Integer.toHexString(category));
    }

    if (exception.getClass() == SQLException.class)
      return Throwables.copy(exception, e);

    e.initCause(exception);
    return e;
  }

  // Spec: http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt
  // Derby: http://web.mit.edu/course/13/13.715/jdk1.6.0_18/db/docs/html/ref/ref-single.html
  // Oracle: https://docs.oracle.com/javadb/10.8.3.0/ref/rrefexcept71493.html
  // PostgreSQL: https://www.postgresql.org/docs/9.2/errcodes-appendix.html
  // MySQL: ???
  private SQLExceptions() {
  }
}