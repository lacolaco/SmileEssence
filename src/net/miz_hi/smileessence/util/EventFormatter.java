package net.miz_hi.smileessence.util;

import net.miz_hi.smileessence.model.EventModel;
import android.os.AsyncTask;

public class EventFormatter extends AsyncTask<EventModel, Integer, EventModel>
{

	@Override
	protected EventModel doInBackground(EventModel... params)
	{
		EventModel event = params[0];
		return event;
	}

}
