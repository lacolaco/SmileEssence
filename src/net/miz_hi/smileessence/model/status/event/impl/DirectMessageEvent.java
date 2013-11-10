package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.UserEvent;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class DirectMessageEvent extends UserEvent
{

    public DirectMessageEvent(UserModel source)
    {
        super(source);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "からDMを受信";
    }

}
