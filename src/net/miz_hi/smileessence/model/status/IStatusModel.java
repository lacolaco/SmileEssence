package net.miz_hi.smileessence.model.status;

import net.miz_hi.smileessence.model.status.user.UserModel;


public interface IStatusModel
{

    UserModel getUser();

    String getTextTop();

    String getTextContent();

    String getTextBottom();
}
