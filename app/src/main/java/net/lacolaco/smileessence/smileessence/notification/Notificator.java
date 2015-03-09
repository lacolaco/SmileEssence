/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.notification;

import android.app.Activity;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.util.UIHandler;

public class Notificator
{

    // ------------------------------ FIELDS ------------------------------

    private static final int DURATION = 1000;
    private static boolean isRunning;
    private Activity activity;
    private String text;
    private NotificationType type;

    // -------------------------- STATIC METHODS --------------------------

    public Notificator(Activity activity, int resID)
    {
        this(activity, resID, NotificationType.INFO);
    }

    public Notificator(Activity activity, String text)
    {
        this(activity, text, NotificationType.INFO);
    }

    public Notificator(Activity activity, int resID, NotificationType type)
    {
        this(activity, activity.getString(resID), type);
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

    // --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Notify self on cronton or toast.
     */
    public static void publish(Activity activity, int resID)
    {
        publish(activity, activity.getString(resID));
    }

    public static void publish(Activity activity, String text)
    {
        new Notificator(activity, text, NotificationType.INFO).publish();
    }

    public static void publish(Activity activity, int resID, NotificationType type)
    {
        publish(activity, activity.getString(resID), type);
    }

    public static void publish(Activity activity, String text, NotificationType type)
    {
        new Notificator(activity, text, type).publish();
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

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
                style.setBackgroundColorValue(Style.holoBlueLight);
                break;
            }
            case ALERT:
            {
                style.setBackgroundColorValue(Style.holoRedLight);
                break;
            }
        }
        return style.build();
    }

    // -------------------------- OTHER METHODS --------------------------

    public Crouton makeCrouton()
    {
        return Crouton.makeText(activity, text, getStyle());
    }

    public Toast makeToast()
    {
        return Toast.makeText(activity, text, Toast.LENGTH_LONG);
    }

    /**
     * Notify self on cronton or toast.
     */
    public void publish()
    {
        if(activity == null || activity.isFinishing())
        {
            return;
        }
        new UIHandler()
        {
            @Override
            public void run()
            {
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
        }.post();
    }
}
