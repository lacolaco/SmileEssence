package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.core.CustomListAdapter;
import net.miz_hi.smileessence.util.LogHelper;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class TimelineScrollListener implements OnScrollListener
{

	private CustomListAdapter adapter;

	public TimelineScrollListener(CustomListAdapter adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		//adapter.setCanNotifyOnChange(false);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if(scrollState == SCROLL_STATE_IDLE)
		{
			adapter.setCanNotifyOnChange(true);
		}
		else
		{
			adapter.setCanNotifyOnChange(false);
		}
		
		if (view.getFirstVisiblePosition() == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 0)
		{
			adapter.notifyAdapter();
		}
	}

}
