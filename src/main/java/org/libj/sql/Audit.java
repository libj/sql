/* Copyright (c) 2023 LibJ
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

import org.slf4j.Logger;

/**
 * Base class for audit-related JDBC sub-classes.
 */
public abstract class Audit {
  protected enum StatementType {
    QUERY,
    UPDATE,
    MULTIPLE
  }

  /**
   * Returns the {@link Logger} to be used for this instance.
   *
   * @return The {@link Logger} to be used for this instance.
   */
  protected abstract Logger logger();

  /**
   * Is the logger instance enabled for the TRACE level?
   *
   * @return {@code true} if this instance is enabled for the TRACE level, otherwise {@code false}.
   */
  protected boolean isTraceEnabled() {
    final Logger logger = logger();
    return logger != null && logger.isTraceEnabled();
  }

  /**
   * Is the logger instance enabled for the DEBUG level?
   *
   * @return {@code true} if this instance is enabled for the DEBUG level, otherwise {@code false}.
   */
  protected boolean isDebugEnabled() {
    final Logger logger = logger();
    return logger != null && logger.isDebugEnabled();
  }

  /**
   * Logs the provided {@code log} entry to this instance's default logger at the TRACE level.
   *
   * @param statementType The {@link StatementType} of the {@code log} entry being logged.
   * @param log The log entry to log.
   */
  protected void trace(final StatementType statementType, final String log) {
    final Logger logger;
    if (log != null && (logger = logger()) != null)
      logger.trace(log);
  }

  /**
   * Logs the provided {@code log} entry to this instance's default logger at the DEBUG level.
   *
   * @param statementType The {@link StatementType} of the {@code log} entry being logged.
   * @param log The log entry to log.
   */
  protected void debug(final StatementType statementType, final String log) {
    final Logger logger;
    if (log != null && (logger = logger()) != null)
      logger.debug(log);
  }
}