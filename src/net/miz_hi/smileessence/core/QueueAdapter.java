package net.miz_hi.smileessence.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class QueueAdapter<T extends Comparable<T>> extends BaseAdapter
{
	
	private List<T> list;
	private int count;
	private final Object lock = new Object();
	private boolean notifyOnChange = true;
	private EventHandlerActivity activity;
	private LayoutInflater inflater;
	
	public QueueAdapter(EventHandlerActivity activity)
	{
		this.list = new ArrayList<T>();
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
	}
	
	public void setList(List<T> list)
	{
		this.list.clear();
		this.list.addAll(list);
	}

	public void addFirst(T status)
	{
		synchronized (lock)
		{
			list.add(0, status);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void addLast(T status)
	{
		synchronized (lock)
		{
			list.add(status);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void addAllFirst(List<T> status)
	{
		synchronized (lock)
		{
			list.addAll(0, status);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void addAllLast(List<T> status)
	{
		synchronized (lock)
		{
			list.addAll(status);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void insert(T status, int index)
	{
		synchronized (lock)
		{
			list.add(index, status);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void removeLast()
	{
		synchronized (lock)
		{
			list.remove(list.size() - 1);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void remove(T status)
	{
		synchronized (lock)
		{
			list.remove(status);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void clear()
	{
		synchronized (lock)
		{
			list.clear();
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
	public void sort()
	{
		synchronized (lock)
		{
			Collections.sort(list);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}
	
    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        count = list.size();
        notifyOnChange = true;
    }    
    
    public void setNotifyOnChange(boolean notifyOnChange)
    {
        this.notifyOnChange = notifyOnChange;
    }
    
    public EventHandlerActivity getActivity() 
    {
        return activity;
    }
	
	@Override
	public int getCount()
	{		
		return count;
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	public LayoutInflater getInflater()
	{
		return inflater;
	}

	@Override
	public abstract View getView(int position, View convertedView, ViewGroup parent);


}
