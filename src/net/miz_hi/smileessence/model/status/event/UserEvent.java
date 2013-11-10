package net.miz_hi.smileessence.model.status.event;

import net.miz_hi.smileessence.model.status.user.UserModel;

public abstract class UserEvent extends EventModel
{

    public UserEvent(UserModel source)
    {
        super(source);
    }

    @Override
    public String getTextContent()
    {
        return "";
    }


}
