package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import net.miz_hi.smileessence.command.MenuCommand;


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
        /*
		 * アプリ情報のダイアログを表示
		 */
    }

}
