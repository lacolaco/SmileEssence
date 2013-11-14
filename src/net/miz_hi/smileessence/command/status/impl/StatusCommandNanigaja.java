package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandNanigaja extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandNanigaja(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "な～にが○○じゃ";
    }

    @Override
    public void workOnUiThread()
    {
        String base = status.getText();
        if (base.startsWith("."))
        {
            base = base.replaceFirst(".", "");
        }
        if (base.startsWith("@" + Client.getMainAccount().getScreenName()))
        {
            base.replaceFirst(Client.getMainAccount().getScreenName(), status.getOriginal().user.screenName);
        }
        String str = "な～にが" + base.trim() + "じゃ";
        long id = status.getOriginal().statusId;
        StatusUpdate update = new StatusUpdate(str);
        update.setInReplyToStatusId(id);
        new TweetTask(update).callAsync();
        status.getOriginal().favorite();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT();
    }

}
