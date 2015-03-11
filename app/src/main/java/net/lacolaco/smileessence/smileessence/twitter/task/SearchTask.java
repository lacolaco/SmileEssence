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

package net.lacolaco.smileessence.twitter.task;

import android.content.res.Configuration;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.FavoriteCache;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class SearchTask extends TwitterTask<QueryResult>
{

    // ------------------------------ FIELDS ------------------------------

    private final MainActivity activity;
    private final Query query;

    // -------------------------- STATIC METHODS --------------------------

    public SearchTask(Twitter twitter, String queryString, MainActivity activity)
    {
        this(twitter, getBaseQuery(activity, queryString), activity);
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public SearchTask(Twitter twitter, Query query, MainActivity activity)
    {
        super(twitter);
        this.activity = activity;
        this.query = query;
    }

    public static Query getBaseQuery(MainActivity activity, String queryString)
    {
        Configuration config = activity.getResources().getConfiguration();
        Query query = new Query();
        query.setQuery(queryString);
        query.setCount(TwitterUtils.getPagingCount(activity));
        query.setResultType(Query.RECENT);
        return query;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onPostExecute(QueryResult queryResult)
    {
        if(queryResult != null)
        {
            for(twitter4j.Status status : queryResult.getTweets())
            {
                StatusCache.getInstance().put(status);
                FavoriteCache.getInstance().put(status);
            }
        }
    }

    @Override
    protected QueryResult doInBackground(Void... params)
    {
        try
        {
            return twitter.search(query);
        }
        catch(TwitterException e)
        {
            e.printStackTrace();
            Logger.debug(e);
            if(e.exceededRateLimitation())
            {
                Notificator.publish(activity, R.string.notice_error_rate_limit, NotificationType.ALERT);
            }
            else
            {
                Notificator.publish(activity, R.string.notice_error_search, NotificationType.ALERT);
            }
            return null;
        }
    }
}
