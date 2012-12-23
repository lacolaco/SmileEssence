package net.miz_hi.warotter.util;

import net.miz_hi.warotter.model.EventModel;
import android.os.AsyncTask;

public class EventAdjuster extends AsyncTask<EventModel, Integer, EventModel>
{

	@Override
	protected EventModel doInBackground(EventModel... params)
	{
		EventModel event = params[0];
		return event;
	}

}
