package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public class TweetTask extends Task<Boolean>
{

    private Account account;
    private StatusUpdate status;

    public TweetTask(StatusUpdate status)
    {
        this(Client.getMainAccount(), status);
    }

    public TweetTask(Account account, StatusUpdate status)
    {
        this.account = account;
        this.status = status;
    }

    @Override
    public void onPostExecute(Boolean result)
    {
        if (result)
        {
            Notificator.info("投稿しました");
        }
        else
        {
            Notificator.alert("投稿失敗しました");
        }
    }

    @Override
    public Boolean call()
    {
        try
        {
            API.tweet(account, status);
            return true;
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onPreExecute()
    {

    }
}
