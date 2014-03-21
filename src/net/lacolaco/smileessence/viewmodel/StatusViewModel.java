/*
 * SmileEssence Lite
 * Copyright 2014 laco0416
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License
 */

package net.lacolaco.smileessence.viewmodel;

import twitter4j.*;

import java.util.Date;

public class StatusViewModel
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
        return getId();
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

    public long getId()
    {
        return id;
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
}
