package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.auth.Consumers.Consumer;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AuthDialogHelper
{
	private Activity activity;
	private Consumer consumer;
	private Runnable onClick;

	public AuthDialogHelper(Activity activity)
	{
		this.activity = activity;
		this.consumer = Consumers.getDedault();
	}

	public void setConsumer(Consumer consumer)
	{
		this.consumer = consumer;
	}

	public Dialog getAuthDialog()
	{

		TextView titleView = new TextView(activity);
		titleView.setText("認証してください");
		titleView.setTextColor(Client.getColor(R.color.White));
		titleView.setPadding(10, 20, 0, 20);
		Button authButton = new Button(activity);
		authButton.setText("認証ページへ");
		authButton.setGravity(Gravity.CENTER);
		authButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (onClick != null)
				{
					onClick.run();
				}
			}
		});
		return SimpleDialogHelper.createDialog(activity, titleView, authButton);
	}

	public void setOnComplete(Runnable runnable)
	{
		onClick = runnable;
	}

}
