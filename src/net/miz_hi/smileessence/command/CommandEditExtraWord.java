package net.miz_hi.smileessence.command;

import android.app.Activity;
import android.content.Intent;
import net.miz_hi.smileessence.view.activity.ExtraWordActivity;

public class CommandEditExtraWord extends MenuCommand
{

    private Activity activity;

    public CommandEditExtraWord(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "抽出ワードの管理";
    }

    @Override
    public void workOnUiThread()
    {
        Intent intent = new Intent(activity, ExtraWordActivity.class);
        activity.startActivity(intent);
    }

}
