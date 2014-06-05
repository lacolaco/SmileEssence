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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.common.collect.Iterables;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.viewmodel.IViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomListAdapter<T extends IViewModel> extends BaseAdapter
{

    // ------------------------------ FIELDS ------------------------------

    protected final Object LOCK = new Object();
    protected Class<T> clss;
    protected ArrayList<T> list = new ArrayList<>();
    protected T[] array;
    protected int count;
    protected boolean isNotifiable = true;
    protected Activity activity;

    // --------------------------- CONSTRUCTORS ---------------------------

    public CustomListAdapter(Activity activity, Class<T> clss)
    {
        this.activity = activity;
        this.clss = clss;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Activity getActivity()
    {
        return activity;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    public boolean isNotifiable()
    {
        synchronized(LOCK)
        {
            return isNotifiable;
        }
    }

    public void setNotifiable(boolean notifiable)
    {
        synchronized(LOCK)
        {
            isNotifiable = notifiable;
        }
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface Adapter ---------------------

    @Override
    public Object getItem(int position)
    {
        return array[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return ((T) getItem(position)).getView(activity, activity.getLayoutInflater(), convertView);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void notifyDataSetChanged()
    {
        sort();
        array = Iterables.toArray(list, clss);
        count = array.length;
        super.notifyDataSetChanged();
    }

    // -------------------------- OTHER METHODS --------------------------

    public void addToBottom(T... items)
    {
        synchronized(LOCK)
        {
            List<T> buffer = Arrays.asList(items);
            for(T item : buffer)
            {
                if(list.contains(item))
                {
                    list.remove(item);
                }
                list.add(item);
            }
        }
    }

    public void addToTop(T... items)
    {
        synchronized(LOCK)
        {
            List<T> buffer = Arrays.asList(items);
            Collections.reverse(buffer);
            for(T item : buffer)
            {
                if(list.contains(item))
                {
                    list.remove(item);
                }
                list.add(0, item);
            }
        }
    }

    public void clear()
    {
        synchronized(LOCK)
        {
            list.clear();
        }
    }

    public T removeItem(int position)
    {
        synchronized(LOCK)
        {
            return list.remove(position);
        }
    }

    public boolean removeItem(T item)
    {
        synchronized(LOCK)
        {
            return list.remove(item);
        }
    }

    public void sort()
    {
    }

    public void update()
    {
        if(isNotifiable)
        {
            updateForce();
        }
    }

    public void updateForce()
    {
        synchronized(LOCK)
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    notifyDataSetChanged();
                }
            }.post();
        }
    }
}
