package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.UserEvent;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class FollowEvent extends UserEvent
{

    public FollowEvent(UserModel source)
    {
        super(source);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "にフォローされた";
    }

}
