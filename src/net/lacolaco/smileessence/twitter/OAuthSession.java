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

import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.twitter.task.AccessTokenTask;
import net.lacolaco.smileessence.twitter.task.RequestTokenTask;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.concurrent.ExecutionException;

public class OAuthSession
{

    // ------------------------------ FIELDS ------------------------------

    public static final String CALLBACK_OAUTH = "oauth://smileessence";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TOKEN_SECRET = "tokenSecret";
    public static final String KEY_SCREEN_NAME = "screenName";
    public static final String KEY_USER_ID = "userID";
    private static final String OAUTH_VERIFIER = "oauth_verifier";
    private RequestToken requestToken;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public String getAuthorizationURL()
    {
        Twitter twitter = new TwitterFactory().getInstance();
        RequestTokenTask task = new RequestTokenTask(twitter);
        task.execute();
        try
        {
            requestToken = task.get();
            return requestToken.getAuthorizationURL();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Logger.error(e.getMessage());
            return null;
        }
    }

    // -------------------------- OTHER METHODS --------------------------

    public AccessToken getAccessToken(String pinCode)
    {
        Twitter twitter = new TwitterFactory().getInstance();
        AccessTokenTask task = new AccessTokenTask(twitter, requestToken, pinCode);
        task.execute();
        try
        {
            return task.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
            return null;
        }
    }
}
