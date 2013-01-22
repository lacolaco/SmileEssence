package net.miz_hi.smileessence.viewmodel;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivityViewModel extends ViewModel
{
	private Handler handler;
	private AuthorizeHelper helper;

	public StartActivityViewModel()
	{
		handler = new Handler();
	}
	
	@Override
	public void onActivityCreated(EventHandlerActivity activity)
	{
		initialize(activity);
	}

	@Override
	public void onActivityResumed(EventHandlerActivity activity)
	{
		if (isAuthed())
		{
			moveToTimeline(activity);
		}
	}
	
	private boolean isAuthed()
	{
		Long lastUsedId = (Long) Client.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);
		return lastUsedId > 0 && !AuthentificationDB.instance().findAll().isEmpty();
	}

	public void initialize(Activity activity)
	{
		Button buttonAuth = (Button)activity.findViewById(R.id.button_Auth);
		TextView textNavi = (TextView)activity.findViewById(R.id.text_navigate);
		buttonAuth.setText("認証ページヘ");
		buttonAuth.setVisibility(View.INVISIBLE);
		if (isAuthed())
		{
			long lastUsedId = (Long) Client.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);
			for (Account account : AuthentificationDB.instance().findAll())
			{
				if (account.getUserId() == lastUsedId)
				{
					MainActivityViewModel.singleton().account = account;
					textNavi.setText(String.format("%sでログインします", account.getScreenName()));
					moveToTimeline(activity);
					break;
				}
			}
		}
		else
		{
			textNavi.setText("認証してください");
			buttonAuth.setVisibility(View.VISIBLE);
		}
	}

	public void moveToTimeline(final Activity activity)
	{	
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{		
				activity.startActivity(new Intent(activity, MainActivity.class));
				activity.finish();
			}
		}, 1000);

	}

	@Override
	public void onActivityResult(EventHandlerActivity activity, int reqCode, int resultCode, Intent data)
	{
		if (reqCode == EnumRequestCode.AUTHORIZE.ordinal() && resultCode == Activity.RESULT_OK)
		{
			Uri uri = data.getData();
			Account account = helper.oauthRecieve(uri);
			if (account != null)
			{
				Client.setMainAccount(account);
				toast("認証成功しました");
			}
			else
			{
				toast("認証失敗しました");
			}
		}
		initialize(activity);
	}

	public void authorize(EventHandlerActivity activity)
	{
		helper = new AuthorizeHelper(activity, Consumers.getDedault());
		helper.oauthSend();
	}

	@Override
	public void onActivityDestroy(EventHandlerActivity activity)
	{		
	}

	@Override
	public boolean onEvent(String eventName, EventHandlerActivity activity)
	{
		if(eventName.equals("authorize"))
		{
			authorize(activity);
			return true;
		}
		return false;
	};
}
