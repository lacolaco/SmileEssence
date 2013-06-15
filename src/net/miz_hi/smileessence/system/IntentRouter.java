package net.miz_hi.smileessence.system;

import net.miz_hi.smileessence.command.status.StatusCommandChaseRelation;
import net.miz_hi.smileessence.command.user.UserCommandOpenInfo;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusUtils;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.activity.MainActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class IntentRouter
{

	public static void onNewIntent(Intent intent)
	{
		LogHelper.d("/intent");
		Uri uri = intent.getData();
		if(uri != null)
		{
			LogHelper.d(uri.toString());
			if (isOrderToPost(uri))
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
				String str = text + " " + url;
				PostSystem.setText(str).openPostPage();
			}
			else if (isStatusUrl(uri))
			{
				StatusModel status = StatusUtils.getOrCreateStatusModel(getStatusId(uri.toString()));
				new StatusCommandChaseRelation(status).run();
			}
			else if (isUserUrl(uri))
			{
				String screenName;
				if(uri.getQueryParameter("screen_name") != null)
				{
					screenName =  uri.getQueryParameter("screen_name");
				}
				else
				{
					String[] arrayOfString = uri.toString().split("/");
					screenName =  arrayOfString[arrayOfString.length -1];
				}
				new UserCommandOpenInfo(screenName, MainActivity.getInstance()).run();
			}
		}
		else if (intent.getAction().equals(Intent.ACTION_SEND))
		{
			Bundle extra = intent.getExtras();
			if(extra != null)
			{
				StringBuilder builder = new StringBuilder();
				if(!TextUtils.isEmpty(extra.getCharSequence(Intent.EXTRA_SUBJECT)))
				{
					builder.append(extra.getCharSequence(Intent.EXTRA_SUBJECT)).append(" ");
				}
				builder.append(extra.getCharSequence(Intent.EXTRA_TEXT));
				PostSystem.setText(builder.toString()).openPostPage();
			}
		}		
	}
	
	public static boolean isOrderToPost(Uri uri)
	{
		if (uri.getHost().equals("twitter.com"))
		{
			if(uri.getPath().equals("/share"))
			{
				return true;
			}
			else
			{
				String[] arr = uri.toString().split("/");
				for(int i = 0; i < arr.length; i++)
				{
					if(arr[i].startsWith("tweet") || arr[i].startsWith("home"))
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
			String[] arrayOfString = uri.toString().split("/");
			for (int i = 0; i < arrayOfString.length; i++)
			{
				if (arrayOfString[i].equals("status") || arrayOfString[i].equals("statuses"))
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

	public static Long getStatusId(String paramUri)
	{
		String str = "0";
		String[] arrayOfString = paramUri.toString().split("/");
		for (int i = 0; i < arrayOfString.length ; i++)
		{
			if (arrayOfString[i].startsWith("status"))
			{
				str = arrayOfString[(i + 1)];
				break;
			}			
		}
		return Long.valueOf(Long.parseLong(str));
	}
}
