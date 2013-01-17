package net.miz_hi.warotter.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.model.StatusModel;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class CustomListAdapter<T extends Comparable<T>> extends BaseAdapter
{
	
	private List<T> list;
	private final Object lock = new Object();
	private boolean notifyOnChange = true;
	private Activity activity;
	private LayoutInflater inflater;
	
	public CustomListAdapter(Activity activity)
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

	public void add(T status)
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
	
	public void addAll(List<T> status)
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
        notifyOnChange = true;
    }    
    
    public void setNotifyOnChange(boolean notifyOnChange)
    {
        this.notifyOnChange = notifyOnChange;
    }
    
    public Activity getActivity() 
    {
        return activity;
    }
	
	@Override
	public int getCount()
	{		
		return list.size();
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
