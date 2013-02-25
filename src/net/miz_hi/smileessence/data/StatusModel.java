package net.miz_hi.smileessence.data;

import java.util.Date;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.status.StatusUtils;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
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
	public String screenName;
	public String name;
	public String text;
	public String retweeterScreenName;
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

	private ExtendedBoolean isReply = new ExtendedBoolean();
	private ExtendedBoolean isMine = new ExtendedBoolean();

	public StatusModel(Status status)
	{
		isRetweet = status.isRetweet();
		createdAt = status.getCreatedAt();

		Status shownStatus;
		if (isRetweet)
		{
			retweeterScreenName = status.getUser().getScreenName();
			shownStatus = status.getRetweetedStatus();
			backgroundColor = Client.getColor(R.color.LightBlue);
		}
		else
		{
			shownStatus = status;
			backgroundColor = -1;
		}
		
		statusId = shownStatus.getId();
		inReplyToStatusId = shownStatus.getInReplyToStatusId();

		if (UserStore.get(shownStatus.getUser().getId()) != null)
		{
			user = UserStore.get(shownStatus.getUser().getId());
		}
		else
		{
			user = UserStore.put(shownStatus.getUser());
		}
		screenName = user.screenName;
		name = user.name;
		
		text = shownStatus.getText();
		if(Morse.isMorse(text))
		{
			text = text + "\n(" + Morse.mcToJa(text) + ")";
		}
		
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

		IconCaches.setIconBitmapToView(user, null);

		headerText = getHeaderText(user);
		footerText = getFooterText(status);

		if (isMine())
		{
			nameColor = Client.getColor(R.color.DarkBlue);
		}
		else
		{
			nameColor = Client.getColor(R.color.ThickGreen);
		}

		isReply.set(StatusUtils.isReply(shownStatus));
		if (!isRetweet && isReply.get())
		{
			backgroundColor = Client.getColor(R.color.LightRed);
		}
		textColor = Client.getColor(R.color.Gray);
	}

	public boolean isMine()
	{
		if (!isMine.isInitialized())
		{
			isMine.set(user.isMe());
		}
		return isMine.get();
	}

	public boolean isReply()
	{
		return isReply.get();
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
