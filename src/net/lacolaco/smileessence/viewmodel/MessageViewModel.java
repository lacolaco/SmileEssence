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
import twitter4j.DirectMessage;

import java.util.Date;

public class MessageViewModel implements IViewModel
{

    private long id;
    private long senderID;
    private String senderScreenName;
    private String senderIconURL;
    private String text;
    private Date createdAt;

    public MessageViewModel(DirectMessage directMessage)
    {
        id = directMessage.getId();
        senderID = directMessage.getSenderId();
        senderScreenName = directMessage.getSenderScreenName();
        senderIconURL = directMessage.getSender().getProfileImageURL();
        text = directMessage.getText();
        createdAt = directMessage.getCreatedAt();
    }

    public long getID()
    {
        return id;
    }

    public long getSenderID()
    {
        return senderID;
    }

    public String getSenderScreenName()
    {
        return senderScreenName;
    }

    public String getSenderIconURL()
    {
        return senderIconURL;
    }

    public String getText()
    {
        return text;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    @Override
    public View getView(Context context, LayoutInflater inflater, View convertedView)
    {
        return new TextView(context);
    }
}
