package net.miz_hi.smileessence.view;

import java.util.List;

import net.miz_hi.smileessence.util.UiHandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListPagerAdapter extends FragmentPagerAdapter
{

	private Fragment[] pageArray;
	private List<Fragment> pageList;
	private boolean canMotifyOnChange = true;
	
	public ListPagerAdapter(FragmentManager fm, Fragment... fragments)
	{
		super(fm);
		pageArray = fragments;
	}
	
	@Override
	public int getCount()
	{
		return pageArray.length;
	}
	
	public synchronized void add(Fragment element)
	{
		if (pageList.contains(element))
		{
			pageList.remove(element);
		}
		pageList.add(element);
		notifyAdapter();
	}
	
	public synchronized void remove(Fragment element)
	{
		pageList.remove(element);
		notifyAdapter();
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
					pageArray = (Fragment[]) pageList.toArray();
					notifyDataSetChanged();
				}
			}.post();
		}
	}

	public synchronized void forceNotifyAdapter()
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

	public synchronized void setCanNotifyOnChange(boolean notifyOnChange)
	{
		canMotifyOnChange = notifyOnChange;
	}

	public synchronized boolean getCanNotifyOnChange()
	{
		return canMotifyOnChange;
	}

	@Override
	public Fragment getItem(int position)
	{
		if(pageArray != null && pageArray.length >= position)
		{
			return pageArray[position];
		}
		else
		{
			return null;
		}
	}

	@Override
	public int getItemPosition(Object object)
	{
		return super.POSITION_NONE;
	}
}
