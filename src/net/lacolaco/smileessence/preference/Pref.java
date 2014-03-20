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

package net.lacolaco.smileessence.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class Pref
{

    private Context context;

    public Pref(Context context)
    {
        this.context = context;
    }

    private SharedPreferences getPref()
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String getString(int resID)
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

    public boolean getValue(int keyID, boolean defaultValue)
    {
        return getString(keyID) != null ? getPref().getBoolean(getString(keyID), defaultValue) : defaultValue;
    }

    public int getValue(int keyID, int defaultValue)
    {
        return getString(keyID) != null ? getPref().getInt(getString(keyID), defaultValue) : defaultValue;
    }

    public float getValue(int keyID, float defaultValue)
    {
        return getString(keyID) != null ? getPref().getFloat(getString(keyID), defaultValue) : defaultValue;
    }

    public long getValue(int keyID, long defaultValue)
    {
        return getString(keyID) != null ? getPref().getLong(getString(keyID), defaultValue) : defaultValue;
    }

    public String getValue(int keyID, String defaultValue)
    {
        return getString(keyID) != null ? getPref().getString(getString(keyID), defaultValue) : defaultValue;
    }

    public Set<String> getValue(int keyID, Set<String> defaultValue)
    {
        return getString(keyID) != null ? getPref().getStringSet(getString(keyID), defaultValue) : defaultValue;
    }

    public boolean putValue(int keyID, boolean value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        SharedPreferences.Editor editor = getPref().edit();
        editor.putBoolean(getString(keyID), value);
        return editor.commit();
    }

    public boolean putValue(int keyID, int value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        SharedPreferences.Editor editor = getPref().edit();
        editor.putInt(getString(keyID), value);
        return editor.commit();
    }

    public boolean putValue(int keyID, float value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        SharedPreferences.Editor editor = getPref().edit();
        editor.putFloat(getString(keyID), value);
        return editor.commit();
    }

    public boolean putValue(int keyID, long value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        SharedPreferences.Editor editor = getPref().edit();
        editor.putLong(getString(keyID), value);
        return editor.commit();
    }

    public boolean putValue(int keyID, String value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        SharedPreferences.Editor editor = getPref().edit();
        editor.putString(getString(keyID), value);
        return editor.commit();
    }

    public boolean putValue(int keyID, Set<String> value)
    {
        if(getString(keyID) == null)
        {
            return false;
        }
        SharedPreferences.Editor editor = getPref().edit();
        editor.putStringSet(getString(keyID), value);
        return editor.commit();
    }
}
