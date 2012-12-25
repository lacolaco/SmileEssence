package net.miz_hi.warotter;

import java.io.File;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.util.EnumPreferenceKey;
import net.miz_hi.warotter.util.EnumPreferenceKey.EnumValueType;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class Warotter
{
	private static Application app;
	private static Account account;
	private static Twitter twitterInstance;
	private static TwitterStream twitterStream;
	private static SharedPreferences preference;

	private Warotter()
	{
	};

	public static void putPreferenceValue(EnumPreferenceKey key, Object value)
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

	public static <T> T getPreferenceValue(EnumPreferenceKey key)
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
			return (T) (Integer) preference.getInt(key.getKey(), 0);
		}
		else if (key.getType() == EnumValueType.FLOAT)
		{
			return (T) (Float) preference.getFloat(key.getKey(), 0.0F);
		}
		else if (key.getType() == EnumValueType.LONG)
		{
			return (T) (Long) preference.getLong(key.getKey(), 0L);
		}
		else
		{
			return null;
		}
	}

	public static Twitter getTwitter()
	{
		if (!(Boolean) getPreferenceValue(EnumPreferenceKey.AUTHORIZED))
		{
			return null;
		}
		if (twitterInstance == null)
		{
			twitterInstance = new TwitterFactory().getInstance();
			twitterInstance.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			AccessToken token = new AccessToken((String) getPreferenceValue(EnumPreferenceKey.TOKEN), (String) getPreferenceValue(EnumPreferenceKey.TOKEN_SECRET));
			twitterInstance.setOAuthAccessToken(token);
		}
		return twitterInstance;
	}

	public static TwitterStream getTwitterStream(boolean reCreate)
	{
		if (!(Boolean) getPreferenceValue(EnumPreferenceKey.AUTHORIZED))
		{
			return null;
		}
		if (twitterStream == null || reCreate)
		{
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey(CONSUMER_KEY);
			cb.setOAuthConsumerSecret(CONSUMER_SECRET);
			cb.setOAuthAccessToken((String) getPreferenceValue(EnumPreferenceKey.TOKEN));
			cb.setOAuthAccessTokenSecret((String) getPreferenceValue(EnumPreferenceKey.TOKEN_SECRET));
			cb.setUserStreamRepliesAllEnabled(false);
			twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		}
		return twitterStream;
	}

	public static Application getApplication()
	{
		return app;
	}

	public static Account getAccount()
	{
		return account;
	}

	public static void setAccount()
	{
		if ((Boolean) getPreferenceValue(EnumPreferenceKey.AUTHORIZED))
		{
			Warotter.account = new Account(Warotter.<String> getPreferenceValue(EnumPreferenceKey.SCREEN_NAME), Warotter.<Long> getPreferenceValue(EnumPreferenceKey.USER_ID));
		}
	}

	public static File getApplicationFile(String fileName)
	{
		File file = new File(app.getExternalCacheDir(), fileName);
		return file;
	}

	public static Resources getResource()
	{
		return app.getResources();
	}

	public static void initialize(Application app)
	{
		Warotter.preference = PreferenceManager.getDefaultSharedPreferences(app);
		Warotter.app = app;
		if ((Boolean) getPreferenceValue(EnumPreferenceKey.AUTHORIZED))
		{
			Warotter.account = new Account(Warotter.<String> getPreferenceValue(EnumPreferenceKey.SCREEN_NAME), Warotter.<Long> getPreferenceValue(EnumPreferenceKey.USER_ID));
		}
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
