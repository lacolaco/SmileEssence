package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

public abstract class StatusCommand extends MenuCommand
{

    protected final TweetModel status;

    public StatusCommand(TweetModel status)
    {
        if (status == null)
        {
            this.status = TweetModel.getSampleModel();
        }
        else
        {
            this.status = status;
        }
    }

}
