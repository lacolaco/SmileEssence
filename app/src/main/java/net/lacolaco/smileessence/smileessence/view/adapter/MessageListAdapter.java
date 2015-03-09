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

import android.app.Activity;
import net.lacolaco.smileessence.viewmodel.MessageViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MessageListAdapter extends CustomListAdapter<MessageViewModel>
{

    // --------------------------- CONSTRUCTORS ---------------------------

    public MessageListAdapter(Activity activity)
    {
        super(activity, MessageViewModel.class);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getLastID()
    {
        return ((MessageViewModel) getItem(getCount() - 1)).getID();
    }

    public long getTopID()
    {
        return ((MessageViewModel) getItem(0)).getID();
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void addToBottom(MessageViewModel... items)
    {
        for(MessageViewModel item : items)
        {
            if(!preAdd(item))
            {
                continue;
            }
            super.addToBottom(items);
        }
    }

    @Override
    public void addToTop(MessageViewModel... items)
    {
        for(MessageViewModel item : items)
        {
            if(!preAdd(item))
            {
                continue;
            }
            super.addToTop(items);
        }
    }

    @Override
    public void sort()
    {
        synchronized(LOCK)
        {
            Collections.sort(list, new Comparator<MessageViewModel>()
            {
                @Override
                public int compare(MessageViewModel lhs, MessageViewModel rhs)
                {
                    return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
                }
            });
        }
    }

    // -------------------------- OTHER METHODS --------------------------

    public MessageViewModel removeByMessageID(long messageID)
    {
        synchronized(this.LOCK)
        {
            Iterator<MessageViewModel> iterator = this.list.iterator();
            while(iterator.hasNext())
            {
                MessageViewModel message = iterator.next();
                if(message.getID() == messageID)
                {
                    iterator.remove();
                    return message;
                }
            }
            return null;
        }
    }

    private boolean preAdd(MessageViewModel item)
    {
        removeByMessageID(item.getID());
        return true;
    }
}
