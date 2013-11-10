package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class UserCommandBlock extends UserCommand implements IConfirmable
{

    public UserCommandBlock(String username)
    {
        super(username);
    }

    @Override
    public String getName()
    {
        return "ブロック";
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
                    API.block(Client.getMainAccount(), userName);
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
                    Notificator.info("ブロックしました");
                }
                else
                {
                    Notificator.alert("ブロック失敗しました");
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