package net.miz_hi.smileessence.twitter;

import android.app.Activity;
import android.text.TextUtils;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.util.CountUpInteger;
import net.miz_hi.smileessence.util.NetworkUtils;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterManager
{

    private static Twitter twitter;
    private static TwitterStream twitterStream;
    private static boolean isStatusUpdateLimit = false;
    private static Account lastAccount;
    private static CountUpInteger count = new CountUpInteger(5);

    private static ConfigurationBuilder generateConfig(Account account)
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(account.getConsumerKey());
        cb.setOAuthConsumerSecret(account.getConsumerSecret());
        cb.setOAuthAccessToken(account.getAccessToken());
        cb.setOAuthAccessTokenSecret(account.getAccessTokenSecret());
        cb.setUseSSL(true);
        cb.setMediaProvider("TWITTER");
        return cb;
    }

    public static Twitter getTwitter()
    {
        return getTwitter(Client.getMainAccount());
    }

    public static Twitter getTwitter(Account account)
    {
        if (lastAccount == null || !account.equals(lastAccount) || twitter == null)
        {
            twitter = new TwitterFactory(generateConfig(account).build()).getInstance();
            lastAccount = account;
        }
        return twitter;
    }

    public static TwitterStream getTwitterStream(Account account)
    {
        if (twitterStream == null)
        {
            ConfigurationBuilder cb = generateConfig(account);
            cb.setUserStreamRepliesAllEnabled(false);
            MyUserStreamListener usListener = new MyUserStreamListener();
            twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            twitterStream.addListener(usListener);
            twitterStream.addConnectionLifeCycleListener(usListener);
        }
        return twitterStream;
    }

    public static boolean openTwitterStream(Activity activity)
    {
        if (NetworkUtils.cannotConnect(activity))
        {
            return false;
        }
        if (twitterStream != null)
        {
            twitterStream.shutdown();
        }
        else
        {
            twitterStream = getTwitterStream(Client.getMainAccount());
        }
        twitterStream.user();
        return true;
    }

    public static void closeTwitterStream()
    {
        if (twitterStream != null)
        {
            twitterStream.shutdown();
            twitterStream = null;
        }
    }

    public static boolean isOauthed(Account account)
    {
        return !TextUtils.isEmpty(account.getAccessToken());
    }

}
