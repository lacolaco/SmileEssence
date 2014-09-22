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

import android.text.TextUtils;
import com.twitter.Validator;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.DirectMessageCache;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.ShowDirectMessageTask;
import net.lacolaco.smileessence.twitter.task.ShowStatusTask;
import net.lacolaco.smileessence.twitter.task.ShowUserTask;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Collection;

public class TwitterUtils
{

    // -------------------------- STATIC METHODS --------------------------

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
     */
    public static void tryGetStatus(Account account, long statusID, final StatusCallback callback)
    {
        Status status = StatusCache.getInstance().get(statusID);
        if(status != null)
        {
            callback.success(status);
            //update cache
            ShowStatusTask task = new ShowStatusTask(new TwitterApi(account).getTwitter(), statusID);
            task.execute();
        }
        else
        {
            ShowStatusTask task = new ShowStatusTask(new TwitterApi(account).getTwitter(), statusID)
            {
                @Override
                protected void onPostExecute(twitter4j.Status status)
                {
                    super.onPostExecute(status);
                    if(status != null)
                    {
                        callback.success(status);
                    }
                    else
                    {
                        callback.error();
                    }
                }
            };
            task.execute();
        }
    }

    /**
     * Get status from api if not cached
     */
    public static void tryGetUser(Account account, long userID, final UserCallback callback)
    {
        User user = UserCache.getInstance().get(userID);
        if(user != null)
        {
            callback.success(user);
            ShowUserTask task = new ShowUserTask(new TwitterApi(account).getTwitter(), userID);
            task.execute();
        }
        else
        {
            ShowUserTask task = new ShowUserTask(new TwitterApi(account).getTwitter(), userID)
            {
                @Override
                protected void onPostExecute(User user)
                {
                    super.onPostExecute(user);
                    if(user != null)
                    {
                        callback.success(user);
                    }
                    else
                    {
                        callback.error();
                    }

                }
            };
            task.execute();
        }
    }

    /**
     * Get direct message from api if not cached
     */
    public static void tryGetMessage(Account account, long messageID, final MessageCallback callback)
    {
        DirectMessage message = DirectMessageCache.getInstance().get(messageID);
        if(message != null)
        {
            callback.success(message);
            ShowDirectMessageTask task = new ShowDirectMessageTask(new TwitterApi(account).getTwitter(), messageID);
            task.execute();
        }
        else
        {
            ShowDirectMessageTask task = new ShowDirectMessageTask(new TwitterApi(account).getTwitter(), messageID)
            {
                @Override
                protected void onPostExecute(DirectMessage directMessage)
                {
                    super.onPostExecute(directMessage);
                    if(directMessage != null)
                    {
                        callback.success(directMessage);
                    }
                    else
                    {
                        callback.error();
                    }
                }
            };
            task.execute();
        }
    }

    /**
     * Get array of screenName in own text
     *
     * @param status            status
     * @param excludeScreenName
     * @return
     */
    public static Collection<String> getScreenNames(Status status, String excludeScreenName)
    {
        ArrayList<String> names = new ArrayList<>();
        names.add(status.getUser().getScreenName());
        if(status.getUserMentionEntities() != null)
        {
            for(UserMentionEntity entity : status.getUserMentionEntities())
            {
                if(names.contains(entity.getScreenName()))
                {
                    continue;
                }
                names.add(entity.getScreenName());
            }
        }
        if(excludeScreenName != null)
        {
            names.remove(excludeScreenName);
        }
        return names;
    }

    public static Collection<String> getScreenNames(DirectMessage message, String excludeScreenName)
    {
        ArrayList<String> names = new ArrayList<>();
        names.add(message.getSenderScreenName());
        if(!message.getRecipientScreenName().equals(message.getSenderScreenName()))
        {
            names.add(message.getRecipientScreenName());
        }
        if(message.getUserMentionEntities() != null)
        {
            for(UserMentionEntity entity : message.getUserMentionEntities())
            {
                if(names.contains(entity.getScreenName()))
                {
                    continue;
                }
                names.add(entity.getScreenName());
            }
        }
        if(excludeScreenName != null)
        {
            names.remove(excludeScreenName);
        }
        return names;
    }

    public static String getUserHomeURL(String screenName)
    {
        return String.format("https://twitter.com/%s", screenName);
    }

    public static String getAclogTimelineURL(String screenName)
    {
        return String.format("http://aclog.koba789.com/%s/timeline", screenName);
    }

    public static String getFavstarRecentURL(String screenName)
    {
        return String.format("http://favstar.fm/users/%s/recent", screenName);
    }

    public static String getTwilogURL(String screenName)
    {
        return String.format("http://twilog.org/%s", screenName);
    }

    /**
     * Get twitter status permalink
     *
     * @param status status
     * @return url string
     */
    public static String getStatusURL(Status status)
    {
        return String.format("https://twitter.com/%s/status/%s", getOriginalStatus(status).getUser().getScreenName(), getOriginalStatus(status).getId());
    }

    /**
     * Get "@screen_name: text" format text
     *
     * @param status status
     * @return summary string
     */
    public static String getStatusSummary(Status status)
    {
        return String.format("@%s: %s", status.getUser().getScreenName(), status.getText());
    }

    /**
     * Replace urls by entities
     *
     * @param text     raw text
     * @param entities url entities
     * @param expand   if true, use expanded url
     * @return replaced text
     */
    public static String replaceURLEntities(String text, URLEntity[] entities, boolean expand)
    {
        if(TextUtils.isEmpty(text))
        {
            return "";
        }
        else if(entities == null)
        {
            return text;
        }
        if(entities.length == 0)
        {
            return text;
        }
        for(URLEntity entity : entities)
        {
            text = text.replace(entity.getURL(), expand ? entity.getExpandedURL() : entity.getDisplayURL());
        }
        return text;
    }

    /**
     * Return original status text. If status is not retweet, value is same to a given.
     *
     * @param status
     * @return
     */
    public static String getOriginalStatusText(Status status)
    {
        return status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText();
    }

    public static Paging getPaging(int count)
    {
        return new Paging(1).count(count);
    }

    public static int getPagingCount(MainActivity activity)
    {
        return activity.getUserPreferenceHelper().getValue(R.string.key_setting_timelines, 20);
    }

    public static Status getOriginalStatus(Status status)
    {
        return StatusCache.getInstance().get((status.isRetweet() ? status.getRetweetedStatus() : status).getId());
    }

    public static String getMessageSummary(DirectMessage message)
    {
        return String.format("@%s: %s", message.getSender().getScreenName(), message.getText());
    }

    // -------------------------- INNER CLASSES --------------------------

    public interface StatusCallback
    {

        void success(Status status);

        void error();
    }

    public interface UserCallback
    {

        void success(User user);

        void error();
    }

    public interface MessageCallback
    {

        void success(DirectMessage message);

        void error();
    }
}
