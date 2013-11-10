package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.IAttackEvent;
import net.miz_hi.smileessence.model.status.event.StatusEvent;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class RetweetEvent extends StatusEvent implements IAttackEvent
{

    public RetweetEvent(UserModel retweeter, TweetModel status)
    {
        super(retweeter, status);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "にリツイートされた";
    }
}
