package net.miz_hi.smileessence.system;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.util.StringUtils;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.TweetView;
import twitter4j.StatusUpdate;
import android.app.Activity;
import android.widget.EditText;

public class TweetSystem
{
	private static TweetSystem instance = new TweetSystem();	
	
	private long inReplyTo = NONE_ID;
	private String text = ""; 
	private File pathPict;
	public static final long NONE_ID = -1;
	
	public static void start(Activity activity)
	{
		TweetMenu.init(activity);
	}
	
	public static String getText()
	{
		return instance.text;
	}
	
	public static void setText(String text)
	{
		instance.text = text;
		TweetView view = TweetView.getInstance();
		EditText edit = view.getEditTextTweet();
		edit.setText(text);
		edit.setSelection(text.length());
	}
	
	public static void setCursor(int index)
	{
		TweetView view = TweetView.getInstance();
		EditText edit = view.getEditTextTweet();
		edit.setSelection(index);
	}
	
	public static void appendText(String str)
	{
		setText(getText() + str);
	}
	
	public static void insertText(String str)
	{
		int cursor = TweetView.getInstance().getCursor();
		StringBuilder sb = new StringBuilder(getText());
		sb.insert(cursor, str);
		cursor = cursor + sb.length();
		if (cursor > sb.length())
		{
			cursor = sb.length();
		}
		setText(sb.toString());
		setCursor(cursor);
	}
	
	public static long getInReplyStatusId()
	{
		return instance.inReplyTo;
	}
	
	public static void setInReplyToStatusId(long statusId)
	{
		instance.inReplyTo = statusId;
	}
	
	public static void setReply(String userName, long statusId)
	{		
		if(statusId != NONE_ID)
		{
			StatusModel model = StatusStore.get(statusId);
			if(model != null)
			{
				TweetView.getInstance().setInReplyToStatus(model);
			}
		}
		setText("@" + userName + " ");
		setInReplyToStatusId(statusId);
	}
	
	public static void addReply(String userName)
	{
		setInReplyToStatusId(NONE_ID);
		Pattern pattern = Pattern.compile("@"+ userName);
		Matcher hasReply = pattern.matcher(getText());
		StringBuilder sb = new StringBuilder(getText());

		if(hasReply.find())
		{
			return;
		}
		else
		{
			sb.append(String.format("@%s ", userName));
		}
		
		if(!getText().startsWith("."))
		{
			sb.insert(0, ".");
		}		

		setText(sb.toString());
		TweetView.getInstance().removeReply();
	}
	
	public static void setPicturePath(File path)
	{
		instance.pathPict = path;
		TweetView.getInstance().setPictureImage(instance.pathPict);
	}
	
	public static File getPicturePath()
	{
		return instance.pathPict;
	}
	
	public static void clear()
	{
		setText("");
		TweetView.getInstance().removeReply();
		setPicturePath(null);	
		setInReplyToStatusId(NONE_ID);
	}
	
	public static void submit()
	{
		if (StringUtils.isNullOrEmpty(getText()) && getPicturePath() == null)
		{
			ToastManager.toast("‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢");
		}
		else
		{

			final StatusUpdate update = new StatusUpdate(getText());
			if (getInReplyStatusId() >= 0)
			{
				update.setInReplyToStatusId(getInReplyStatusId());
			}
			if(getPicturePath() != null)
			{
				update.setMedia(getPicturePath());
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
	}
}
