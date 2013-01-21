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
import android.text.Html;

public class StatusModel implements Comparable<StatusModel>
{
	public long statusId;
	public User user;
	public String screenName;
	public String name;
	public String text;
	public String source;
	public String retweetedBy;
	public String createdAtString;
	public Date createdAt;
	public URLEntity[] urls;
	public MediaEntity[] medias;
	public HashtagEntity[] hashtags;
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
		Status shownStatus;
		if(isRetweet)
		{
			shownStatus = status.getRetweetedStatus();
			statusId = shownStatus.getId();
			retweetedBy = "(RT:"+ status.getUser().getScreenName() + ")";
			backgroundColor = Client.getResource().getColor(R.color.LightBlue);
		}
		else
		{
			shownStatus = status;
			statusId = shownStatus.getId();
			retweetedBy = "";
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
		source = "via " + Html.fromHtml(shownStatus.getSource()).toString();
		createdAt = shownStatus.getCreatedAt();
		createdAtString = StringUtils.dateToString(createdAt);
		urls = shownStatus.getURLEntities();
		medias = shownStatus.getMediaEntities();
		hashtags = shownStatus.getHashtagEntities();
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
		return this.createdAt.compareTo(another.createdAt);
	}
	
	@Override
	public boolean equals(Object another)
	{
		if(another instanceof StatusModel)
		{
			return this.statusId == ((StatusModel)another).statusId;
		}
		
		return false;
	}

}
