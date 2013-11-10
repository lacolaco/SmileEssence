package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.IAttackEvent;
import net.miz_hi.smileessence.model.status.event.StatusEvent;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class FavoriteEvent extends StatusEvent implements IAttackEvent
{

    public FavoriteEvent(UserModel source, TweetModel targetStatus)
    {
        super(source, targetStatus);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "にふぁぼられた";
    }
}
