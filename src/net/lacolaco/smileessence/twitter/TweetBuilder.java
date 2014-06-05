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

package net.lacolaco.smileessence.twitter;

import android.text.TextUtils;
import twitter4j.Status;
import twitter4j.StatusUpdate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class TweetBuilder
{

    // ------------------------------ FIELDS ------------------------------

    private String text = "";
    private ArrayList<String> screenNameList = new ArrayList<>();
    private long inReplyToStatusID = -1;
    private String mediaPath = "";

    // --------------------- GETTER / SETTER METHODS ---------------------

    public TweetBuilder setInReplyToStatusID(long inReplyToStatusID)
    {
        this.inReplyToStatusID = inReplyToStatusID;
        return this;
    }

    public TweetBuilder setMediaPath(String mediaPath)
    {
        this.mediaPath = mediaPath;
        return this;
    }

    public TweetBuilder setQuotation(Status status)
    {
        return setText(String.format(" RT @%s: %s", status.getUser().getScreenName(), status.getText()));
    }

    public TweetBuilder setText(String text)
    {
        this.text = text;
        return this;
    }

    // -------------------------- OTHER METHODS --------------------------

    public TweetBuilder addScreenName(String screenName)
    {
        screenNameList.add(screenName);
        return this;
    }

    public TweetBuilder addScreenNames(Collection<String> screenNames)
    {
        screenNameList.addAll(screenNames);
        return this;
    }

    public TweetBuilder appendText(String str)
    {
        return setText(text + str);
    }

    public StatusUpdate build()
    {
        StatusUpdate statusUpdate = new StatusUpdate(buildText());
        if(inReplyToStatusID >= 0)
        {
            statusUpdate.setInReplyToStatusId(inReplyToStatusID);
        }
        if(!TextUtils.isEmpty(mediaPath))
        {
            File media = new File(mediaPath);
            if(media.exists())
            {
                statusUpdate.setMedia(media);
            }
        }
        return statusUpdate;
    }

    public String buildText()
    {
        StringBuilder builder = new StringBuilder();

        for(String screenName : screenNameList)
        {
            builder.append(String.format("@%s ", screenName));
        }
        builder.append(text);
        return builder.toString();
    }

    public TweetBuilder setQuotation(Status status, String text)
    {
        return setText(String.format("%s RT @%s: %s", text, status.getUser().getScreenName(), status.getText()));
    }
}
