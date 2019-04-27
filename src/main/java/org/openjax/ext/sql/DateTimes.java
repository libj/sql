/* Copyright (c) 2017 OpenJAX
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

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import org.openjax.standard.util.Dates;

/**
 * Utility functions for operations pertaining to {@link Date} and {@link Time}.
 */
public final class DateTimes {
  /**
   * Returns a {@link Time} representation of the specified {@link LocalTime}.
   *
   * @param localTime The {@link LocalTime}.
   * @return A {@link Time} representation of the specified {@link LocalTime}.
   */
  public static Time toTime(final LocalTime localTime) {
    final Time time = Time.valueOf(localTime);
    time.setTime(time.getTime() + localTime.get(ChronoField.MILLI_OF_SECOND));
    return time;
  }

  /**
   * Returns a {@link LocalTime} representation of the specified {@link Time}.
   *
   * @param time The {@link Time}.
   * @return A {@link LocalTime} representation of the specified {@link Time}.
   */
  public static LocalTime toLocalTime(final Time time) {
    final LocalTime localTime = time.toLocalTime();
    localTime.plus(Dates.getMilliOfSecond(time), ChronoUnit.MILLIS);
    return localTime;
  }

  private DateTimes() {
  }
}