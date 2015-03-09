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

import net.lacolaco.smileessence.logging.Logger;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class ShowFriendshipTask extends TwitterTask<Relationship>
{

    // ------------------------------ FIELDS ------------------------------

    private final long userID;
    private final String screenName;

    // --------------------------- CONSTRUCTORS ---------------------------

    public ShowFriendshipTask(Twitter twitter, long userID)
    {
        super(twitter);
        this.userID = userID;
        this.screenName = null;
    }

    public ShowFriendshipTask(Twitter twitter, String screenName)
    {
        super(twitter);
        this.screenName = screenName;
        this.userID = -1;
    }

    @Override
    protected Relationship doInBackground(Void... params)
    {
        try
        {
            if(screenName != null)
            {
                return twitter.friendsFollowers().showFriendship(twitter.getScreenName(), screenName);
            }
            else
            {
                return twitter.friendsFollowers().showFriendship(twitter.getId(), userID);
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
