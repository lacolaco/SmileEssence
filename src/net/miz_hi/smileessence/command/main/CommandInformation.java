package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.dialog.ContentWithSingleButtonDialog;


public class CommandInformation extends MenuCommand
{

    Activity activity;

    public CommandInformation(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "このアプリについて";
    }

    @Override
    public void workOnUiThread()
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_appinfo, null);
        new ContentWithSingleButtonDialog(activity, view).create().show();
    }

}
