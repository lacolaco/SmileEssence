package net.lacolaco.smileessence.twitter;

import net.lacolaco.smileessence.entity.Account;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

public class TwitterApi
{

    private Account account;

    public TwitterApi(String token, String tokenSecret)
    {
        this.account = new Account(token, tokenSecret);
    }

    public Twitter getTwitter()
    {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthAccessToken(new AccessToken(account.accessToken, account.accessSecret));
        return twitter;
    }

    public TwitterStream getTwitterStream()
    {
        TwitterStream stream = TwitterStreamFactory.getSingleton();
        stream.setOAuthAccessToken(new AccessToken(account.accessToken, account.accessSecret));
        return stream;
    }
}
