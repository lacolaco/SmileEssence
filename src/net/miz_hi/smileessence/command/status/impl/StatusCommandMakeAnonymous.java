package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandMakeAnonymous extends StatusCommand implements IHideable
{

    public StatusCommandMakeAnonymous(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "匿名にする";
    }

    @Override
    public void workOnUiThread()
    {
        String str = "？？？「" + status.getText() + "」";
        PostSystem.setText(str);
        PostSystem.openPostPage();
    }

}
