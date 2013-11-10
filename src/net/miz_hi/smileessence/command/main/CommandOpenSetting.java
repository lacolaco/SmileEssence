package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import android.content.Intent;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.view.activity.SettingActivity;

public class CommandOpenSetting extends MenuCommand
{

    private Activity activity;

    public CommandOpenSetting(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "設定";
    }

    @Override
    public void workOnUiThread()
    {
        activity.startActivity(new Intent(activity, SettingActivity.class));
    }

}
