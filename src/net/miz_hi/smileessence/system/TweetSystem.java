package net.miz_hi.smileessence.system;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.StringUtils;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.TweetView;
import twitter4j.StatusUpdate;

public class TweetSystem
{
	private static TweetSystem instance = new TweetSystem();	
	
	private long inReplyTo = NONE_ID;
	private String text = ""; 
	private Pattern replyPattern = Pattern.compile("@[a-zA-Z0-9_]+");
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
	
	public void clear()
	{
		text = "";
		TweetView.getInstance().setText(text);
	}
	
	public void submit()
	{
		if (StringUtils.isNullOrEmpty(text))
		{
			ToastManager.getInstance().toast("‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢");
		}
		else
		{
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					StatusUpdate update = new StatusUpdate(text);
					if (inReplyTo >= 0)
					{
						update.setInReplyToStatusId(inReplyTo);
						inReplyTo = -1;
					}
					new AsyncTweetTask(update).addToQueue();
				}
			}.postDelayed(10);
		}
	}
}
