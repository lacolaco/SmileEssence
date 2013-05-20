package net.miz_hi.smileessence.system;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusUtils;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.StringUtils;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.PostFragment;
import twitter4j.StatusUpdate;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class PostSystem
{
	public static PostSystem instance;
	public String text = "";
	public int cursor = 0;
	private long inReplyTo = NONE_ID;
	private String pictPath;
	public static final long NONE_ID = -1;
	
	static
	{
		instance = new PostSystem();
	}
	
	private PostSystem(){}
	
	public static String getText()
	{
		return instance.text;
	}

	public static PostSystem setText(String text)
	{
		instance.text = text;
		return instance;
	}
	
	public static PostSystem clearText()
	{
		return setText("");
	}
	
	public static PostSystem setCursor(int index)
	{
		instance.cursor = index;
		return instance;
	}
	
	public static int getCursor()
	{
		return instance.cursor;
	}
	
	public static PostSystem appendText(String str)
	{
		setText(instance.text + str);
		return instance;
	}
	
	public static PostSystem insertText(String str)
	{
		int cursor = instance.cursor;
		StringBuilder sb = new StringBuilder(getText());
		sb.insert(cursor, str);
		cursor = cursor + sb.length();
		if (cursor > sb.length())
		{
			cursor = sb.length();
		}
		setText(sb.toString());
		setCursor(cursor);
		return instance;
	}
	
	public static long getInReplyToStatusId()
	{
		return instance.inReplyTo;
	}
	
	public static PostSystem setInReplyToStatusId(long statusId)
	{
		instance.inReplyTo = statusId;
		return instance;
	}
	
	public static PostSystem setReply(String userName)
	{
		return setReply(userName, NONE_ID);
	}
	
	public static PostSystem setReply(String userName, long statusId)
	{
		setText("@" + userName + " ");
		setCursor(getText().length());
		setInReplyToStatusId(statusId);
		return instance;
	}
	
	public static PostSystem clearReply()
	{
		setInReplyToStatusId(NONE_ID);
		return instance;
	}
	
	public static PostSystem addReply(String userName)
	{
		clearReply();
		StringBuilder sb = new StringBuilder(getText());

		if((sb.indexOf("@" + userName) != -1))
		{
			return instance;
		}
		else
		{
			sb.append(String.format("@%s ", userName));
		}
		
		if(sb.charAt(0) != '.')
		{
			sb.insert(0, ".");
		}		

		setText(sb.toString());
		return instance;
	}
	
	public static PostSystem setPicturePath(String path)
	{
		instance.pictPath = path;
		return instance;
	}
	
	public static String getPicturePath()
	{
		return instance.pictPath;
	}
	
	public static PostSystem clearPicturePath()
	{
		return setPicturePath(null);
	}
	
	public static PostSystem clear()
	{
		clearText();
		clearReply();
		clearPicturePath();
		return instance;
	}
	
	public static boolean submit(String text)
	{
		if (TextUtils.isEmpty(text) && getPicturePath() == null)
		{
			Notifier.alert("‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢");
			return false;
		}
		else if(StringUtils.countTweetCharacters(text) > 140)
		{
			Notifier.alert("•¶Žš”‚ª‘½‚·‚¬‚Ü‚·");
			return false;
		}
		else
		{
			final StatusUpdate update = new StatusUpdate(text);
			if (getInReplyToStatusId() >= 0)
			{
				update.setInReplyToStatusId(getInReplyToStatusId());
			}
			if(getPicturePath() != null)
			{
				update.setMedia(new File(getPicturePath()));
			}
			new UiHandler()
			{

				@Override
				public void run()
				{
					new AsyncTweetTask(update).addToQueue();
					clear();
				}
			}.postDelayed(10);
		}
		return true;
	}
	
	public static void openPostPage()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				MainActivity.moveViewPage(MainActivity.PAGE_POST);
				PostFragment.singleton().load();
			}
		}.post();
	}
}
