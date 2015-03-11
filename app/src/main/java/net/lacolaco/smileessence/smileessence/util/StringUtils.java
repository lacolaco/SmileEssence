/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2015 lacolaco.net
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils
{

    // -------------------------- STATIC METHODS --------------------------

    public static String dateToString(Date date)
    {
        Calendar current = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if(isSameYear(current, cal))
        {
            if(isSameDay(current, cal))
            {
                return new SimpleDateFormat("HH:mm:ss").format(date);
            }
            else
            {
                return new SimpleDateFormat("MM/dd HH:mm:ss").format(date);
            }
        }
        else
        {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
        }
    }

    private static boolean isSameDay(Calendar current, Calendar cal)
    {
        return cal.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isSameYear(Calendar current, Calendar cal)
    {
        return cal.get(Calendar.YEAR) == current.get(Calendar.YEAR);
    }
}
