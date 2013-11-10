package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.system.PostSystem;
import twitter4j.UserMentionEntity;

public class StatusCommandReplyToAll extends StatusCommand
{

    public StatusCommandReplyToAll(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "全員に返信";
    }

    @Override
    public void workOnUiThread()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(".");
        for (UserMentionEntity entity : status.userMentions)
        {
            if (entity.getScreenName().equals(Client.getMainAccount().getScreenName()))
            {
                continue;
            }
            builder.append("@").append(entity.getScreenName()).append(" ");
        }

        PostSystem.setText(builder.toString());
        PostSystem.openPostPage();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return status.userMentions != null && status.userMentions.length > 1;
    }


}
