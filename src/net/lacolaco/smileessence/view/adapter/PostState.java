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

package net.lacolaco.smileessence.view.adapter;

public class PostState
{

    private static PostState instance = new PostState();
    private String text = "";
    private long inReplyToStatusID = -1L;
    private String inReplyToScreenName = "";
    private String inReplyToText = "";
    private String mediaFilePath = "";
    private OnPostStateChangeListener listener;

    private PostState()
    {
    }

    public static PostState getInstance()
    {
        return instance;
    }

    public static PostState clearState()
    {
        return instance = new PostState();
    }

    public String getText()
    {
        return text;
    }

    public PostState setText(String text)
    {
        this.text = text;
        postStateChange();
        return this;
    }

    public long getInReplyToStatusID()
    {
        return inReplyToStatusID;
    }

    public PostState setInReplyToStatusID(long inReplyToStatusID)
    {
        this.inReplyToStatusID = inReplyToStatusID;
        postStateChange();
        return this;
    }

    public String getInReplyToScreenName()
    {
        return inReplyToScreenName;
    }

    public PostState setInReplyToScreenName(String inReplyToScreenName)
    {
        this.inReplyToScreenName = inReplyToScreenName;
        postStateChange();
        return this;
    }

    public String getInReplyToText()
    {
        return inReplyToText;
    }

    public PostState setInReplyToText(String inReplyToText)
    {
        this.inReplyToText = inReplyToText;
        postStateChange();
        return this;
    }

    public String getMediaFilePath()
    {
        return mediaFilePath;
    }

    public PostState setMediaFilePath(String mediaFilePath)
    {
        this.mediaFilePath = mediaFilePath;
        postStateChange();
        return this;
    }

    public void setListener(OnPostStateChangeListener listener)
    {
        this.listener = listener;
    }

    public void removeListener()
    {
        this.listener = null;
    }

    private void postStateChange()
    {
        if(listener != null)
        {
            listener.onPostStateChange(this);
        }
    }

    public static interface OnPostStateChangeListener
    {

        void onPostStateChange(PostState postState);
    }
}
