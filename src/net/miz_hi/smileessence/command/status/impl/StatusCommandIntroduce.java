package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandIntroduce extends StatusCommand implements IHideable
{

    public StatusCommandIntroduce(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "みんなに紹介する";
    }

    @Override
    public void workOnUiThread()
    {
        String str = status.user.name + " ( @" + status.user.screenName + " )";
        PostSystem.setText(str);
        PostSystem.getState().setCursor(0);
        PostSystem.openPostPage();
    }

}
