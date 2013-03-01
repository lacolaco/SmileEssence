package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.CustomListAdapter;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.listener.EventOnClickListener;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryListAdapter extends CustomListAdapter<EventModel>
{
	
	private ToastManager manager;

	public HistoryListAdapter(Activity activity)
	{
		super(activity, 1000);
		manager = new ToastManager(activity);
	}	
	
	public void notice(EventModel model)
	{
		manager.noticeEvent(model);
	}

	@Override
	public void addFirst(final EventModel model)
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				manager.noticeEvent(model);		

				ListView historyListView = MainActivity.getInstance().getHistoryListView();

				HistoryListAdapter.super.addFirst(model);

				if (historyListView.getFirstVisiblePosition() == 0 && historyListView.getChildAt(0) != null && historyListView.getChildAt(0).getTop() == 0)
				{
					setCanNotifyOnChange(true);
				}
				else if(historyListView.getChildCount() == 0)
				{
					setCanNotifyOnChange(true);
				}					
				else
				{
					setCanNotifyOnChange(false);
				}
				notifyAdapter();
			}
		}.post();
	}

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		if(convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.eventtoast_layout, null);
		}
		EventModel model = (EventModel) getItem(position);
		convertedView = EventViewFactory.getView(getActivity(), model, convertedView);
		convertedView.setOnClickListener(new EventOnClickListener(getActivity(), model));
		
		return convertedView;
	}

}
