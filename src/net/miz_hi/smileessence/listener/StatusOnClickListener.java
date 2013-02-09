package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.StatusMenuAdapter;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class StatusOnClickListener implements OnClickListener
{
	private Activity activity;
	private StatusModel model;
	private static boolean wasTouched = false;

	public StatusOnClickListener(Activity activity, StatusModel model)
	{
		this.activity = activity;
		this.model = model;
	}

	@Override
	public void onClick(final View v)
	{
		final StatusMenuAdapter adapter = new StatusMenuAdapter(activity, model);
		new UiHandler()
		{

			@Override
			public void run()
			{
				v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
				v.invalidate();
				new UiHandler()
				{

					@Override
					public void run()
					{
						v.setBackgroundColor(model.backgroundColor);
						adapter.createMenuDialog(true).show();
					}
				}.postDelayed(50);

			}
		}.post();
	}

}
