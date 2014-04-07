/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.viewmodel;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.util.NameStyles;
import net.lacolaco.smileessence.util.StringUtils;
import twitter4j.*;

import java.util.Date;

public class StatusViewModel implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    private long id;
    private long userID;
    private String screenName;
    private String name;
    private String iconURL;
    private String text;
    private Date createdAt;
    private String source;
    private boolean isFavorited;
    private boolean isProtected;
    private StatusViewModel retweetedStatus;
    private UserMentionEntity[] mentions;
    private HashtagEntity[] hashtags;
    private MediaEntity[] media;
    private URLEntity[] urls;
    private SymbolEntity[] symbols;
    private boolean isMyStatus;
    private boolean isMention;
    private boolean isRetweetOfMe;

    // --------------------------- CONSTRUCTORS ---------------------------

    public StatusViewModel(Status status, Account account)
    {
        if(status.isRetweet())
        {
            retweetedStatus = new StatusViewModel(status.getRetweetedStatus(), account);
        }
        id = status.getId();
        text = status.getText();
        createdAt = status.getCreatedAt();
        source = status.getSource();
        isFavorited = status.isFavorited();
        mentions = status.getUserMentionEntities();
        hashtags = status.getHashtagEntities();
        media = status.getMediaEntities();
        urls = status.getURLEntities();
        symbols = status.getSymbolEntities();
        User user = status.getUser();
        userID = user.getId();
        screenName = user.getScreenName();
        name = user.getName();
        iconURL = user.getProfileImageURLHttps();
        isProtected = user.isProtected();
        setMention(isMention(account.screenName));
        setMyStatus(isMyStatus(account.userID));
        setRetweetOfMe(isRetweetOfMe(account.userID));
    }

    public boolean isMention(String screenName)
    {
        return text.contains(String.format("@%s", screenName));
    }

    public boolean isMyStatus(long userID)
    {
        return this.userID == userID;
    }

    public boolean isRetweetOfMe(long userID)
    {
        return retweetedStatus != null && retweetedStatus.getUserID() == userID;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Date getCreatedAt()
    {
        if(isRetweet())
        {
            return retweetedStatus.createdAt;
        }
        return createdAt;
    }

    public HashtagEntity[] getHashtags()
    {
        if(isRetweet())
        {
            return retweetedStatus.hashtags;
        }
        return hashtags;
    }

    public String getIconURL()
    {
        if(isRetweet())
        {
            return retweetedStatus.iconURL;
        }
        return iconURL;
    }

    public long getID()
    {
        return id;
    }

    public MediaEntity[] getMedia()
    {
        if(isRetweet())
        {
            return retweetedStatus.media;
        }
        return media;
    }

    public UserMentionEntity[] getMentions()
    {
        if(isRetweet())
        {
            return retweetedStatus.mentions;
        }
        return mentions;
    }

    public String getName()
    {
        if(isRetweet())
        {
            return retweetedStatus.name;
        }
        return name;
    }

    public StatusViewModel getRetweetedStatus()
    {
        return retweetedStatus;
    }

    public String getScreenName()
    {
        if(isRetweet())
        {
            return retweetedStatus.screenName;
        }
        return screenName;
    }

    public String getSource()
    {
        if(isRetweet())
        {
            return retweetedStatus.source;
        }
        return source;
    }

    public SymbolEntity[] getSymbols()
    {
        return symbols;
    }

    public String getText()
    {
        if(isRetweet())
        {
            return retweetedStatus.text;
        }
        return text;
    }

    public URLEntity[] getURLs()
    {
        return urls;
    }

    public long getUserID()
    {
        return userID;
    }

    public boolean isFavorited()
    {
        if(isRetweet())
        {
            return retweetedStatus.isFavorited;
        }
        return isFavorited;
    }

    public boolean isMention()
    {
        if(isRetweet())
        {
            return retweetedStatus.isMention;
        }
        return isMention;
    }

    public void setMention(boolean mention)
    {
        isMention = mention;
    }

    public boolean isMyStatus()
    {
        if(isRetweet())
        {
            return retweetedStatus.isMyStatus;
        }
        return isMyStatus;
    }

    public void setMyStatus(boolean myStatus)
    {
        isMyStatus = myStatus;
    }

    public boolean isProtected()
    {
        if(isRetweet())
        {
            return retweetedStatus.isProtected;
        }
        return isProtected;
    }

    private boolean isRetweet()
    {
        return retweetedStatus != null;
    }

    public boolean isRetweetOfMe()
    {
        if(isRetweet())
        {
            return retweetedStatus.isRetweetOfMe;
        }
        return isRetweetOfMe;
    }

    public void setRetweetOfMe(boolean retweet)
    {
        this.isRetweetOfMe = retweet;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface IViewModel ---------------------

    @Override
    public View getView(Context context, LayoutInflater inflater, View convertedView)
    {
        if(convertedView == null)
        {
            convertedView = inflater.inflate(R.layout.list_item_status, null);
        }
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(context);
        int textSize = preferenceHelper.getValue(R.string.key_setting_text_size, 10);
        int nameStyle = preferenceHelper.getValue(R.string.key_setting_namestyle, 0);
        NetworkImageView icon = (NetworkImageView)convertedView.findViewById(R.id.imageview_status_icon);
        ImageCache.getInstance().setImageToView(getIconURL(), icon);
        TextView header = (TextView)convertedView.findViewById(R.id.textview_status_header);
        header.setTextSize(textSize);
        header.setText(NameStyles.getNameString(nameStyle, getScreenName(), getName()));
        TextView content = (TextView)convertedView.findViewById(R.id.textview_status_text);
        content.setTextSize(textSize);
        content.setText(getText());
        TextView footer = (TextView)convertedView.findViewById(R.id.textview_status_footer);
        footer.setTextSize(textSize);
        StringBuilder builder = new StringBuilder();
        if(isRetweet())
        {
            builder.append("(RT: ");
            builder.append(this.screenName);
            builder.append(") ");
        }
        builder.append(StringUtils.dateToString(getCreatedAt()));
        builder.append(" via ");
        builder.append(Html.fromHtml(getSource()));
        footer.setText(builder.toString());
        ImageView favorited = (ImageView)convertedView.findViewById(R.id.imageview_status_favorited);
        favorited.setVisibility(isFavorited() ? View.VISIBLE : View.GONE);

        return convertedView;
    }
}
