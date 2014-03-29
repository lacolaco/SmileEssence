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
import twitter4j.Status;
import twitter4j.User;

public class EventViewModel implements IViewModel
{

    private EnumEvent event;
    private long sourceUserID;
    private long targetStatusID;
    private String sourceScreenName;

    public EventViewModel(EnumEvent event, User source, Status status)
    {
        this.event = event;
        this.sourceUserID = source.getId();
        this.sourceScreenName = source.getScreenName();
        if(status != null)
        {
            targetStatusID = status.getId();
        }
        else
        {
            targetStatusID = -1L;
        }
    }

    public EventViewModel(EnumEvent event, User source)
    {
        this(event, source, null);
    }

    public EnumEvent getEvent()
    {
        return event;
    }

    public long getSourceUserID()
    {
        return sourceUserID;
    }

    public String getSourceScreenName()
    {
        return sourceScreenName;
    }

    public long getTargetStatusID()
    {
        return targetStatusID;
    }

    public boolean isStatusEvent()
    {
        return targetStatusID != -1L;
    }

    public String getFormattedString(Context context)
    {
        return context.getString(event.getTextFormatResourceID(), sourceScreenName);
    }

    @Override
    public View getView(Context context, LayoutInflater inflater, View convertedView)
    {
        return new TextView(context);
    }
}
