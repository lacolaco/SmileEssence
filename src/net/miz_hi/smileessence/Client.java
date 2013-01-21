package net.miz_hi.smileessence;

import java.io.File;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.PreferenceHelper;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Application;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class Client
{
	private static Application app;
	private static Account mainAccount;
	private static TwitterStream twitterStream;
	private static PreferenceHelper prefHelper;

	private Client()
	{
	}

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
		if (account != null)
		{
			putPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID, account.getUserId());
		}
		else
		{
			if (twitterStream != null)
			{
				twitterStream.shutdown();
			}
			putPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID, -1L);
		}
		mainAccount = account;
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
		Client.prefHelper = new PreferenceHelper(PreferenceManager.getDefaultSharedPreferences(app));
		Client.app = app;

		if((Integer)getPreferenceValue(EnumPreferenceKey.TEXT_SIZE) < 0)
		{
			putPreferenceValue(EnumPreferenceKey.TEXT_SIZE, 10);
		}
	}

	public static final String HOMEPAGE_URL = "http://warotter.web.fc2.com/";
	public static final String T4J_URL = "http://twitter4j.org/";
	public static final String PREF_OAUTH_NAME = "oauth_pref";
	public static final String CALLBACK_OAUTH = "oauth://smileessence";

}
