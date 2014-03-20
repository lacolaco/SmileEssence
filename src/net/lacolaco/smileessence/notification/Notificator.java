/*
 * SmileEssence Lite
 * Copyright 2014 laco0416
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License
 */

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
