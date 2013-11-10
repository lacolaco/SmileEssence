package net.miz_hi.smileessence.model.status.event.impl;

import net.miz_hi.smileessence.model.status.event.UserEvent;
import net.miz_hi.smileessence.model.status.user.UserModel;

public class BlockEvent extends UserEvent
{

    public BlockEvent(UserModel source)
    {
        super(source);
    }

    @Override
    public String getTextTop()
    {
        return source.screenName + "にブロックされた";
    }

}
