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

package net.lacolaco.smileessence.twitter.util;

import com.twitter.Validator;
import net.lacolaco.smileessence.data.DirectMessageCache;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.ShowDirectMessageTask;
import net.lacolaco.smileessence.twitter.task.ShowStatusTask;
import net.lacolaco.smileessence.twitter.task.ShowUserTask;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;

import java.util.concurrent.ExecutionException;

public class TwitterUtils
{

    /**
     * Get twitter style fixed text length
     *
     * @return length
     */
    public static int getFixedTextLength(String str)
    {
        Validator validator = new Validator();
        return validator.getTweetLength(str);
    }

    /**
     * Get status from api if not cached
     *
     * @return null if api error
     */
    public static Status tryGetStatus(Account account, long statusID)
    {
        Status status = StatusCache.getInstance().get(statusID);
        if(status != null)
        {
            return status;
        }
        ShowStatusTask task = new ShowStatusTask(new TwitterApi(account).getTwitter(), statusID);
        task.execute();
        try
        {
            status = task.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
        }
        return status;
    }

    /**
     * Get status from api if not cached
     *
     * @return null if api error
     */
    public static User tryGetUser(Account account, long userID)
    {
        User user = UserCache.getInstance().get(userID);
        if(user != null)
        {
            return user;
        }
        ShowUserTask task = new ShowUserTask(new TwitterApi(account).getTwitter(), userID);
        task.execute();
        try
        {
            user = task.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
        }
        return user;
    }

    /**
     * Get direct message from api if not cached
     *
     * @return null if api error
     */
    public static DirectMessage tryGetMessage(Account account, long messageID)
    {
        DirectMessage message = DirectMessageCache.getInstance().get(messageID);
        if(message != null)
        {
            return message;
        }
        ShowDirectMessageTask task = new ShowDirectMessageTask(new TwitterApi(account).getTwitter(), messageID);
        task.execute();
        try
        {
            message = task.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            Logger.error(e.getMessage());
        }
        return message;
    }
}
