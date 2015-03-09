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

import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import twitter4j.StatusUpdate;

public class PostState
{

    // ------------------------------ FIELDS ------------------------------

    private static PostState instance = new PostState();
    private String text = "";
    private long inReplyToStatusID = -1L;
    private String inReplyToScreenName = "";
    private String inReplyToText = "";
    private String mediaFilePath = "";
    private boolean directMessage = false;
    private OnPostStateChangeListener listener;
    private int selectionStart = 0;
    private int selectionEnd = 0;

    // -------------------------- STATIC METHODS --------------------------

    private PostState()
    {
    }

    public static PostState getState()
    {
        return instance;
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static PostState newState()
    {
        return instance = new PostState().setListener(instance.listener);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getInReplyToStatusID()
    {
        return inReplyToStatusID;
    }

    public String getMediaFilePath()
    {
        return mediaFilePath;
    }

    public int getSelectionEnd()
    {
        if(selectionEnd < 0)
        {
            return text.length();
        }
        return selectionEnd;
    }

    public int getSelectionStart()
    {
        if(selectionStart < 0)
        {
            return text.length();
        }
        return selectionStart;
    }

    public String getText()
    {
        return text;
    }

    public PostState setListener(OnPostStateChangeListener listener)
    {
        this.listener = listener;
        return this;
    }

    // -------------------------- OTHER METHODS --------------------------

    public PostStateTransaction beginTransaction()
    {
        return new PostStateTransaction(this);
    }

    public void removeListener()
    {
        this.listener = null;
    }

    /**
     * Convert to StatusUpdate for tweet.
     *
     * @return StatusUpdate
     */
    public StatusUpdate toStatusUpdate()
    {
        return new StatusUpdate(getText())
                .inReplyToStatusId(getInReplyToStatusID());
    }

    private PostState copy(PostState another)
    {
        this.text = another.text;
        this.inReplyToStatusID = another.inReplyToStatusID;
        this.inReplyToScreenName = another.inReplyToScreenName;
        this.inReplyToText = another.inReplyToText;
        this.mediaFilePath = another.mediaFilePath;
        this.directMessage = another.directMessage;
        this.selectionStart = another.selectionStart;
        this.selectionEnd = another.selectionEnd;
        this.listener = another.listener;
        return this;
    }

    private void postStateChange()
    {
        if(listener != null)
        {
            listener.onPostStateChange(this);
        }
    }

    // -------------------------- INNER CLASSES --------------------------

    public static interface OnPostStateChangeListener
    {

        void onPostStateChange(PostState postState);
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

        public PostStateTransaction appendText(String text)
        {
            state.text = state.text + text;
            return this;
        }

        public PostStateTransaction insertText(int index, String text)
        {
            StringBuilder builder = new StringBuilder(state.text);
            builder.insert(index, text);
            state.text = builder.toString();
            return this;
        }

        public PostStateTransaction setInReplyToStatusID(long inReplyToStatusID)
        {
            state.inReplyToStatusID = inReplyToStatusID;
            return this;
        }

        public PostStateTransaction setMediaFilePath(String mediaFilePath)
        {
            state.mediaFilePath = mediaFilePath;
            return this;
        }

        public PostStateTransaction setCursor(int cursor)
        {
            state.selectionStart = state.selectionEnd = cursor;
            return this;
        }

        public PostStateTransaction setSelection(int start, int end)
        {
            state.selectionStart = start;
            state.selectionEnd = end;
            return this;
        }

        public void commit()
        {
            PostState.getState().copy(state).postStateChange();
        }

        public void commitWithOpen(MainActivity activity)
        {
            DialogHelper.closeAll(activity);
            PostState.getState().copy(state).postStateChange();
            activity.openPostPage();
        }

        public PostStateTransaction moveCursor(int length)
        {
            int cursor = state.selectionEnd + length;
            return setCursor(cursor);
        }
    }
}
