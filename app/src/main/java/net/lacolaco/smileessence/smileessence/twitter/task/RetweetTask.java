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
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class RetweetTask extends TwitterTask<Status>
{

    // ------------------------------ FIELDS ------------------------------

    private final long statusID;
    private final Activity activity;

    // --------------------------- CONSTRUCTORS ---------------------------

    public RetweetTask(Twitter twitter, long statusID, Activity activity)
    {
        super(twitter);
        this.statusID = statusID;
        this.activity = activity;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onPostExecute(twitter4j.Status status)
    {
        if(status != null)
        {
            StatusCache.getInstance().put(status);
            new Notificator(activity, R.string.notice_retweet_succeeded).publish();
        }
        else
        {
            new Notificator(activity, R.string.notice_retweet_failed, NotificationType.ALERT).publish();
        }
    }

    @Override
    protected twitter4j.Status doInBackground(Void... params)
    {
        try
        {
            return twitter.tweets().retweetStatus(statusID);
        }
        catch(TwitterException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
            return null;
        }
    }
}
