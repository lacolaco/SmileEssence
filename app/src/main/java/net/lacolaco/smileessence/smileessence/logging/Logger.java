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

package net.lacolaco.smileessence.logging;

import android.util.Log;

public class Logger
{

    // ------------------------------ FIELDS ------------------------------

    private static final String TAG = "SmileEssence";

    // -------------------------- STATIC METHODS --------------------------

    public static void debug(Object message)
    {
        Log.d(TAG, String.valueOf(message));
    }

    public static void info(Object message)
    {
        Log.i(TAG, String.valueOf(message));
    }

    public static void error(Object message)
    {
        Log.e(TAG, String.valueOf(message));
    }

    public static void warn(Object message)
    {
        Log.w(TAG, String.valueOf(message));
    }

    public static void verbose(Object message)
    {
        Log.v(TAG, String.valueOf(message));
    }
}
