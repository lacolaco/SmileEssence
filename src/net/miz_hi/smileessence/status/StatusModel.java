package net.miz_hi.smileessence.status;

import java.util.Date;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.status.IconCaches.Icon;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import android.text.Html;

public class StatusModel implements Comparable<StatusModel>
{
	public long statusId;
	public User user;
	public String screenName;
	public String name;
	public String text;
	public String footer;
	public Date createdAt;
	public URLEntity[] urls;
	public MediaEntity[] medias;
	public HashtagEntity[] hashtags;
	public UserMentionEntity[] userMentions;
	public int backgroundColor;
	public int nameColor;
	public int textColor;
	public boolean isRetweet;
	private ExtendedBoolean isReply = new ExtendedBoolean();
	private ExtendedBoolean isMine = new ExtendedBoolean();

	public static StatusModel createInstance(Status status)
	{
		if (status == null)
		{
			return null;
		}
		else
		{
			return new StatusModel(status);
		}
	}

	private StatusModel(Status status)
	{
		isRetweet = status.isRetweet();
		createdAt = status.getCreatedAt();
		Status shownStatus;
		StringBuilder footerBuiler = new StringBuilder();
		if(isRetweet)
		{
			shownStatus = status.getRetweetedStatus();
			statusId = shownStatus.getId();
			footerBuiler.append("(RT: ");
			footerBuiler.append(status.getUser().getScreenName());
			footerBuiler.append(") ");
			backgroundColor = Client.getResource().getColor(R.color.LightBlue);
		}
		else
		{
			shownStatus = status;
			statusId = shownStatus.getId();
			if (isReply())
			{
				backgroundColor = Client.getResource().getColor(R.color.LightRed);
			}
			else
			{
				backgroundColor = -1;
			}
		}
		user = shownStatus.getUser();
		screenName = user.getScreenName();
		name = user.getName();
		text = shownStatus.getText();
		
		footerBuiler.append(StringUtils.dateToString(shownStatus.getCreatedAt()));
		footerBuiler.append(" via ");
		footerBuiler.append(Html.fromHtml(shownStatus.getSource()).toString());
		footer = footerBuiler.toString();
		
		urls = shownStatus.getURLEntities();
		medias = shownStatus.getMediaEntities();
		hashtags = shownStatus.getHashtagEntities();
		userMentions = shownStatus.getUserMentionEntities();
		
		IconCaches.setIconBitmapToView(user, null);
		
		if (isMine())
		{				
			nameColor = Client.getResource().getColor(R.color.DarkBlue);
		}
		else
		{
			nameColor = Client.getResource().getColor(R.color.ThickGreen);
		}
		textColor = Client.getResource().getColor(R.color.Gray);
	}
	
	public boolean isMine()
	{
		if(!isMine.isInitialized())
		{
			isMine.set(StatusUtils.isMe(user));
		}
		return isMine.get();
	}
	
	public boolean isReply()
	{
		if(!isReply.isInitialized())
		{
			isReply.set(StatusUtils.isReply(statusId));
		}
		return isReply.get();
	}

	@Override
	public int compareTo(StatusModel another)
	{
		return another.createdAt.compareTo(this.createdAt);
	}
}
