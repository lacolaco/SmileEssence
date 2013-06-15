package net.miz_hi.smileessence.status;

import java.util.Date;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
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
	public boolean isFavorited;
	
	private StatusModel(){};

	public StatusModel(Status status)
	{
		isRetweet = status.isRetweet();
		createdAt = status.getCreatedAt();
		isFavorited = status.isFavorited();
		
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
		
		updateHeaderText();
		getFooterText(status);

		isMine = user.isMe();

		isReply = StatusUtils.isReply(shownStatus);
	}

	public void updateHeaderText()
	{
		StringBuilder builder = new StringBuilder();
		String style = Client.getPreferenceValue(EnumPreferenceKey.NAME_STYLE);
		if(style.equals(EnumNameStyle.S_N.get()) || style.equals(EnumNameStyle.S.get()))
		{
			builder.append(user.screenName);
		}
		else if(style.equals(EnumNameStyle.N_S.get()) || style.equals(EnumNameStyle.N.get()))
		{
			builder.append(user.name);
		}
		if(style.equals(EnumNameStyle.S_N.get()))
		{
			builder.append(" / ");
			builder.append(user.name);
		}
		else if(style.equals(EnumNameStyle.N_S.get()))
		{
			builder.append(" / ");
			builder.append(user.screenName);
		}
		headerText = builder.toString();
	}

	public void getFooterText(Status status)
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
		footerText = builder.toString();
	}

	@Override
	public int compareTo(StatusModel another)
	{
		return another.createdAt.compareTo(this.createdAt);
	}
	
	public static StatusModel getNullStatusModel()
	{
		StatusModel status = new StatusModel();
		status.isRetweet = false;
		status.createdAt = new Date();
		status.statusId = 0;
		status.inReplyToStatusId = 0;		
		status.user = UserModel.getNullUserModel();
		status.screenName = status.user.screenName;
		status.name = status.user.name;		
		status.text = "";
		
		status.urls = null;
		status.medias = null;
		status.hashtags = null;
		status.userMentions = null;
		
		status.headerText = "";
		status.footerText = "";

		status.isMine = false;
		status.isReply = false;
		return status;
	}
}