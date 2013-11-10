package net.miz_hi.smileessence.command.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.MenuCommand;

public class CommandOpenFavstar extends MenuCommand
{

    private Activity activity;

    public CommandOpenFavstar(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "Favstarを開く(web)";
    }

    @Override
    public void workOnUiThread()
    {
        String url = "http://favstar.fm/users/" + Client.getMainAccount().getScreenName() + "/recent";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

}
