package net.miz_hi.smileessence.model.status.tweet;

import android.text.Html;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.cache.TweetCache;
import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.status.ResponseConverter;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.EnumNameStyle;
import net.miz_hi.smileessence.status.TweetUtils;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.*;

import java.util.Date;

/**
 * data model for view and menu
 */
public class TweetModel implements Comparable<TweetModel>, IStatusModel
{

    public Date createdAt;
    public long statusId;
    public long inReplyToStatusId;
    public UserModel user;
    public String text;
    public URLEntity[] urls;
    public MediaEntity[] medias;
    public HashtagEntity[] hashtags;
    public UserMentionEntity[] userMentions;
    public String source;
    public long parentStatusId;
    public UserModel retweeter;
    public EnumTweetType type = EnumTweetType.NORMAL;

    public TweetModel(Status status)
    {
        Status shownStatus;
        if (status.isRetweet())
        {
            retweeter = ResponseConverter.convert(status.getUser());
            shownStatus = status.getRetweetedStatus();
        }
        else
        {
            shownStatus = status;
        }

        createdAt = status.getCreatedAt();
        parentStatusId = status.getId();

        statusId = shownStatus.getId();
        inReplyToStatusId = shownStatus.getInReplyToStatusId();
        user = ResponseConverter.convert(shownStatus.getUser());
        text = shownStatus.getText();
        urls = shownStatus.getURLEntities();
        medias = shownStatus.getMediaEntities();
        hashtags = shownStatus.getHashtagEntities();
        userMentions = shownStatus.getUserMentionEntities();
        source = Html.fromHtml(shownStatus.getSource()).toString();

        if (hashtags != null)
        {
            for (HashtagEntity hashtag : hashtags)
            {
                TweetCache.putHashtag(hashtag.getText());
            }
        }

        type = status.isRetweet() ? EnumTweetType.RETWEET : (TweetUtils.isReply(status) ? EnumTweetType.REPLY : EnumTweetType.NORMAL);
    }

    @Override
    public int compareTo(TweetModel another)
    {
        return another.createdAt.compareTo(this.createdAt);
    }

    private TweetModel()
    {
    }

    public static TweetModel getNullStatusModel()
    {
        TweetModel status = new TweetModel();
        status.createdAt = new Date();
        status.statusId = 0;
        status.inReplyToStatusId = 0;
        status.user = UserModel.getNullUserModel();
        status.text = "";
        status.source = "";
        return status;
    }

    @Override
    public UserModel getUser()
    {
        return user;
    }

    @Override
    public String getTextTop()
    {
        StringBuilder builder = new StringBuilder();
        String style = Client.getPreferenceValue(EnumPreferenceKey.NAME_STYLE);
        if (style.equals(EnumNameStyle.S_N.get()) || style.equals(EnumNameStyle.S.get()))
        {
            builder.append(user.screenName);
        }
        else if (style.equals(EnumNameStyle.N_S.get()) || style.equals(EnumNameStyle.N.get()))
        {
            builder.append(user.name);
        }
        if (style.equals(EnumNameStyle.S_N.get()))
        {
            builder.append(" / ");
            builder.append(user.name);
        }
        else if (style.equals(EnumNameStyle.N_S.get()))
        {
            builder.append(" / ");
            builder.append(user.screenName);
        }
        return builder.toString();
    }

    @Override
    public String getTextContent()
    {
        return text;
    }

    @Override
    public String getTextBottom()
    {
        StringBuilder builder = new StringBuilder();
        if (type == EnumTweetType.RETWEET)
        {
            builder.append("(RT: ");
            builder.append(retweeter.screenName);
            builder.append(") ");
            builder.append(StringUtils.dateToString(createdAt));
            builder.append(" via ");
            builder.append(source);
        }
        else
        {
            builder.append(StringUtils.dateToString(createdAt));
            builder.append(" via ");
            builder.append(source);
        }
        return builder.toString();
    }
}
