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

import java.util.Set;

public class SharedPreferenceHelper
{

    // ------------------------------ FIELDS ------------------------------

    protected final Context context;
    protected final String fileName;

    // --------------------------- CONSTRUCTORS ---------------------------

    public SharedPreferenceHelper(Context context, String fileName)
    {
        this.context = context;
        this.fileName = fileName;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    protected SharedPreferences getPref()
    {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    // -------------------------- OTHER METHODS --------------------------

    public boolean getValue(String key, boolean defaultValue)
    {
        return getPref().getBoolean(key, defaultValue);
    }

    public int getValue(String key, int defaultValue)
    {
        return Integer.valueOf(getPref().getString(key, String.valueOf(defaultValue)));
    }

    public float getValue(String key, float defaultValue)
    {
        return Float.valueOf(getPref().getString(key, String.valueOf(defaultValue)));
    }

    public long getValue(String key, long defaultValue)
    {
        return Long.valueOf(getPref().getString(key, String.valueOf(defaultValue)));
    }

    public String getValue(String key, String defaultValue)
    {
        return getPref().getString(key, defaultValue);
    }

    public Set<String> getValue(String key, Set<String> defaultValue)
    {
        return getPref().getStringSet(key, defaultValue);
    }

    public boolean putValue(String key, boolean value)
    {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, int value)
    {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }

    public boolean putValue(String key, float value)
    {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }

    public boolean putValue(String key, long value)
    {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }

    public boolean putValue(String key, String value)
    {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, Set<String> value)
    {
        SharedPreferences.Editor editor = getPref().edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }
}
