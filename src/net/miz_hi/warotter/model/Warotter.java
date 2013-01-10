package net.miz_hi.warotter.model;

import java.io.File;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import net.miz_hi.warotter.core.PreferenceHelper;
import net.miz_hi.warotter.core.ThemeHelper;
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
	private static Account mainAccount;
	private static TwitterStream twitterStream;
	private static PreferenceHelper prefHelper;

	private Warotter() {}

	public static void putPreferenceValue(EnumPreferenceKey key, Object value)
	{
		prefHelper.putPreferenceValue(key, value);
	}

	public static <T> T getPreferenceValue(EnumPreferenceKey key)
	{
		return prefHelper.getPreferenceValue(key);
	}
	
	private static ConfigurationBuilder generateConfig(Account account)
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(account.getConsumerKey());
		cb.setOAuthConsumerSecret(account.getConsumerSecret());
		cb.setOAuthAccessToken(account.getAccessToken());
		cb.setOAuthAccessTokenSecret(account.getAccessTokenSecret());
		return cb;
	}

	public static Twitter getTwitter(Account account)
	{
		return new TwitterFactory(generateConfig(account).build()).getInstance();
	}

	public static TwitterStream getTwitterStream(Account account, boolean reCreate)
	{
		if (twitterStream == null || reCreate)
		{
			ConfigurationBuilder cb = generateConfig(account);
			cb.setUserStreamRepliesAllEnabled(false);
			twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		}
		return twitterStream;
	}

	public static Application getApplication()
	{
		return app;
	}

	public static Account getMainAccount()
	{
		return mainAccount;
	}

	public static void setMainAccount(Account account)
	{
		mainAccount = account;
		if(mainAccount != null)
		{
			putPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID, account.getUserId());
		}
		else
		{
			if(twitterStream != null)
			{
				twitterStream.shutdown();
			}
			putPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID, -1L);
		}
	}
	
	public static int getTheme()
	{
		int theme = getPreferenceValue(EnumPreferenceKey.THEME);
		return theme > 0 ? theme : ThemeHelper.WHITE;
	}
	
	public static void setTheme(int theme)
	{
		putPreferenceValue(EnumPreferenceKey.THEME, theme);
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
		Warotter.prefHelper = new PreferenceHelper(PreferenceManager.getDefaultSharedPreferences(app));
		Warotter.app = app;
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
