package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.dialog.EventMenuAdapter;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.util.ColorUtils;
import android.app.Activity;
import android.os.Handler;
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
		final EventMenuAdapter adapter = new EventMenuAdapter(activity, model);
		v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
		v.invalidate();
		Handler hanlder = new Handler();
		hanlder.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				v.setBackgroundColor(ColorUtils.setAlpha(Client.getResource().getColor(R.color.LightGray), 200));
				adapter.createMenuDialog(true).show();
			}
		}, 50);
	}

}
