/*
 * SmileEssence Lite
 * Copyright 2014 laco0416
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License
 */

package net.lacolaco.smileessence.logging;

import android.util.Log;

public class Logger
{

    private static final String TAG = "SmileEssence";

    public static void debug(String message)
    {
        Log.d(TAG, message);
    }

    public static void info(String message)
    {
        Log.i(TAG, message);
    }

    public static void error(String message)
    {
        Log.e(TAG, message);
    }

    public static void warn(String message)
    {
        Log.w(TAG, message);
    }

    public static void verbose(String message)
    {
        Log.v(TAG, message);
    }
}
