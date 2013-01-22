package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.StatusMenuAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class StatusOnClickListener implements OnClickListener
{
	private EventHandlerActivity activity;
	private StatusModel model;
	private static boolean wasTouched = false;
	
	public StatusOnClickListener(EventHandlerActivity activity, StatusModel model)
	{
		this.activity = activity;
		this.model = model;
	}	
	
	@Override
	public void onClick(final View v)
	{
		final StatusMenuAdapter adapter = new StatusMenuAdapter(activity, model);
		Handler handler = new Handler();
		v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
		v.invalidate();
		handler.postDelayed(new Runnable()
		{
			public void run()
			{
				v.setBackgroundColor(model.backgroundColor);
				adapter.createMenuDialog(true).show();
			}
		}, 50);
	}

}
