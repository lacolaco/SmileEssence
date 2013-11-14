package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.system.PostSystem;

public class UserCommandIntroduce extends UserCommand implements IHideable
{

    public UserCommandIntroduce(String userName)
    {
        super(userName);
    }

    @Override
    public String getName()
    {
        return "みんなに紹介する";
    }

    @Override
    public void workOnUiThread()
    {
        String str = " (@" + userName + ")";
        PostSystem.setText(str);
        PostSystem.getState().setCursor(0);
        PostSystem.openPostPage();
    }

}
