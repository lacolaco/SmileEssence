package net.lacolaco.smileessence.command.status;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;

import twitter4j.Status;

public class StatusCommandCopyURLToClipboard extends StatusCommand
{

    // --------------------------- CONSTRUCTORS ---------------------------

    public StatusCommandCopyURLToClipboard(Activity activity, Status status)
    {
        super(R.id.key_command_status_copy_url_to_clipboard, activity, status);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getText()
    {
        return getActivity().getString(R.string.command_status_copy_url_to_clipboard);
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    // -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean execute()
    {
        String statusURL = TwitterUtils.getStatusURL(getOriginalStatus());
        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("tweet url", statusURL));
        Notificator.publish(getActivity(), R.string.notice_copy_clipboard);
        return true;
    }
}
