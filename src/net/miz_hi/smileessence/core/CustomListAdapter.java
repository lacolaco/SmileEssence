package net.miz_hi.smileessence.core;

import java.util.ArrayList;
import java.util.Collection;

import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CustomListAdapter<T> extends BaseAdapter
{

	private T[] array;
	private ArrayList<T> list;
	private int count;
	private final Object lock = new Object();
	private boolean canMotifyOnChange = true;
	private Activity activity;
	private LayoutInflater inflater;
	private int capacity;

	public CustomListAdapter(Activity activity, int capacity)
	{
		this.capacity = capacity;
		this.list = new ArrayList<T>(capacity);
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
	}

	public void addAll(Collection<T> collection)
	{
		synchronized (lock)
		{
			for (T element : collection)
			{
				if (list.contains(element))
				{
					list.remove(element);
				}
				list.add(element);
				if (list.size() >= capacity)
				{
					break;
				}
			}
		}
	}

	public void addFirst(T element)
	{
		synchronized (lock)
		{
			if (list.contains(element))
			{
				list.remove(element);
			}
			list.add(0, element);

			if (list.size() >= capacity)
			{
				list.remove(list.size() - 1);
			}
		}
	}

	public void removeElement(T element)
	{
		synchronized (lock)
		{
			list.remove(element);
		}
	}

	public void notifyAdapter()
	{
		if (canMotifyOnChange)
		{
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					//setCanNotifyOnChange(false);
					notifyDataSetChanged();
				}
			}.postAtFrontOfQueue();
		}
	}

	public void forceNotifyAdapter()
	{
		synchronized (lock)
		{
			new UiHandler()
			{
				@Override
				public void run()
				{
					notifyDataSetChanged();
				}
			}.post();
		}
	}

	public void setCanNotifyOnChange(boolean notifyOnChange)
	{
		synchronized (lock)
		{
			this.canMotifyOnChange = notifyOnChange;
		}
	}
	
	public boolean getCanNotifyOnChange()
	{
		synchronized (lock)
		{
			return canMotifyOnChange;
		}
	}

	public Activity getActivity()
	{
		return activity;
	}
	

	@Override
	public void notifyDataSetChanged()
	{
		synchronized (lock)
		{
			LogHelper.print(Thread.currentThread().getName());
			CustomListAdapter.super.notifyDataSetChanged();
			array = (T[]) list.toArray();
			count = array.length;	
		}
	}

	@Override
	public int getCount()
	{
		return count;
	}

	@Override
	public Object getItem(int position)
	{
		synchronized (lock)
		{
			if(array != null && array.length >= position)
			{
				return array[position];
			}
			else
			{
				return null;
			}
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
