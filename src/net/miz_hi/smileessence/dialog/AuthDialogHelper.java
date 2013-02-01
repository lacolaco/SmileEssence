package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.auth.Consumers.Consumer;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class AuthDialogHelper
{
	private Activity _activity;
	private Consumer _consumer;
	private Runnable _onComplete;

	public AuthDialogHelper(Activity activity)
	{
		_activity = activity;
		_consumer = Consumers.getDedault();
	}
	
	public void setConsumer(Consumer consumer)
	{
		_consumer = consumer;
	}

	public Dialog getAuthDialog()
	{

		TextView titleView = new TextView(_activity);
		titleView.setText("認証してください");
		titleView.setTextColor(Client.getColor(R.color.White));
		titleView.setPadding(10, 20, 0, 20);
		Button authButton = new Button(_activity);
		authButton.setText("認証ページへ");
		authButton.setGravity(Gravity.CENTER);
		authButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(_onComplete != null)
				{
					_onComplete.run();				
				}
			}
		});
		return SimpleDialogHelper.createDialog(_activity, titleView, authButton);
	}
	
	public void setOnComplete(Runnable runnable)
	{
		_onComplete = runnable;
	}
	
}
