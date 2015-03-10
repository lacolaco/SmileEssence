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

package net.lacolaco.smileessence;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.CommandOpenUserDetail;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.adapter.PostState;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.StatusDetailDialogFragment;
import twitter4j.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntentRouter
{

    // ------------------------------ FIELDS ------------------------------

    public static final String TWITTER_HOST = "twitter.com";

    // -------------------------- STATIC METHODS --------------------------

    public static void onNewIntent(MainActivity activity, Intent intent)
    {
        Logger.debug("IntentRouter:onNewIntent");
        Uri uri = intent.getData();
        if(uri != null)
        {
            onUriIntent(activity, uri);
        }
        else if(intent.getAction() != null)
        {
            switch(intent.getAction())
            {
                case Intent.ACTION_SEND:
                {
                    if(intent.getType().equals("text/plain"))
                    {
                        Bundle extra = intent.getExtras();
                        if(extra != null)
                        {
                            String text = getText(extra);
                            openPostPage(activity, text);
                        }
                    }
                    else
                    {
                        Matcher matcher = Pattern.compile("image/.+").matcher(intent.getType());
                        if(matcher.find())
                        {
                            Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                            openPostPageWithImage(activity, imageUri);
                        }
                    }
                    break;
                }
            }
        }
    }

    private static String getText(Bundle extra)
    {
        StringBuilder builder = new StringBuilder();
        if(!TextUtils.isEmpty(extra.getCharSequence(Intent.EXTRA_SUBJECT)))
        {
            builder.append(extra.getCharSequence(Intent.EXTRA_SUBJECT)).append(" ");
        }
        builder.append(extra.getCharSequence(Intent.EXTRA_TEXT));
        return builder.toString();
    }

    private static String getText(Uri uri)
    {
        String text = "";
        String url = "";
        if(uri.getQueryParameter("text") != null)
        {
            text = uri.getQueryParameter("text").replaceAll("\\+", " ");
        }
        else if(uri.getQueryParameter("status") != null)
        {
            text = uri.getQueryParameter("status").replaceAll("\\+", " ");
        }

        if(uri.getQueryParameter("url") != null)
        {
            url = uri.getQueryParameter("url");
        }
        return text + " " + url;
    }

    private static void onUriIntent(MainActivity activity, Uri uri)
    {
        Logger.debug(uri.toString());
        if(isPostIntent(uri))
        {
            String str = getText(uri);
            openPostPage(activity, str);
        }
        else if(isStatusIntent(uri))
        {
            long id = getStatusID(uri);
            showStatusDialog(activity, id);
        }
        else if(isUserIntent(uri))
        {
            String screenName = getScreenName(uri);
            showUserDialog(activity, screenName);
        }
    }

    private static String getScreenName(Uri uri)
    {
        String screenName;
        if(uri.getQueryParameter("screen_name") != null)
        {
            screenName = uri.getQueryParameter("screen_name");
        }
        else
        {
            String[] arrayOfString = uri.toString().split("/");
            screenName = arrayOfString[arrayOfString.length - 1];
        }
        return screenName;
    }

    private static long getStatusID(Uri uri)
    {
        String str = "-1";
        String[] arrayOfString = uri.getPath().toString().split("/");
        for(int i = 0; i < arrayOfString.length; i++)
        {
            if(arrayOfString[i].startsWith("status"))
            {
                str = arrayOfString[(i + 1)];
                break;
            }
        }
        return Long.parseLong(str);
    }

    private static boolean isPostIntent(Uri uri)
    {
        if(uri.getHost().equals(TWITTER_HOST))
        {
            if(uri.getPath().equals("/share"))
            {
                return true;
            }
            else
            {
                String[] arr = uri.toString().split("/");
                for(String s : arr)
                {
                    if(s.startsWith("tweet") || s.startsWith("home"))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isStatusIntent(Uri uri)
    {
        if(uri.getHost().equals(TWITTER_HOST))
        {
            String[] arr = uri.toString().split("/");
            for(String s : arr)
            {
                if(s.equals("status") || s.equals("statuses"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isUserIntent(Uri uri)
    {
        if(uri.getHost().equals(TWITTER_HOST))
        {
            if(uri.getQueryParameter("screen_name") != null)
            {
                return true;
            }

            String[] arrayOfString = uri.toString().split("/");
            if(arrayOfString.length == 4 && uri.getQuery() == null)
            {
                return true;
            }
            else if(arrayOfString.length > 4)
            {
                if(arrayOfString[3].equals("#!") && uri.getQuery() == null)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static void showStatusDialog(final MainActivity activity, long id)
    {
        if(id != -1)
        {
            TwitterUtils.tryGetStatus(activity.getCurrentAccount(), id, new TwitterUtils.StatusCallback()
            {
                @Override
                public void success(Status status)
                {
                    StatusDetailDialogFragment fragment = new StatusDetailDialogFragment();
                    fragment.setStatusID(status.getId());
                    DialogHelper.showDialog(activity, fragment);
                }

                @Override
                public void error()
                {
                    Notificator.publish(activity, R.string.error_intent_status_cannot_load, NotificationType.ALERT);
                }
            });
        }
        else
        {
            Notificator.publish(activity, R.string.error_intent_status_cannot_load, NotificationType.ALERT);
        }
    }

    private static void showUserDialog(MainActivity activity, String screenName)
    {
        CommandOpenUserDetail openUserDetail = new CommandOpenUserDetail(activity, screenName, activity.getCurrentAccount());
        openUserDetail.execute();
    }

    private static void openPostPage(final MainActivity activity, final String str)
    {
        new UIHandler()
        {
            @Override
            public void run()
            {
                PostState.newState().beginTransaction().setText(str).commitWithOpen(activity);
            }
        }.post();
    }

    private static void openPostPageWithImage(final MainActivity activity, final Uri imageUri)
    {
        new UIHandler()
        {
            @Override
            public void run()
            {
                activity.openPostPageWithImage(imageUri);
            }
        }.post();
    }
}
