package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.StatusMenuAdapter;
import net.miz_hi.smileessence.model.Client;
import net.miz_hi.smileessence.model.StatusModel;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class StatusOnClickListener implements OnClickListener
{
	private EventHandlerActivity activity;
	private StatusModel model;
	
	public StatusOnClickListener(EventHandlerActivity activity, StatusModel model)
	{
		this.activity = activity;
		this.model = model;
	}
	
	
	@Override
	public void onClick(final View v)
	{
		final StatusMenuAdapter adapter = new StatusMenuAdapter(activity, model);
		v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
		v.invalidate();
		Handler hanlder = new Handler();
		hanlder.postDelayed(new Runnable()
		{
			public void run()
			{
				v.setBackgroundColor(model.backgroundColor);
				adapter.createMenuDialog().show();
			}
		}, 50);
	}

}
