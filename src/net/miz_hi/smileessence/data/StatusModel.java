package net.miz_hi.smileessence.data;

import java.util.Calendar;
import java.util.Date;

import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.status.StatusUtils;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import android.text.Html;

/**
 * data model for view and menu
 */
public class StatusModel implements Comparable<StatusModel>
{
	public long statusId;
	public long inReplyToStatusId;
	public UserModel user;
	public UserModel retweeter;
	public String screenName;
	public String name;
	public String text;
	public String headerText;
	public String footerText;
	public Date createdAt;
	public URLEntity[] urls;
	public MediaEntity[] medias;
	public HashtagEntity[] hashtags;
	public UserMentionEntity[] userMentions;
	public int backgroundColor;
	public int nameColor;
	public int textColor;
	public boolean isRetweet;
	public boolean isReply;
	public boolean isMine;

	public StatusModel(Status status)
	{
		isRetweet = status.isRetweet();
		createdAt = status.getCreatedAt();

		Status shownStatus;
		if (isRetweet)
		{
			retweeter = UserStore.put(status.getUser());
			shownStatus = status.getRetweetedStatus();
		}
		else
		{
			shownStatus = status;
		}
		
		statusId = shownStatus.getId();
		inReplyToStatusId = shownStatus.getInReplyToStatusId();
		
		user = UserStore.put(shownStatus.getUser());

		screenName = user.screenName;
		name = user.name;
		
		text = shownStatus.getText();
		
		urls = shownStatus.getURLEntities();
		medias = shownStatus.getMediaEntities();
		hashtags = shownStatus.getHashtagEntities();
		if(hashtags != null)
		{
			for(HashtagEntity hashtag : hashtags)
			{
				StatusStore.putHashtag(hashtag.getText());
			}
		}
		userMentions = shownStatus.getUserMentionEntities();
		
		headerText = getHeaderText(user);
		footerText = getFooterText(status);

		isMine = user.isMe();

		isReply = StatusUtils.isReply(shownStatus);
	}

	public String getHeaderText(UserModel user)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(user.screenName);
		builder.append(" / ");
		builder.append(user.name);
		return builder.toString();
	}

	public String getFooterText(Status status)
	{
		StringBuilder builder = new StringBuilder();
		if (isRetweet)
		{
			builder.append("(RT: ");
			builder.append(status.getUser().getScreenName());
			builder.append(") ");
			builder.append(StringUtils.dateToString(status.getRetweetedStatus().getCreatedAt()));
			builder.append(" via ");
			builder.append(Html.fromHtml(status.getRetweetedStatus().getSource()).toString());
		}
		else
		{
			builder.append(StringUtils.dateToString(status.getCreatedAt()));
			builder.append(" via ");
			builder.append(Html.fromHtml(status.getSource()).toString());
		}
		return builder.toString();
	}

	@Override
	public int compareTo(StatusModel another)
	{
		return another.createdAt.compareTo(this.createdAt);
	}
}
