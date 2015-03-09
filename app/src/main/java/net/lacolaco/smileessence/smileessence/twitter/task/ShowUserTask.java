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

import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.logging.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class ShowUserTask extends TwitterTask<User>
{

    // ------------------------------ FIELDS ------------------------------

    private final long userID;
    private final String screenName;

    // --------------------------- CONSTRUCTORS ---------------------------

    public ShowUserTask(Twitter twitter, long userID)
    {
        super(twitter);
        this.userID = userID;
        this.screenName = null;
    }

    public ShowUserTask(Twitter twitter, String screenName)
    {
        super(twitter);
        this.screenName = screenName;
        this.userID = -1;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onPostExecute(User user)
    {
        if(user != null)
        {
            UserCache.getInstance().put(user);
        }
    }

    @Override
    protected User doInBackground(Void... params)
    {
        try
        {
            if(screenName != null)
            {
                return twitter.users().showUser(screenName);
            }
            else
            {
                return twitter.users().showUser(userID);
            }
        }
        catch(TwitterException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
            return null;
        }
    }
}
