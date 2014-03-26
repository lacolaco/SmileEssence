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

import android.os.AsyncTask;
import net.lacolaco.smileessence.logging.Logger;
import twitter4j.*;

import java.util.Collections;

public class HomeTimelineTask extends AsyncTask<Void, Void, Status[]>
{

    private final Twitter twitter;
    private final Paging paging;

    public HomeTimelineTask(Twitter twitter)
    {
        this(twitter, null);
    }

    public HomeTimelineTask(Twitter twitter, Paging paging)
    {
        this.twitter = twitter;
        this.paging = paging;
    }

    @Override
    protected twitter4j.Status[] doInBackground(Void... params)
    {
        ResponseList<twitter4j.Status> responseList;
        try
        {
            if(paging == null)
            {
                responseList = twitter.timelines().getHomeTimeline();
            }
            else
            {
                responseList = twitter.timelines().getHomeTimeline(paging);
            }
        }
        catch(TwitterException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
            return null;
        }

        Collections.reverse(responseList);
        return responseList.toArray(new twitter4j.Status[0]);
    }
}
