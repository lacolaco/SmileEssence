package net.miz_hi.smileessence.preference;

import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceHelper
{
	private SharedPreferences preference;

	public PreferenceHelper(SharedPreferences pref)
	{
		this.preference = pref;
	}

	public void putPreferenceValue(EnumPreferenceKey key, Object value)
	{
		putPreferenceValue(key.getKey(), key.getType(), value);
	}

	public <T> T getPreferenceValue(EnumPreferenceKey key)
	{
		return (T)getPreferenceValue(key.getKey(), key.getType(), key.getDefaultValue());
	}
	
	public void putPreferenceValue(String key, EnumValueType type, Object value)
	{
		Editor editor = preference.edit();
		if (type == EnumValueType.BOOLEAN)
		{
			editor.putBoolean(key, (Boolean) value);
		}
		else if (type == EnumValueType.STRING)
		{
			editor.putString(key, (String) value);
		}
		else if (type == EnumValueType.INTEGER)
		{
			editor.putInt(key, (Integer) value);
		}
		else if (type == EnumValueType.FLOAT)
		{
			editor.putFloat(key, (Float) value);
		}
		else if (type == EnumValueType.LONG)
		{
			editor.putLong(key, (Long) value);
		}

		editor.commit();
	}
	
	public <T> T getPreferenceValue(String key, EnumValueType type, T defaultValue)
	{
		if (type == EnumValueType.BOOLEAN)
		{
			return (T) (Boolean) preference.getBoolean(key, (Boolean) defaultValue);
		}
		else if (type == EnumValueType.STRING)
		{
			return (T) preference.getString(key, (String)defaultValue);
		}
		else if (type == EnumValueType.INTEGER)
		{
			return (T) (Integer) preference.getInt(key, (Integer)defaultValue);
		}
		else if (type == EnumValueType.FLOAT)
		{
			return (T) (Float) preference.getFloat(key, (Float)defaultValue);
		}
		else if (type == EnumValueType.LONG)
		{
			return (T) (Long) preference.getLong(key, (Long)defaultValue);
		}
		else
		{
			return defaultValue;
		}
	}
}
