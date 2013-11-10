package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;
import twitter4j.User;


public class GetUserTask extends Task<User>
{

    long userId = -1;
    String screenName = null;

    public GetUserTask(long userId)
    {
        this.userId = userId;
    }

    public GetUserTask(String screenName)
    {
        this.screenName = screenName;
    }

    @Override
    public User call()
    {
        if (userId < 0 && screenName == null)
        {
            return null;
        }
        try
        {
            if (screenName == null)
            {
                return API.getUser(Client.getMainAccount(), userId);
            }
            else
            {
                return API.getUser(Client.getMainAccount(), screenName);
            }

        }
        catch (TwitterException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onPreExecute()
    {
    }

    @Override
    public void onPostExecute(User result)
    {
    }

}
