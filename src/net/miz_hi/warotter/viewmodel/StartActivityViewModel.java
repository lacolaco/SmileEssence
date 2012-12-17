package net.miz_hi.warotter.viewmodel;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import gueei.binding.Command;
import gueei.binding.labs.EventAggregator;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.EventBindingActivity;
import net.miz_hi.warotter.core.StartActivityMessage;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.util.ActivityCallback;
import net.miz_hi.warotter.util.EnumPreferenceKey;
import net.miz_hi.warotter.util.EnumRequestCode;
import net.miz_hi.warotter.view.WebViewActivity;

public class StartActivityViewModel extends ViewModel
{
	public StringObservable text = new StringObservable();
	public StringObservable buttonAuth = new StringObservable("認証ページへ");
	public BooleanObservable buttonAuthVisibility = new BooleanObservable();
	public RequestToken req;
	public Twitter twitter;
	private Boolean isAuthed;
	
	public StartActivityViewModel()
	{
	}
	
	@Override
	public void onActivityCreated()
	{
		initialize();
	}
	
	@Override
	public void onActivityResumed()
	{
		if(isAuthed)
		{
			moveToTimeline();
		}
	}
	
	public void initialize()
	{
		buttonAuthVisibility.set(false);
		isAuthed = (Boolean) Warotter.getWarotter().getPreferenceValue(EnumPreferenceKey.AUTHORIZED);
		if(isAuthed != null && isAuthed.booleanValue())
		{
			text.set(String.format("%1$sでログインします",(String)Warotter.getWarotter().getPreferenceValue(EnumPreferenceKey.SCREEN_NAME)));
		}
		else
		{
			isAuthed = false;
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
				eventAggregator.publish("toast", new ToastMessage("move!!"), null);				
			}
		}, 1000);
		
	}
	
	public Command authorize = new Command()
	{
		@Override
		public void Invoke(View arg0, Object... arg1)
		{			
			try
			{
				twitter = TwitterFactory.getSingleton();
				twitter.setOAuthConsumer(Warotter.CONSUMER_KEY, Warotter.CONSUMER_SECRET);
				req = twitter.getOAuthRequestToken(Warotter.CALLBACK_OAUTH);
				Intent intent = new Intent();
				intent.setData(Uri.parse(req.getAuthenticationURL()));
				eventAggregator.publish("startActivity", new StartActivityMessage(intent, WebViewActivity.class, EnumRequestCode.AUTHORIZE.ordinal()
						, new ActivityCallback()
				{
					
					@Override
					public void run(int result, Intent data)
					{
						if(result == Activity.RESULT_OK && data != null)
						{
							try
							{
								Uri uri = data.getData();
								String verifier = uri.getQueryParameter(Warotter.OAUTH_VERIFIER);
								AccessToken accessToken = null;
								accessToken = twitter.getOAuthAccessToken(req, verifier);
								if(accessToken != null)
								{
									Warotter app = Warotter.getWarotter();
									app.putPreferenceValue(EnumPreferenceKey.TOKEN, accessToken.getToken());
									app.putPreferenceValue(EnumPreferenceKey.TOKEN_SECRET, accessToken.getTokenSecret());
									app.putPreferenceValue(EnumPreferenceKey.SCREEN_NAME, accessToken.getScreenName());
									app.putPreferenceValue(EnumPreferenceKey.AUTHORIZED, true);		
									eventAggregator.publish("toast", new ToastMessage("認証成功しました"), null);
								}
								else
								{
									throw new NullPointerException();
								}
							}
							catch (Exception e) 
							{
								e.printStackTrace();
								eventAggregator.publish("toast", new ToastMessage("認証失敗しました"), null);
							}
						}
						initialize();						
					}
				}), null);
				
			}
			catch (TwitterException e)
			{
				e.printStackTrace();
			}
			
		}
	};
}
