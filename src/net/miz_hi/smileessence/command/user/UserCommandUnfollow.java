package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class UserCommandUnfollow extends UserCommand implements IConfirmable
{

    public UserCommandUnfollow(String userName)
    {
        super(userName);
    }

    @Override
    public String getName()
    {
        return "リムーヴする";
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
                    API.unfollow(Client.getMainAccount(), userName);
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
                    Notificator.info("リムーヴしました");
                }
                else
                {
                    Notificator.alert("リムーヴ失敗しました");
                }
            }
        }.callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return !Client.getMainAccount().getScreenName().equals(userName);
    }

}