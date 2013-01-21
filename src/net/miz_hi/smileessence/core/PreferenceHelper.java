package net.miz_hi.smileessence.core;

import net.miz_hi.smileessence.core.EnumPreferenceKey.EnumValueType;
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
		Editor editor = preference.edit();
		if (key.getType() == EnumValueType.BOOLEAN)
		{
			editor.putBoolean(key.getKey(), (Boolean) value);
		}
		else if (key.getType() == EnumValueType.STRING)
		{
			editor.putString(key.getKey(), (String) value);
		}
		else if (key.getType() == EnumValueType.INTEGER)
		{
			editor.putInt(key.getKey(), (Integer) value);
		}
		else if (key.getType() == EnumValueType.FLOAT)
		{
			editor.putFloat(key.getKey(), (Float) value);
		}
		else if (key.getType() == EnumValueType.LONG)
		{
			editor.putLong(key.getKey(), (Long) value);
		}
		editor.commit();
	}

	public <T> T getPreferenceValue(EnumPreferenceKey key)
	{

		if (key.getType() == EnumValueType.BOOLEAN)
		{
			return (T) (Boolean) preference.getBoolean(key.getKey(), false);
		}
		else if (key.getType() == EnumValueType.STRING)
		{
			return (T) preference.getString(key.getKey(), "");
		}
		else if (key.getType() == EnumValueType.INTEGER)
		{
			return (T) (Integer) preference.getInt(key.getKey(), -1);
		}
		else if (key.getType() == EnumValueType.FLOAT)
		{
			return (T) (Float) preference.getFloat(key.getKey(), -1F);
		}
		else if (key.getType() == EnumValueType.LONG)
		{
			return (T) (Long) preference.getLong(key.getKey(), -1L);
		}
		else
		{
			return null;
		}
	}
}
