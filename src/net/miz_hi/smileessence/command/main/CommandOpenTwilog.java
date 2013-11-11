package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.MenuCommand;

public class CommandOpenTwilog extends MenuCommand
{

    private Activity activity;

    public CommandOpenTwilog(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "Twilogを開く(web)";
    }

    @Override
    public void workOnUiThread()
    {
        String url = "http://twilog.org/" + Client.getMainAccount().getScreenName();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

}
