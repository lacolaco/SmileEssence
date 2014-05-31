/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.util;

import java.util.Calendar;
import java.util.Date;

public class StringUtils
{

    // -------------------------- STATIC METHODS --------------------------

    public static String dateToString(Date date)
    {
        Calendar cal = Calendar.getInstance();
        Calendar calToday = Calendar.getInstance();

        cal.setTime(date);

        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DATE);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);

        StringBuilder builder = new StringBuilder();

        if(cal.get(Calendar.YEAR) != calToday.get(Calendar.YEAR))
        {
            builder.append(y).append("/");
        }
        if(cal.get(Calendar.DAY_OF_YEAR) != calToday.get(Calendar.DAY_OF_YEAR))
        {
            builder.append(String.format("%02d", m + 1)).append("/").append(String.format("%02d", d)).append(" ");
        }
        builder.append(String.format("%02d", h)).append(":").append(String.format("%02d", min)).append(":").append(String.format("%02d", s));

        return builder.toString();
    }
}
