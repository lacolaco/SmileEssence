package net.miz_hi.warotter;

import twitter4j.Twitter;
import net.miz_hi.warotter.util.EnumPreferenceKey;
import net.miz_hi.warotter.util.EnumPreferenceKey.EnumValueType;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Warotter
{
	private static Warotter instance;
	private static Twitter twitterInstance;
	private SharedPreferences preference;
	
	private Warotter(){};
	
	public static Warotter getWarotter()
	{
		return instance;
	}
	
	public void putPreferenceValue(EnumPreferenceKey key, Object value)
	{
		Editor editor = preference.edit();
		if(key.getType() == EnumValueType.BOOLEAN)
		{
			editor.putBoolean(key.getKey(), (Boolean)value);
		}
		else if(key.getType() == EnumValueType.STRING)
		{
			editor.putString(key.getKey(), (String)value);
		}
		else if(key.getType() == EnumValueType.INTEGER)
		{
			editor.putInt(key.getKey(), (Integer)value);
		}
		else if(key.getType() == EnumValueType.FLOAT)
		{
			editor.putFloat(key.getKey(), (Float)value);
		}
		else if(key.getType() == EnumValueType.LONG)
		{
			editor.putLong(key.getKey(), (Long)value);
		}
		editor.commit();
	}
	
	public Object getPreferenceValue(EnumPreferenceKey key)
	{
		if(preference.contains(key.getKey()))
		{
			if(key.getType() == EnumValueType.BOOLEAN)
			{
				return preference.getBoolean(key.getKey(), false);
			}
			else if(key.getType() == EnumValueType.STRING)
			{
				return preference.getString(key.getKey(), "");
			}
			else if(key.getType() == EnumValueType.INTEGER)
			{
				return preference.getInt(key.getKey(), 0);
			}
			else if(key.getType() == EnumValueType.FLOAT)
			{
				return preference.getFloat(key.getKey(), 0.0F);
			}
			else if(key.getType() == EnumValueType.LONG)
			{
				return preference.getLong(key.getKey(), 0L);
			}
		}
		return null;
	}

	public static void initialize(SharedPreferences pref)
	{
		instance = new Warotter();
		instance.preference = pref;
	}
	
	public static final String HOMEPAGE_URL = "http://warotter.web.fc2.com/";
	public static final String T4J_URL = "http://twitter4j.org/";
	public static final String PREF_OAUTH_NAME = "oauth_pref";
	public static final String CALLBACK_OAUTH = "oauth://warotter";
	public static final String PREF_KEY_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TOKEN = "oauth_token";
	public static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final String CONSUMER_KEY = "7BPBIfjUFYpFCByyg";
	public static final String CONSUMER_SECRET = "wE7qedFk2P2dhi1ykPI9lR5aemlFzueLI1XFzmHcaE";
	
}
