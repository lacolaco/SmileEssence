package net.lacolaco.smileessence.notification;

import android.app.Activity;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import net.lacolaco.smileessence.logging.Logger;

public class Notificator
{

    private Activity activity;
    private String text;
    private NotificationType type;
    private static boolean isRunning;
    private static final int DURATION = 1000;

    public Notificator(Activity activity, String text)
    {
        this(activity, text, NotificationType.INFO);
    }

    public Notificator(Activity activity, String text, NotificationType type)
    {
        this.activity = activity;
        this.text = text;
        this.type = type;
    }

    public static void startNotification()
    {
        isRunning = true;
    }

    public static void stopNotification()
    {
        isRunning = false;
        Crouton.cancelAllCroutons();
    }

    public void publish()
    {
        if(activity == null || activity.isFinishing())
        {
            return;
        }
        if(isRunning)
        {
            Logger.debug(String.format("notify by crouton %s", text));
            makeCrouton().show();
        }
        else
        {
            Logger.debug(String.format("notify by toast %s", text));
            makeToast().show();
        }
    }

    private Style getStyle()
    {
        Configuration.Builder conf = new Configuration.Builder();
        conf.setDuration(DURATION);
        Style.Builder style = new Style.Builder();
        style.setConfiguration(conf.build());
        switch(type)
        {
            case INFO:
            {
                style.setBackgroundColor(Style.holoBlueLight);
                break;
            }
            case ALERT:
            {
                style.setBackgroundColor(Style.holoRedLight);
                break;
            }
        }
        return style.build();
    }

    public Crouton makeCrouton()
    {
        return Crouton.makeText(activity, text, getStyle());
    }

    public Toast makeToast()
    {
        return Toast.makeText(activity, text, Toast.LENGTH_LONG);
    }
}
