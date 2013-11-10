package net.miz_hi.smileessence.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import net.miz_hi.smileessence.command.status.impl.StatusCommandChaseTalk;
import net.miz_hi.smileessence.command.user.UserCommandOpenInfo;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.status.TweetUtils;
import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.activity.MainActivity;

public class IntentRouter
{

    public static void onNewIntent(Intent intent)
    {
        LogHelper.d("/intent");
        Uri uri = intent.getData();
        if (uri != null)
        {
            LogHelper.d(uri.toString());
            if (isOrderToPost(uri))
            {
                String text = "";
                String url = "";
                if (uri.getQueryParameter("text") != null)
                {
                    text = uri.getQueryParameter("text").replaceAll("\\+", " ");
                }
                else if (uri.getQueryParameter("status") != null)
                {
                    text = uri.getQueryParameter("status").replaceAll("\\+", " ");
                }

                if (uri.getQueryParameter("url") != null)
                {
                    url = uri.getQueryParameter("url");
                }
                String str = text + " " + url;
                PostSystem.setText(str);
                PostSystem.openPostPage();
            }
            else if (isStatusUrl(uri))
            {
                TweetModel status = TweetUtils.getOrCreateStatusModel(getStatusId(uri.toString()));
                new StatusCommandChaseTalk(MainActivity.getInstance(), status).run();
            }
            else if (isUserUrl(uri))
            {
                String screenName;
                if (uri.getQueryParameter("screen_name") != null)
                {
                    screenName = uri.getQueryParameter("screen_name");
                }
                else
                {
                    String[] arrayOfString = uri.toString().split("/");
                    screenName = arrayOfString[arrayOfString.length - 1];
                }
                new UserCommandOpenInfo(MainActivity.getInstance(), screenName).run();
            }
        }
        else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND))
        {
            Bundle extra = intent.getExtras();
            if (extra != null)
            {
                StringBuilder builder = new StringBuilder();
                if (!TextUtils.isEmpty(extra.getCharSequence(Intent.EXTRA_SUBJECT)))
                {
                    builder.append(extra.getCharSequence(Intent.EXTRA_SUBJECT)).append(" ");
                }
                builder.append(extra.getCharSequence(Intent.EXTRA_TEXT));
                PostSystem.setText(builder.toString());
                PostSystem.openPostPage();
            }
        }
    }

    public static boolean isOrderToPost(Uri uri)
    {
        if (uri.getHost().equals("twitter.com"))
        {
            if (uri.getPath().equals("/share"))
            {
                return true;
            }
            else
            {
                String[] arr = uri.toString().split("/");
                for (String s : arr)
                {
                    if (s.startsWith("tweet") || s.startsWith("home"))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isStatusUrl(Uri uri)
    {
        if (uri.getHost().equals("twitter.com"))
        {
            String[] arr = uri.toString().split("/");
            for (String s : arr)
            {
                if (s.equals("status") || s.equals("statuses"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUserUrl(Uri uri)
    {
        if (uri.getHost().equals("twitter.com"))
        {
            if (uri.getQueryParameter("screen_name") != null)
            {
                return true;
            }

            String[] arrayOfString = uri.toString().split("/");
            if (arrayOfString.length == 4 && uri.getQuery() == null)
            {
                return true;
            }
            else if (arrayOfString.length > 4)
            {
                if (arrayOfString[3].equals("#!") && uri.getQuery() == null)
                {
                    return true;
                }
            }
        }


        return false;
    }

    public static Long getStatusId(String paramUri)
    {
        String str = "0";
        String[] arrayOfString = paramUri.split("/");
        for (int i = 0; i < arrayOfString.length; i++)
        {
            if (arrayOfString[i].startsWith("status"))
            {
                str = arrayOfString[(i + 1)];
                break;
            }
        }
        return Long.parseLong(str);
    }
}
