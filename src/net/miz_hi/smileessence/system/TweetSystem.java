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
	
	public static TweetSystem getInstance()
	{
		return instance;
	}
	
	public void setText(String text)
	{
		this.text = text;
		TweetView.getInstance().setText(text);
	}
	
	public void appendText(String str)
	{
		text = text + str;
		TweetView.getInstance().setText(text);
	}
	
	public void insertText(String str)
	{
		int cursor = TweetView.getInstance().getCursor();
		StringBuilder sb = new StringBuilder(text);
		sb.insert(cursor, str);
		text = sb.toString();
		cursor = cursor + sb.length();
		if (cursor > text.length())
		{
			cursor = text.length();
		}
		TweetView.getInstance().setText(text);
		TweetView.getInstance().setCursor(cursor);
	}
	
	public String getText()
	{
		return text;
	}
	
	public long getInReplyStatusId()
	{
		return inReplyTo;
	}
	
	public void setInReplyToStatusId(long statusId)
	{
		inReplyTo = statusId;
	}
	
	public void setReply(String userName, long statusId)
	{
		inReplyTo = statusId;
		text = "@" + userName + " ";
		
		if(inReplyTo != NONE_ID)
		{
			StatusModel model = StatusStore.get(TweetSystem.getInstance().getInReplyStatusId());
			if(model != null)
			{
				TweetView.getInstance().setInReplyToStatus(model);
			}
		}
		TweetView.getInstance().setText(text);
	}
	
	public void addReply(String userName)
	{
		inReplyTo = NONE_ID;
		Pattern pattern = Pattern.compile("@"+ userName);
		Matcher hasReply = pattern.matcher(text);
		StringBuilder builder = new StringBuilder();

		builder.append(text);
		if(hasReply.find())
		{
			return;
		}
		else
		{
			builder.append(String.format("@%s ", userName));
		}
		
		if(!text.startsWith("."))
		{
			builder.insert(0, ".");
		}		

		text = builder.toString();
		TweetView.getInstance().setText(text);
		TweetView.getInstance().removeObjects();
	}
	
	public void setPicturePath(File path)
	{
		pathPict = path;
		TweetView.getInstance().setPictureImage(pathPict);
	}
	
	public File getPicturePath()
	{
		return pathPict;
	}
	
	public void clear()
	{
		text = "";
		TweetView.getInstance().setText(text);
		setPicturePath(null);	
		inReplyTo = -1;
	}
	
	public void submit()
	{
		if (StringUtils.isNullOrEmpty(text) && pathPict == null)
		{
			ToastManager.show("‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢");
		}
		else
		{

			final StatusUpdate update = new StatusUpdate(text);
			if (inReplyTo >= 0)
			{
				update.setInReplyToStatusId(inReplyTo);
			}
			if(pathPict != null)
			{
				update.setMedia(pathPict);
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
