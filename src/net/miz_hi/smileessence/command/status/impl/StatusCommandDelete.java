package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.TwitterManager;
import twitter4j.TwitterException;

public class StatusCommandDelete extends StatusCommand implements IConfirmable
{

    public StatusCommandDelete(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "削除";
    }

    @Override
    public void workOnUiThread()
    {
        new Task<Boolean>()
        {

            @Override
            public Boolean call()
            {
                try
                {
                    TwitterManager.getTwitter(Client.getMainAccount()).destroyStatus(status.parentStatusId);
                }
                catch (TwitterException e)
                {
                    e.printStackTrace();
                    return false;
                }
                return true;
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
        }.callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return status.user.isMe() || (status.retweeter != null && status.retweeter.isMe());
    }

}
