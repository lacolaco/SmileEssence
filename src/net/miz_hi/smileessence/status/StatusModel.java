package net.miz_hi.smileessence.status;

import java.util.Date;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.status.IconCaches.Icon;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.Status;
import twitter4j.User;
import android.text.Html;

public class StatusModel implements Comparable<StatusModel>
{
	public long statusId;
	public Icon icon;
	public String screenName;
	public String name;
	public String text;
	public String source;
	public String retweetedBy;
	public String createdAtString;
	public Date createdAt;
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
		if(isRetweet)
		{
			Status sourceStatus = status.getRetweetedStatus();
			statusId = sourceStatus.getId();
			screenName = sourceStatus.getUser().getScreenName();
			name = sourceStatus.getUser().getName();
			text = sourceStatus.getText();
			source = "via " + Html.fromHtml(sourceStatus.getSource()).toString();
			createdAt = sourceStatus.getCreatedAt();
			createdAtString = StringUtils.dateToString(createdAt);
			retweetedBy = "(RT:"+ status.getUser().getScreenName() + ")";
			backgroundColor = Client.getResource().getColor(R.color.LightBlue);
			IconCaches.setIconBitmapToView(sourceStatus.getUser(), null, this);
		}
		else
		{
			statusId = status.getId();
			screenName = status.getUser().getScreenName();
			name = status.getUser().getName();
			text = status.getText();
			source = "via " + Html.fromHtml(status.getSource()).toString();
			createdAt = status.getCreatedAt();
			createdAtString = StringUtils.dateToString(createdAt);
			retweetedBy = "";
			IconCaches.setIconBitmapToView(status.getUser(), null, this);
			if (isReply())
			{
				backgroundColor = Client.getResource().getColor(R.color.LightRed);
			}
			else
			{
				backgroundColor = -1;
			}
		}		
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
			isMine.set(StatusUtils.isMine(statusId));
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
	
	public User getUser()
	{
		return StatusStore.get(statusId).getUser();
	}
	
	public User getUserToShow()
	{
		return StatusStore.get(statusId).getUser();
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
