package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class RetweetTask extends Task<Boolean>
{

    private Account account;
    private long statusId;

    public RetweetTask(long statusId)
    {
        this(Client.getMainAccount(), statusId);
    }

    public RetweetTask(Account account, long statusId)
    {
        this.account = account;
        this.statusId = statusId;
    }

    @Override
    public Boolean call()
    {
        try
        {
            API.retweet(account, statusId);
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

    @Override
    public void onPostExecute(Boolean result)
    {
        if (result)
        {
            Notificator.info("リツイートしました");
        }
        else
        {
            Notificator.info("リツイートに失敗しました");
        }
    }

}
