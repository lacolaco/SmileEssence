package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.listener.EventOnClickListener;
import net.miz_hi.smileessence.util.CustomListAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class HistoryListAdapter extends CustomListAdapter<EventModel>
{
	
	public HistoryListAdapter(Activity activity)
	{
		super(activity, 1000);
	}	

	@Override
	public void addFirst(final EventModel model)
	{
		Notifier.buildEvent(model).raise();		
		HistoryListAdapter.super.addFirst(model);
	}

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		if(convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.event_layout, null);
		}
		EventModel model = (EventModel) getItem(position);
		convertedView = EventViewFactory.getView(model, convertedView);
		convertedView.setOnClickListener(new EventOnClickListener(getActivity(), model));
		
		return convertedView;
	}

}
