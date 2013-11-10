package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.StatusEvent;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class ReplyEvent extends StatusEvent
{

    public ReplyEvent(UserModel user, TweetModel status)
    {
        super(user, status);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "からの返信";
    }
}
