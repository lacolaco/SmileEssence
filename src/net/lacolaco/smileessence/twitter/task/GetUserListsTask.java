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

import net.lacolaco.smileessence.data.UserListCache;
import net.lacolaco.smileessence.logging.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UserList;

import java.util.ArrayList;
import java.util.List;

public class GetUserListsTask extends TwitterTask<UserList[]>
{

    // --------------------------- CONSTRUCTORS ---------------------------

    public GetUserListsTask(Twitter twitter)
    {
        super(twitter);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onPostExecute(UserList[] lists)
    {
        for(UserList list : lists)
        {
            UserListCache.getInstance().put(list.getFullName());
        }
    }

    @Override
    protected UserList[] doInBackground(Void... params)
    {
        try
        {
            List<UserList> userLists = new ArrayList<>();

            userLists.addAll(twitter.list().getUserLists(twitter.getId()));
            return userLists.toArray(new UserList[userLists.size()]);
        }
        catch(TwitterException e)
        {
            Logger.error(e);
            return new UserList[0];
        }
    }
}
