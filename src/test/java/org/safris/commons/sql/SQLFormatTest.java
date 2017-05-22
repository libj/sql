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

package org.safris.commons.sql;

import org.junit.Test;

public class SQLFormatTest {
  @Test
  public void testFormat() {
    final String sql = "SELECT a.address1, a.address2, a.city, a.locality, a.postal_code, a.country, a.latitude, a.longitude, a.customer_number, a.company_name, a.first_name, a.last_name, a.phone, a.sales_employee_number, a.credit_limit, 7918 * ATAN2(SQRT(EXP(LN(SIN(a.latitude - 37.78536811469731 * PI() / 360)) * 2) + COS(a.latitude * PI() / 360) * EXP(LN(SIN(a.longitude - -122.3931884765625 * PI() / 360)) * 2)), SQRT(EXP(LN(1 - SIN(a.latitude - 37.78536811469731 * PI() / 360)) * 2) * COS(PI() * 37.78536811469731 / 180) * COS(a.latitude * PI() / 180) * EXP(LN(SIN(a.longitude - -122.3931884765625 * PI() / 360)) * 2))) AS c FROM customer a GROUP BY a.address1, a.address2, a.city, a.locality, a.postal_code, a.country, a.latitude, a.longitude, a.customer_number, a.company_name, a.first_name, a.last_name, a.phone, a.sales_employee_number, a.credit_limit HAVING 7918 * ATAN2(SQRT(EXP(LN(SIN(a.latitude - 37.78536811469731 * PI() / 360)) * 2) + COS(a.latitude * PI() / 360) * EXP(LN(SIN(a.longitude - -122.3931884765625 * PI() / 360)) * 2)), SQRT(EXP(LN(1 - SIN(a.latitude - 37.78536811469731 * PI() / 360)) * 2) * COS(PI() * 37.78536811469731 / 180) * COS(a.latitude * PI() / 180) * EXP(LN(SIN(a.longitude - -122.3931884765625 * PI() / 360)) * 2))) < 10 AND 7918 * ATAN2(SQRT(EXP(LN(SIN(a.latitude - 37.78536811469731 * PI() / 360)) * 2) + COS(a.latitude * PI() / 360) * EXP(LN(SIN(a.longitude - -122.3931884765625 * PI() / 360)) * 2)), SQRT(EXP(LN(1 - SIN(a.latitude - 37.78536811469731 * PI() / 360)) * 2) * COS(PI() * 37.78536811469731 / 180) * COS(a.latitude * PI() / 180) * EXP(LN(SIN(a.longitude - -122.3931884765625 * PI() / 360)) * 2))) < 10 ORDER BY c DESC FETCH FIRST 10 ROWS ONLY";
    System.out.println(SQLFormat.format(sql));
  }
}