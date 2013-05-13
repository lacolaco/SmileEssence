package net.miz_hi.smileessence.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.miz_hi.smileessence.view.IRemovable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NamedFragmentPagerAdapter extends FragmentStatePagerAdapter
{

	private NamedFragment[] pageArray;
	private int count;
	private ArrayList<NamedFragment> pageList = new ArrayList<NamedFragment>();
	private boolean canMotifyOnChange = true;
		
	public NamedFragmentPagerAdapter(FragmentManager fm, Collection<NamedFragment> fragments)
	{
		super(fm);
		pageList.addAll(fragments);
		forceNotifyAdapter();
	}
	
	public NamedFragmentPagerAdapter(FragmentManager fm)
	{
		super(fm);
		forceNotifyAdapter();
	}
	
	@Override
	public synchronized CharSequence getPageTitle(int position)
	{
		return pageArray[position].getTitle();
	}

	@Override
	public synchronized int getCount()
	{
		return count;
	}
	
	public NamedFragment[] getList()
	{
		forceNotifyAdapter();
		return Arrays.copyOf(pageArray, pageArray.length);
	}
	
	public synchronized int add(NamedFragment element)
	{
		if (pageList.contains(element))
		{
			pageList.remove(element);
		}
		pageList.add(element);
		notifyAdapter();
		return pageList.indexOf(element);
	}
	
	public synchronized void add(NamedFragment element, int index)
	{
		if (pageList.contains(element))
		{
			pageList.remove(element);
		}
		pageList.add(index, element);
		notifyAdapter();
	}
	
	public synchronized void remove(NamedFragment element)
	{
		pageList.remove(element);
		if(element instanceof IRemovable)
		{
			((IRemovable)element).onRemoved();
		}
		notifyAdapter();
	}
	
	public synchronized void remove(int i)
	{
		NamedFragment fragment = pageList.remove(i);
		remove(fragment);
	}

	public synchronized void notifyAdapter()
	{
		if (canMotifyOnChange)
		{
			forceNotifyAdapter();
		}
	}

	public synchronized void forceNotifyAdapter()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				pageArray = (NamedFragment[]) pageList.toArray(new NamedFragment[]{});
				count = pageArray.length;
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
		int index = -1;
		for(int i = 0;i < pageArray.length; i++)
		{
			Object element = pageArray[i];
			if(element == object)
			{
				index = i;
				break;
			}
		}
		return index != -1 ? index : POSITION_NONE;
	}

}
