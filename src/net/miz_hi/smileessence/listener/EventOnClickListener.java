package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.menu.EventMenu;
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
		v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
		v.invalidate();
		new UiHandler()
		{
			@Override
			public void run()
			{
				v.setBackgroundColor(ColorUtils.setAlpha(Client.getColor(R.color.LightGray), 200));
				new EventMenu(activity, model).create().show();
			}
		}.postDelayed(50);
	}

}
