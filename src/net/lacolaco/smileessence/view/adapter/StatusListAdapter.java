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
import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class StatusListAdapter extends CustomListAdapter<StatusViewModel>
{

    // --------------------------- CONSTRUCTORS ---------------------------

    public StatusListAdapter(Activity activity)
    {
        super(activity, StatusViewModel.class);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getLastID()
    {
        return ((StatusViewModel) getItem(getCount() - 1)).getID();
    }

    public long getTopID()
    {
        return ((StatusViewModel) getItem(0)).getID();
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void addToBottom(StatusViewModel... items)
    {
        for(StatusViewModel item : items)
        {
            if(isBlockUser(item))
            {
                continue;
            }
            super.addToBottom(item);
        }
    }

    private boolean isBlockUser(StatusViewModel item)
    {
        return UserCache.getInstance().isBlockID(item.getOriginalUserID());
    }

    @Override
    public void addToTop(StatusViewModel... items)
    {
        for(StatusViewModel item : items)
        {
            if(isBlockUser(item))
            {
                continue;
            }
            super.addToTop(item);
        }
    }

    // -------------------------- OTHER METHODS --------------------------

    public StatusViewModel removeByStatusID(long statusID)
    {
        synchronized(this.LOCK)
        {
            Iterator<StatusViewModel> iterator = this.list.iterator();
            while(iterator.hasNext())
            {
                StatusViewModel statusViewModel = iterator.next();
                if(statusViewModel.getID() == statusID)
                {
                    iterator.remove();
                    update();
                    return statusViewModel;
                }
            }
            return null;
        }
    }

    /**
     * Sort list by Status#createdAt
     */
    public void sort()
    {
        synchronized(LOCK)
        {
            Collections.sort(list, new Comparator<StatusViewModel>()
            {
                @Override
                public int compare(StatusViewModel lhs, StatusViewModel rhs)
                {
                    return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
                }
            });
        }
    }
}
