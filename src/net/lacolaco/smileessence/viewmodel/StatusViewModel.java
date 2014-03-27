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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import twitter4j.*;

import java.util.Date;

public class StatusViewModel implements IViewModel
{

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

    public StatusViewModel(Status status)
    {
        if(status.isRetweet())
        {
            retweetedStatus = new StatusViewModel(status.getRetweetedStatus());
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
    }

    public long getID()
    {
        return id;
    }

    public String getText()
    {
        return text;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public String getSource()
    {
        return source;
    }

    public boolean isFavorited()
    {
        return isFavorited;
    }

    public StatusViewModel getRetweetedStatus()
    {
        return retweetedStatus;
    }

    public UserMentionEntity[] getMentions()
    {
        return mentions;
    }

    public HashtagEntity[] getHashtags()
    {
        return hashtags;
    }

    public MediaEntity[] getMedia()
    {
        return media;
    }

    public URLEntity[] getURLs()
    {
        return urls;
    }

    public SymbolEntity[] getSymbols()
    {
        return symbols;
    }

    public long getUserID()
    {
        return userID;
    }

    public String getScreenName()
    {
        return screenName;
    }

    public String getName()
    {
        return name;
    }

    public String getIconURL()
    {
        return iconURL;
    }

    public boolean isProtected()
    {
        return isProtected;
    }

    @Override
    public View getView(Context context, LayoutInflater inflater, View convertedView)
    {
        return new TextView(context);
    }
}
