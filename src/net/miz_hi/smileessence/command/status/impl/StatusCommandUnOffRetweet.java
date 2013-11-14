package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandUnOffRetweet extends StatusCommand implements IHideable
{

    public StatusCommandUnOffRetweet(TweetModel model)
    {
        super(model);
    }

    @Override
    public String getName()
    {
        return "非公式RT";
    }

    @Override
    public void workOnUiThread()
    {
        String text = " RT @" + status.getOriginal().user.screenName + ": " + status.getText();
        PostSystem.clear(true);
        PostSystem.setText(text);
        PostSystem.getState().setCursor(0);
        PostSystem.openPostPage();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canUnOffRetweet() && !status.getOriginal().user.isProtected;
    }
}
