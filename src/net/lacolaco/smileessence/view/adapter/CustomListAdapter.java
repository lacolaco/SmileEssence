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
import net.lacolaco.smileessence.viewmodel.IViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomListAdapter<T extends IViewModel> extends BaseAdapter
{

    protected final Object LOCK = new Object();
    protected Class<T> clss;
    protected ArrayList<T> list = new ArrayList<>();
    protected T[] array;
    protected boolean isNotifiable = true;
    protected Activity activity;

    public CustomListAdapter(Activity activity, Class<T> clss)
    {
        this.activity = activity;
        this.clss = clss;
    }

    public Activity getActivity()
    {
        return activity;
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

    public void addToBottom(T... items)
    {
        synchronized(LOCK)
        {
            for(T item : items)
            {
                list.add(item);
            }
            update();
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
                list.add(0, item);
            }
            update();
        }
    }

    public T removeItem(int position)
    {
        synchronized(LOCK)
        {
            T removed = list.remove(position);
            if(removed != null)
            {
                update();
            }
            return removed;
        }
    }

    public boolean removeItem(T item)
    {
        synchronized(LOCK)
        {
            if(list.remove(item))
            {
                update();
            }
            return true;
        }
    }

    public void update()
    {
        if(isNotifiable)
        {
            notifyDataSetChanged();
        }
    }

    @Override
    public void notifyDataSetChanged()
    {
        array = Iterables.toArray(list, clss);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        if(array == null)
        {
            return 0;
        }
        return array.length;
    }

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
        View view = ((T)getItem(position)).getView(activity, activity.getLayoutInflater(), convertView);
        return view;
    }
}
