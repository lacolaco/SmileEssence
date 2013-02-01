package net.miz_hi.smileessence.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CustomListAdapter<T extends Comparable<T>> extends BaseAdapter
{

	private volatile ArrayList<T> _array;
	private volatile int _count;
	private volatile Object _lock = new Object();
	private volatile boolean _notifyOnChange = true;
	private Activity _activity;
	private LayoutInflater _inflater;
	private int _capacity;

	public CustomListAdapter(Activity activity, int capacity)
	{
		this._capacity = capacity;
		this._array = new ArrayList<T>(capacity);
		this._activity = activity;
		this._inflater = LayoutInflater.from(activity);
	}

	public void setList(List<T> list)
	{
		synchronized (_lock)
		{
			this._array.clear();
			this._array.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void addFirst(T status)
	{
		synchronized (_lock)
		{
			_array.add(0, status);
			if(_array.size() >= _capacity)
			{
				_array.remove(_array.size() - 1);
			}
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void addLast(T status)
	{
		synchronized (_lock)
		{
			_array.add(status);
			if(_array.size() >= _capacity)
			{
				_array.remove(_array.size() - 1);
			}
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void addAllFirst(List<T> statuses)
	{
		synchronized (_lock)
		{
			_array.addAll(0, statuses);
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void addAllLast(List<T> statuses)
	{
		synchronized (_lock)
		{			
			_array.addAll(statuses);
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void insert(T status, int index)
	{
		synchronized (_lock)
		{
			_array.add(index, status);
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void removeLast()
	{
		synchronized (_lock)
		{
			_array.remove(_array.size() - 1);
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void remove(T status)
	{
		synchronized (_lock)
		{
			_array.remove(status);
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void clear()
	{
		synchronized (_lock)
		{
			_array.clear();
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	public void sort()
	{
		synchronized (_lock)
		{
			Collections.sort(_array);
		}
		if (_notifyOnChange)
		{
			notifyDataSetChanged();
		}
	}

	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
		synchronized (_lock)
		{
			_count = _array.size();
			_notifyOnChange = true;
		}
	}    

	public void setNotifyOnChange(boolean notifyOnChange)
	{
		synchronized (_lock)
		{
			this._notifyOnChange = notifyOnChange;
		}
	}

	public Activity getActivity() 
	{
		return _activity;
	}

	@Override
	public int getCount()
	{		
		synchronized (_lock)
		{
			return _count;
		}
	}

	@Override
	public Object getItem(int position)
	{
		synchronized (_lock)
		{
			return _array.get(position);
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public LayoutInflater getInflater()
	{
		return _inflater;
	}

	@Override
	public abstract View getView(int position, View convertedView, ViewGroup parent);


}
