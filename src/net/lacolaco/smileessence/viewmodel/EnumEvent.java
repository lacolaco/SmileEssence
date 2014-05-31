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

import net.lacolaco.smileessence.R;

public enum EnumEvent
{

    FAVORITED(R.string.format_event_favorited),
    UNFAVORITED(R.string.format_event_unfavorited),
    RETWEETED(R.string.format_event_retweeted),
    MENTIONED(R.string.format_event_mentioned),
    FOLLOWED(R.string.format_event_followed),
    BLOCKED(R.string.format_event_blocked),
    UNBLOCKED(R.string.format_event_unblocked),
    RECEIVE_MESSAGE(R.string.format_event_message);

    // ------------------------------ FIELDS ------------------------------

    private int textFormatResourceID;

    // --------------------------- CONSTRUCTORS ---------------------------

    private EnumEvent(int textFormatResourceID)
    {
        this.textFormatResourceID = textFormatResourceID;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public int getTextFormatResourceID()
    {
        return textFormatResourceID;
    }
}
