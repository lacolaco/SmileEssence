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

	private volatile List<T> list;
	private volatile int count;
	private volatile Object lock = new Object();
	private volatile boolean notifyOnChange = true;
	private EventHandlerActivity activity;
	private LayoutInflater inflater;
	private int capacity;

	public QueueAdapter(EventHandlerActivity activity, int capacity)
	{
		this.capacity = capacity;
		this.list = new ArrayList<T>(capacity);
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
	}

	public void setList(List<T> list)
	{
		synchronized (lock)
		{
			this.list.clear();
			this.list.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void addFirst(T status)
	{
		synchronized (lock)
		{
			list.add(0, status);
			if(list.size() >= capacity)
			{
				list.remove(list.size() - 1);
			}
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
			if(list.size() >= capacity)
			{
				list.remove(list.size() - 1);
			}
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void addAllFirst(List<T> statuses)
	{
		synchronized (lock)
		{
			list.addAll(0, statuses);
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void addAllLast(List<T> statuses)
	{
		synchronized (lock)
		{			
			list.addAll(statuses);
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
		synchronized (lock)
		{
			this.notifyOnChange = notifyOnChange;
		}
	}

	public EventHandlerActivity getActivity() 
	{
		return activity;
	}

	@Override
	public int getCount()
	{		
		synchronized (lock)
		{
			return count;
		}
	}

	@Override
	public Object getItem(int position)
	{
		synchronized (lock)
		{
			return list.get(position);
		}
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
