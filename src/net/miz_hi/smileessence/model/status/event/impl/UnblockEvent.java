package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.UserEvent;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class UnblockEvent extends UserEvent
{

    public UnblockEvent(UserModel source)
    {
        super(source);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "にブロック解除された";
    }

}
