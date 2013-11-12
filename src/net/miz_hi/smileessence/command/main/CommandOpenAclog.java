package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.MenuCommand;

public class CommandOpenAclog extends MenuCommand
{

    private Activity activity;

    public CommandOpenAclog(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "Aclogを開く";
    }

    @Override
    public void workOnUiThread()
    {
        String url = "http://aclog.koba789.com/" + Client.getMainAccount().getScreenName() + "/timeline";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

}
