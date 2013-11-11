package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import android.content.Intent;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.view.activity.LicenseActivity;

public class CommandOpenLicense extends MenuCommand
{

    private Activity activity;

    public CommandOpenLicense(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "オープンソースライセンス";
    }

    @Override
    public void workOnUiThread()
    {
        activity.startActivity(new Intent(activity, LicenseActivity.class));
    }

}
