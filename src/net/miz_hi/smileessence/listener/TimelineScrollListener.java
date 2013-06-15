package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.util.CustomListAdapter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class TimelineScrollListener implements OnScrollListener
{

	private CustomListAdapter<?> adapter;

	public TimelineScrollListener(CustomListAdapter<?> adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		adapter.setCanNotifyOnChange(false);
		
		if (view.getFirstVisiblePosition() == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 0)
		{
			if(scrollState == SCROLL_STATE_IDLE)
			{
				adapter.setCanNotifyOnChange(true);
				int before = adapter.getCount();
				adapter.notifyDataSetChanged();
				int after = adapter.getCount();
				int addCount = after - before;
				((ListView)view).setSelectionFromTop(addCount, 0);
				if(addCount > 0)
				{
					Notifier.info(addCount + "件の新着ツイートがあります");
				}
			}
		}
	}

}
