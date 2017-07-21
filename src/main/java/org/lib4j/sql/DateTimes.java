/* Copyright (c) 2017 lib4j
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

import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import org.lib4j.util.Dates;

public final class DateTimes {
  public static class XTime extends Time {
    private static final long serialVersionUID = -5429219222847694085L;

    @SuppressWarnings("deprecation")
    public XTime(final int hour, final int minute, final int second) {
      super(hour, minute, second);
    }

    public XTime(long time) {
      super(time);
    }

    @Override
    public LocalTime toLocalTime() {
      return DateTimes.toLocalTime(this);
    }
  }

  public static Time toTime(final LocalTime localTime) {
    final Time time = Time.valueOf(localTime);
//    System.err.println(time + " " + localTime.get(ChronoField.MILLI_OF_SECOND));
    time.setTime(time.getTime() + localTime.get(ChronoField.MILLI_OF_SECOND));
//    System.err.println(time);
    return time;
  }

  public static LocalTime toLocalTime(final Time time) {
    final LocalTime localTime = time.toLocalTime();
    localTime.plus(Dates.getMilliOfSecond(time), ChronoUnit.MILLIS);
    return localTime;
  }

  private DateTimes() {
  }
}