package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class FavoriteTask extends Task<Boolean>
{

    private Account account;
    private long statusId;

    public FavoriteTask(long statusId)
    {
        this(Client.getMainAccount(), statusId);
    }

    public FavoriteTask(Account account, long statusId)
    {
        this.account = account;
        this.statusId = statusId;
    }

    @Override
    public Boolean call()
    {
        try
        {
            API.favorite(account, statusId);
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
            Notificator.info("お気に入りに追加しました");
        }
        else
        {
            Notificator.alert("お気に入りの追加に失敗しました");
        }
    }

    @Override
    public void onPreExecute()
    {
    }

}
