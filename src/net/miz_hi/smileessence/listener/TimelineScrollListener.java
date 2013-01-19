package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.core.QueueAdapter;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class TimelineScrollListener implements OnScrollListener
{
	
	private QueueAdapter adapter;
	
	public TimelineScrollListener(QueueAdapter adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (view.getFirstVisiblePosition() == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 0)
		{
			adapter.setNotifyOnChange(true);
		}
		else if (view.getChildCount() == 0)
		{
			adapter.setNotifyOnChange(true);
		}
		else
		{
			adapter.setNotifyOnChange(false);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (view.getFirstVisiblePosition() == 0 && view.getChildAt(0).getTop() == 0)
		{
			adapter.setNotifyOnChange(true);
		}
		adapter.notifyDataSetChanged();
	}

}
