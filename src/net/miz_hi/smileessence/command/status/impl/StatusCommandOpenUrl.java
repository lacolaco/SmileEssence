package net.miz_hi.smileessence.command.status.impl;

import android.app.Activity;
import net.miz_hi.smileessence.command.CommandOpenUrl;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

public class StatusCommandOpenUrl extends StatusCommand
{

    private Activity activity;

    public StatusCommandOpenUrl(TweetModel status, Activity activity)
    {
        super(status);
        this.activity = activity;
    }

    @Override
    public void workOnUiThread()
    {
        new CommandOpenUrl(activity, String.format("https://twitter.com/%s/status/%s", status.getOriginal().user.screenName, status.getOriginal().statusId)).run();
    }

    @Override
    public String getName()
    {
        return "ブラウザで開く";
    }
}
