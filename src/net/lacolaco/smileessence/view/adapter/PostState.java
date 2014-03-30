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
    private boolean directMessage = false;
    private boolean requestedToOpen = false;
    private OnPostStateChangeListener listener;

    private PostState()
    {
    }

    public static PostState getState()
    {
        return instance;
    }

    public static PostState newState()
    {
        return instance = new PostState().setListener(instance.listener);
    }

    public String getText()
    {
        return text;
    }

    public long getInReplyToStatusID()
    {
        return inReplyToStatusID;
    }

    public String getInReplyToScreenName()
    {
        return inReplyToScreenName;
    }

    public String getInReplyToText()
    {
        return inReplyToText;
    }

    public String getMediaFilePath()
    {
        return mediaFilePath;
    }

    public boolean isDirectMessage()
    {
        return directMessage;
    }

    public boolean isRequestedToOpen()
    {
        return requestedToOpen;
    }

    public PostState setListener(OnPostStateChangeListener listener)
    {
        this.listener = listener;
        return this;
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

    public PostStateTransaction beginTransaction()
    {
        return new PostStateTransaction(this);
    }

    private PostState copy(PostState another)
    {
        this.text = another.text;
        this.inReplyToStatusID = another.inReplyToStatusID;
        this.inReplyToScreenName = another.inReplyToScreenName;
        this.inReplyToText = another.inReplyToText;
        this.mediaFilePath = another.mediaFilePath;
        this.directMessage = another.directMessage;
        this.requestedToOpen = another.requestedToOpen;
        this.listener = another.listener;
        return this;
    }

    public static class PostStateTransaction
    {

        private PostState state;

        private PostStateTransaction(PostState state)
        {
            this.state = new PostState().copy(state);
        }

        public PostStateTransaction setText(String text)
        {
            state.text = text;
            return this;
        }

        public PostStateTransaction setInReplyToStatusID(long inReplyToStatusID)
        {
            state.inReplyToStatusID = inReplyToStatusID;
            return this;
        }

        public PostStateTransaction setInReplyToScreenName(String inReplyToScreenName)
        {
            state.inReplyToScreenName = inReplyToScreenName;
            return this;
        }

        public PostStateTransaction setInReplyToText(String inReplyToText)
        {
            state.inReplyToText = inReplyToText;
            return this;
        }

        public PostStateTransaction setMediaFilePath(String mediaFilePath)
        {
            state.mediaFilePath = mediaFilePath;
            return this;
        }

        public PostStateTransaction setDirectMessage(boolean directMessage)
        {
            state.directMessage = directMessage;
            return this;
        }

        public PostStateTransaction requestOpenPage(boolean openPageRequest)
        {
            state.requestedToOpen = openPageRequest;
            return this;
        }

        public void commit()
        {
            PostState.getState().copy(state).postStateChange();
        }
    }

    public static interface OnPostStateChangeListener
    {

        void onPostStateChange(PostState postState);
    }
}
