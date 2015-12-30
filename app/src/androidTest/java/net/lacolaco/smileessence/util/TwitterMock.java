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

package net.lacolaco.smileessence.util;

import android.content.Context;
import android.content.res.AssetManager;

import net.lacolaco.smileessence.BuildConfig;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TwitterApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.DirectMessage;
import twitter4j.JSONException;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.User;

public class TwitterMock {

    AssetManager assets;

    public TwitterMock(Context context) {
        assets = context.getAssets();
    }

    private String getJson(String fileName) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assets.open(fileName)));
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return null;
    }

    public Status getStatusMock() throws IOException, TwitterException {
        return TwitterObjectFactory.createStatus(getJson("status.json"));
    }

    public Status getReplyMock() throws IOException, TwitterException {
        return TwitterObjectFactory.createStatus(getJson("reply.json"));
    }

    public Status getRetweetMock() throws IOException, TwitterException {
        return TwitterObjectFactory.createStatus(getJson("retweet.json"));
    }

    public User getUserMock() throws IOException, TwitterException {
        return TwitterObjectFactory.createUser(getJson("user.json"));
    }

    public DirectMessage getDirectMessageMock() throws IOException, TwitterException {
        return TwitterObjectFactory.createDirectMessage(getJson("directmessage.json"));
    }

    public String getAccessToken() throws IOException, JSONException {
        return BuildConfig.ACCESS_TOKEN;
    }

    public String getAccessTokenSecret() throws IOException, JSONException {
        return BuildConfig.ACCESS_TOKEN_SECRET;
    }

    public Twitter getTwitterMock() throws IOException, JSONException {
        return new TwitterApi(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, getAccessToken(), getAccessTokenSecret()).getTwitter();
    }

    public Account getAccount() throws IOException, TwitterException, JSONException {
        return new Account(getAccessToken(), getAccessTokenSecret(), getUserMock().getId(), getUserMock().getScreenName());
    }
}
