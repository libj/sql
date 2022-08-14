/* Copyright (c) 2021 LibJ
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

import java.sql.SQLException;

/**
 * An {@link Exception} to signal that a {@link SQLException} of a particular class and SQL State is not supported by
 * {@link SQLExceptions}.
 */
public class UnsupportedSQLException extends Exception {
  public UnsupportedSQLException(final SQLException e) {
    super("Unsupported class: " + SQLExceptions.getSqlState(e).substring(0, 2) + ", SQL State: " + SQLExceptions.getSqlState(e) + ", error code: " + e.getErrorCode());
  }
}