package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.EventViewFactory;
import android.app.Dialog;
import android.view.View;

public class EventMenuAdapter extends DialogAdapter
{
	
	private EventModel model;
	
	public EventMenuAdapter(EventHandlerActivity activity, EventModel model)
	{
		super(activity);
		this.model = model;
	}

	@Override
	public Dialog createMenuDialog()
	{
		View viewEvent = EventViewFactory.getView(activity, model);
		
		return super.createMenuDialog(viewEvent);
	}

}
