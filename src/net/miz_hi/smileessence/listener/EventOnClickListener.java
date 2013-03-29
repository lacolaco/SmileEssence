package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.event.EventCommandDialog;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.util.ColorUtils;
import net.miz_hi.smileessence.util.UiHandler;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class EventOnClickListener implements OnClickListener
{
	private Activity activity;
	private EventModel model;

	public EventOnClickListener(Activity _activity, EventModel model)
	{
		this.activity = _activity;
		this.model = model;
	}

	@Override
	public void onClick(final View v)
	{
		final EventCommandDialog adapter = new EventCommandDialog(activity, model);
		v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
		v.invalidate();
		new UiHandler()
		{
			@Override
			public void run()
			{
				v.setBackgroundColor(ColorUtils.setAlpha(Client.getColor(R.color.LightGray), 200));
				adapter.createMenuDialog(true).show();
			}
		}.postDelayed(50);
	}

}
