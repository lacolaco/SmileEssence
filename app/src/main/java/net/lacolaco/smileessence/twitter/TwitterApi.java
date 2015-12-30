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

package net.lacolaco.smileessence.twitter;

import net.lacolaco.smileessence.entity.Account;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApi {

    // ------------------------------ FIELDS ------------------------------

    public static final int MEDIA_SIZE_LIMIT = 2 * 1024 * 1024;
    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String tokenSecret;

    // -------------------------- STATIC METHODS --------------------------

    public TwitterApi(Consumer consumer, Account account) {
        this.consumerKey = consumer.key;
        this.consumerSecret = consumer.secret;
        this.token = account.accessToken;
        this.tokenSecret = account.accessSecret;
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public TwitterApi(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public static Configuration getConf(String consumerKey, String consumerSecret) {
        ConfigurationBuilder conf = new ConfigurationBuilder();
        conf.setOAuthConsumerKey(consumerKey);
        conf.setOAuthConsumerSecret(consumerSecret);
        conf.setDebugEnabled(true);
        return conf.build();
    }

    public static Twitter getTwitter(Consumer consumer, Account account) {
        return new TwitterApi(consumer, account).getTwitter();
    }

    public Twitter getTwitter() {
        Twitter twitter = new TwitterFactory(getConf(consumerKey, consumerSecret)).getInstance();
        twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
        return twitter;
    }

    public TwitterStream getTwitterStream() {
        TwitterStream stream = new TwitterStreamFactory(getConf(consumerKey, consumerSecret)).getInstance();
        stream.setOAuthAccessToken(new AccessToken(token, tokenSecret));
        return stream;
    }
}
