package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListPagerAdapter extends FragmentPagerAdapter
{

	private static int NUM_OF_VIEWS = 3;
	private Fragment[] pagesMap;
	
	public ListPagerAdapter(FragmentManager fm, Fragment... fragments)
	{
		super(fm);
		pagesMap = fragments;
	}
	
	@Override
	public int getCount()
	{
		return NUM_OF_VIEWS;
	}
	
	@Override
	public Fragment getItem(int arg0)
	{
		return pagesMap[arg0];
	}

	@Override
	public int getItemPosition(Object object)
	{
		return super.POSITION_NONE;
	}
}
