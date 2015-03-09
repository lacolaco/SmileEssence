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

public class TwitterApi
{

    // ------------------------------ FIELDS ------------------------------

    public static final int MEDIA_SIZE_LIMIT = 2 * 1024 * 1024;
    private final String token;
    private final String tokenSecret;

    // -------------------------- STATIC METHODS --------------------------

    public TwitterApi(Account account)
    {
        this.token = account.accessToken;
        this.tokenSecret = account.accessSecret;
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public TwitterApi(String token, String tokenSecret)
    {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public static Twitter getTwitter(Account account)
    {
        return new TwitterApi(account).getTwitter();
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Twitter getTwitter()
    {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
        return twitter;
    }

    public TwitterStream getTwitterStream()
    {
        TwitterStream stream = new TwitterStreamFactory().getInstance();
        stream.setOAuthAccessToken(new AccessToken(token, tokenSecret));
        return stream;
    }
}
