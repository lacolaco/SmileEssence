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

import android.app.Activity;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.data.FavoriteCache;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import twitter4j.*;

public class MentionsTimelineTask extends TwitterTask<Status[]>
{

    // ------------------------------ FIELDS ------------------------------

    private final Activity activity;
    private final Paging paging;

    // --------------------------- CONSTRUCTORS ---------------------------

    protected MentionsTimelineTask(Twitter twitter, Activity activity)
    {
        this(twitter, activity, null);
    }

    public MentionsTimelineTask(Twitter twitter, Activity activity, Paging paging)
    {
        super(twitter);
        this.activity = activity;
        this.paging = paging;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onPostExecute(twitter4j.Status[] statuses)
    {
        if(statuses.length != 0)
        {
            for(twitter4j.Status status : statuses)
            {
                StatusCache.getInstance().put(status);
                FavoriteCache.getInstance().put(status);
            }
        }
    }

    @Override
    protected twitter4j.Status[] doInBackground(Void... params)
    {
        ResponseList<twitter4j.Status> responseList;
        try
        {
            if(paging == null)
            {
                responseList = twitter.timelines().getMentionsTimeline();
            }
            else
            {
                responseList = twitter.timelines().getMentionsTimeline(paging);
            }
        }
        catch(TwitterException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
            if(e.exceededRateLimitation())
            {
                Notificator.publish(activity, R.string.notice_error_rate_limit, NotificationType.ALERT);
            }
            else
            {
                Notificator.publish(activity, R.string.notice_error_get_mentions, NotificationType.ALERT);
            }
            return new twitter4j.Status[0];
        }
        return responseList.toArray(new twitter4j.Status[responseList.size()]);
    }
}
