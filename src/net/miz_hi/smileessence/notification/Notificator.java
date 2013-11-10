package net.miz_hi.smileessence.notification;

import android.app.Activity;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import net.miz_hi.smileessence.model.status.event.EventModel;
import net.miz_hi.smileessence.model.status.event.IAttackEvent;
import net.miz_hi.smileessence.model.status.event.StatusEvent;
import net.miz_hi.smileessence.util.CountUpInteger;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.activity.MainActivity;
import twitter4j.User;

public class Notificator
{

    private static User enemyUser;
    private static long lastUserId = -1;
    private static long lastStatusId = -1;
    private static CountUpInteger counterSourceUser = new CountUpInteger(5);
    private static CountUpInteger counterTargetStatus = new CountUpInteger(5);

    public static void toast(final String text)
    {
        final Activity activity = MainActivity.getInstance();
        if (activity == null || activity.isFinishing())
        {
            return;
        }
        new UiHandler()
        {

            @Override
            public void run()
            {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        }.post();
    }

    public static void info(String text)
    {
        crouton(new Notice(text));
    }

    public static void alert(String text)
    {
        crouton(new Notice(text).setStyle(Style.ALERT));
    }

    private static Style getStyle(Style type)
    {
        Configuration.Builder config = new Configuration.Builder();
        config.setDuration(1000);
        Style.Builder builder = new Style.Builder();
        builder.setHeight(64).setConfiguration(config.build());
        if (type == Style.INFO)
        {
            builder.setBackgroundColorValue(Style.holoBlueLight);
        }
        else if (type == Style.ALERT)
        {
            builder.setBackgroundColorValue(Style.holoRedLight);
        }
        return builder.build();
    }

    public static void crouton(final Notice event)
    {
        final Activity activity = MainActivity.getInstance();
        if (activity == null || activity.isFinishing())
        {
            return;
        }

        new UiHandler()
        {

            @Override
            public void run()
            {
                Crouton.makeText(activity, event.getText(), getStyle(event.getStyle())).show();
            }
        }.post();
    }

    public static Notice buildEvent(final EventModel model)
    {
        if (model instanceof StatusEvent)
        {
            StatusEvent se = (StatusEvent) model;
            if (se instanceof IAttackEvent)
            {
                if (lastUserId != se.source.userId)
                {
                    counterSourceUser.reset();
                    lastUserId = se.source.userId;
                }
                else
                {
                    if (counterSourceUser.isOver())
                    {
                        return Notice.getNullEvent();
                    }

                    if (counterSourceUser.countUp())
                    {
                        return new Notice(se.source.screenName + "から攻撃を受けています");
                    }
                }

                if (lastStatusId != se.tweet.statusId)
                {
                    counterTargetStatus.reset();
                    lastStatusId = se.tweet.statusId;
                }
                else
                {
                    if (counterTargetStatus.isOver())
                    {
                        return Notice.getNullEvent();
                    }
                    if (counterTargetStatus.countUp())
                    {
                        return new Notice("あなたのツイートが攻撃を受けています");
                    }
                }
            }
        }

        return new Notice(model.getTextTop());
    }
}
