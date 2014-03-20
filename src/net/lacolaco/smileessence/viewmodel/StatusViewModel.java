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

    public URLEntity[] urls;
    public long id;
    public String text;
    public UserViewModel user;
    public Date createdAt;
    public String source;
    public boolean favorited;
    public StatusViewModel retweetedStatus;
    public UserMentionEntity[] mentions;
    public HashtagEntity[] hashtags;
    public MediaEntity[] media;

    public StatusViewModel(Status status)
    {
        if(status.isRetweet())
        {
            retweetedStatus = new StatusViewModel(status.getRetweetedStatus());
        }
        id = status.getId();
        text = status.getText();
        user = new UserViewModel(status.getUser());
        createdAt = status.getCreatedAt();
        source = status.getSource();
        favorited = status.isFavorited();
        mentions = status.getUserMentionEntities();
        hashtags = status.getHashtagEntities();
        media = status.getMediaEntities();
        urls = status.getURLEntities();
    }
}
