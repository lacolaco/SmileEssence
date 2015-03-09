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

package net.lacolaco.smileessence.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class UserPreferenceHelper extends SharedPreferenceHelper
{

    // ------------------------------ FIELDS ------------------------------

    public static final int TEXT_SIZE_MIN = 8;
    public static final int TEXT_SIZE_MAX = 24;
    public static final int TIMELINES_MIN = 1;
    public static final int TIMELINES_MAX = 200;

    // --------------------------- CONSTRUCTORS ---------------------------

    public UserPreferenceHelper(Context context)
    {
        super(context, null);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    protected SharedPreferences getPref()
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // -------------------------- OTHER METHODS --------------------------

    public boolean getValue(int keyID, boolean defaultValue)
    {
        return getString(keyID) != null ? super.getValue(getString(keyID), defaultValue) : defaultValue;
    }

    public int getValue(int keyID, int defaultValue)
    {
        return getString(keyID) != null ? super.getValue(getString(keyID), defaultValue) : defaultValue;
    }

    public float getValue(int keyID, float defaultValue)
    {
        return getString(keyID) != null ? super.getValue(getString(keyID), defaultValue) : defaultValue;
    }

    public long getValue(int keyID, long defaultValue)
    {
        return getString(keyID) != null ? super.getValue(getString(keyID), defaultValue) : defaultValue;
    }

    public String getValue(int keyID, String defaultValue)
    {
        return getString(keyID) != null ? super.getValue(getString(keyID), defaultValue) : defaultValue;
    }

    public Set<String> getValue(int keyID, Set<String> defaultValue)
    {
        return getString(keyID) != null ? super.getValue(getString(keyID), defaultValue) : defaultValue;
    }

    public boolean putValue(int keyID, boolean value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        return super.putValue(getString(keyID), value);
    }

    public boolean putValue(int keyID, int value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        return super.putValue(getString(keyID), value);
    }

    public boolean putValue(int keyID, float value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        return super.putValue(getString(keyID), value);
    }

    public boolean putValue(int keyID, long value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        return super.putValue(getString(keyID), value);
    }

    public boolean putValue(int keyID, String value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        return super.putValue(getString(keyID), value);
    }

    public boolean putValue(int keyID, Set<String> value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        return super.putValue(getString(keyID), value);
    }

    protected String getString(int resID)
    {
        try
        {
            return context.getString(resID);
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
