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
import net.miz_hi.warotter.util.EnumPreferenceKey;
import net.miz_hi.warotter.util.EnumRequestCode;
import net.miz_hi.warotter.view.MainActivity;
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
	public RequestToken req;
	public Twitter twitter;
	private Boolean isAuthed;
	private Handler handler;

	public StartActivityViewModel()
	{
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Warotter.CONSUMER_KEY, Warotter.CONSUMER_SECRET);
		initialize();
		handler = new Handler();
	}

	@Override
	public void onActivityCreated()
	{

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
				eventAggregator.publish("startActivity", new StartActivityMessage(new Intent(), MainActivity.class, EnumRequestCode.MAIN.ordinal(), new ActivityCallback()
				{

					@Override
					public void run(int result, Intent data)
					{
						eventAggregator.publish("finish", null, null);
					}
				}), null);
				textVisibility.set(false);
			}
		}, 1200);

	}

	public Command authorize = new Command()
	{
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{

						req = twitter.getOAuthRequestToken(Warotter.CALLBACK_OAUTH);
						Intent intent = new Intent();
						intent.setData(Uri.parse(req.getAuthenticationURL()));
						eventAggregator.publish("startActivity", new StartActivityMessage(intent, WebViewActivity.class, EnumRequestCode.AUTHORIZE.ordinal(), new ActivityCallback()
						{
							@Override
							public void run(final int result, final Intent data)
							{
								new Thread(new Runnable()
								{

									@Override
									public void run()
									{
										if (result == Activity.RESULT_OK && data != null)
										{
											try
											{
												Uri uri = data.getData();
												String verifier = uri.getQueryParameter(Warotter.OAUTH_VERIFIER);
												AccessToken accessToken = null;
												accessToken = twitter.getOAuthAccessToken(req, verifier);
												if (accessToken != null)
												{													
													Consumer consumer;
													if(accessToken.getScreenName().equals("miz_hi"))
													{
														consumer = Consumers.consumersMap.get("miz_hi");
													}
													else
													{
														consumer = Consumers.getDedault();
													}
													Account account = new Account(accessToken, consumer);
													AuthentificationDB db = AuthentificationDB.instance();
													db.save(account);
													Warotter.setMainAccount(account);
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
											handler.post(new Runnable()
											{

												@Override
												public void run()
												{
													initialize();
												}
											});
										}
									}
								}).start();
							}
						}), null);

					}
					catch (TwitterException e)
					{
						e.printStackTrace();
					}
				}
			}).start();

		}
	};
}
