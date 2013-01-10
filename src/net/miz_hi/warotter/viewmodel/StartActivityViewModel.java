package net.miz_hi.warotter.viewmodel;

import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.core.ActivityCallback;
import net.miz_hi.warotter.core.StartActivityMessage;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.model.AuthentificationDB;
import net.miz_hi.warotter.model.Consumers;
import net.miz_hi.warotter.model.Consumers.Consumer;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.util.AuthorizeHelper;
import net.miz_hi.warotter.util.EnumPreferenceKey;
import net.miz_hi.warotter.util.EnumRequestCode;
import net.miz_hi.warotter.view.MainActivity;
import net.miz_hi.warotter.view.StartActivity;
import net.miz_hi.warotter.view.WebViewActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

public class StartActivityViewModel extends ViewModel
{
	public StringObservable text = new StringObservable();
	public StringObservable buttonAuth = new StringObservable("認証ページへ");
	public BooleanObservable textVisibility = new BooleanObservable(true);
	public BooleanObservable buttonAuthVisibility = new BooleanObservable();
	private Boolean isAuthed;
	private Handler handler;
	private AuthorizeHelper helper;

	public StartActivityViewModel(Activity activity)
	{
		super(activity);
		initialize();
		handler = new Handler();
	}

	@Override
	public void onActivityResumed()
	{
		if (isAuthed)
		{
			moveToTimeline();
		}
	}

	public void initialize()
	{
		buttonAuthVisibility.set(false);
		long lastUsedId = (Long)Warotter.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);
		isAuthed = lastUsedId > 0 && !AuthentificationDB.instance().findAll().isEmpty();
		if (isAuthed)
		{
			for(Account account : AuthentificationDB.instance().findAll())
			{
				if(account.getUserId() == lastUsedId)
				{
					Warotter.setMainAccount(account);
					text.set(String.format("%1$sでログインします", account.getScreenName()));
					moveToTimeline();
					break;
				}
			}
		}
		else
		{
			text.set("認証してください");
			buttonAuthVisibility.set(true);
		}
	}

	public void moveToTimeline()
	{
		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				textVisibility.set(false);
				activity.startActivity(new Intent(activity, MainActivity.class));
				activity.finish();
			}
		}, 1200);

	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		if (reqCode == EnumRequestCode.AUTHORIZE.ordinal() && resultCode == Activity.RESULT_OK )
		{
			Uri uri = data.getData();
			Account account = helper.oauthRecieve(uri);
			if(account != null)
			{
				Warotter.setMainAccount(account);
				toast("認証成功しました");
			}	
			else
			{
				toast("認証失敗しました");
			}
		}
		initialize();
	}


	public Command authorize = new Command()
	{
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			helper = new AuthorizeHelper(activity, Consumers.consumersMap.get("miz_hi"));
			helper.oauthSend();
		}
	};
}
