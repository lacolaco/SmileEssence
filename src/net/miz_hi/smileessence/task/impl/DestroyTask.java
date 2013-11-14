package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class DestroyTask extends Task<Boolean>
{

    private Account account;
    private long statusId;

    public DestroyTask(long statusId)
    {
        this(Client.getMainAccount(), statusId);
    }

    public DestroyTask(Account account, long statusId)
    {
        this.account = account;
        this.statusId = statusId;
    }

    @Override
    public Boolean call()
    {
        try
        {
            API.destroyTweet(account, statusId);
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
            Notificator.info("削除しました");
        }
        else
        {
            Notificator.alert("削除失敗しました");
        }
    }

}
