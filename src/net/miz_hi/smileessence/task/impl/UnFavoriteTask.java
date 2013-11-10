package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class UnFavoriteTask extends Task<Boolean>
{

    private Account account;
    private long statusId;

    public UnFavoriteTask(long statusId)
    {
        this(Client.getMainAccount(), statusId);
    }

    public UnFavoriteTask(Account account, long statusId)
    {
        this.account = account;
        this.statusId = statusId;
    }

    @Override
    public Boolean call()
    {
        try
        {
            API.unfavorite(account, statusId);
            return true;
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onPostExecute(Boolean result)
    {
        if (result)
        {
            Notificator.info("お気に入りを削除しました");
        }
        else
        {
            Notificator.alert("お気に入りの削除に失敗しました");
        }
    }

    @Override
    public void onPreExecute()
    {
    }

}
