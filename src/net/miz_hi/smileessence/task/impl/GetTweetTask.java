package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.Status;
import twitter4j.TwitterException;


public class GetTweetTask extends Task<Status>
{

    long statusId;

    public GetTweetTask(long statusId)
    {
        this.statusId = statusId;
    }

    @Override
    public Status call()
    {
        try
        {
            return API.getStatus(Client.getMainAccount(), statusId);
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
    public void onPostExecute(Status result)
    {
    }

}
