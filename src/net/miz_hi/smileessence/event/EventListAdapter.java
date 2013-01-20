package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.QueueAdapter;
import android.view.View;
import android.view.ViewGroup;

public class EventListAdapter extends QueueAdapter<EventModel>
{

	public EventListAdapter(EventHandlerActivity activity)
	{
		super(activity, 1000);
	}

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
