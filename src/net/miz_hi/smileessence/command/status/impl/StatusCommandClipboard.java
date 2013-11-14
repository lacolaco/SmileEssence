package net.miz_hi.smileessence.command.status.impl;

import android.content.ClipboardManager;
import android.content.Context;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.notification.Notificator;

public class StatusCommandClipboard extends StatusCommand implements IHideable
{

    public StatusCommandClipboard(TweetModel model)
    {
        super(model);
    }

    @Override
    public String getName()
    {
        return "クリップボードにコピー";
    }

    @Override
    public void workOnUiThread()
    {
        try
        {
            ClipboardManager manager = (ClipboardManager) Client.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setText(status.getText());
            Notificator.info("クリップボードにコピーしました");
        }
        catch (Exception e)
        {
            Notificator.alert("コピー失敗しました");
        }
    }

}
