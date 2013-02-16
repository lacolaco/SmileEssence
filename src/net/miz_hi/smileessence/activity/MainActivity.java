package net.miz_hi.smileessence.activity;

import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncMentionsGetter;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.dialog.AuthDialogHelper;
import net.miz_hi.smileessence.dialog.OptionMenuAdapter;
import net.miz_hi.smileessence.dialog.ProgressDialogHelper;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.EventNoticer;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.listener.WarotterUserStreamListener;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.TweetViewManager;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.Paging;
import twitter4j.TwitterStream;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Runnable
{

	private static MainActivity instance;
	private static final int HANDLER_NOT_AUTHED = 0;
	private static final int HANDLER_SETUPED = 1;
	private static final int HANDLER_OAUTH_SUCCESS = 2;

	private ExtendedBoolean isHomeMode;
	private TweetViewManager tweetViewManager;
	private StatusListAdapter homeListAdapter;
	private StatusListAdapter mentionsListAdapter;
	private OptionMenuAdapter optionMenuAdapter;
	private EventNoticer eventNoticer;
	private AuthorizeHelper authHelper;
	private AuthDialogHelper authDialog;
	private TwitterStream twitterStream;
	private boolean isFirstLoad = false;
	private TextView titleView;
	private ImageView tweetImage;
	private ImageView timelineImage;
	private ImageView menuImage;
	private ListView homeListView;
	private ListView mentionsListView;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			progressDialog.dismiss();
			switch (msg.what)
			{
				case HANDLER_SETUPED:
				{
					Toast.makeText(instance, "ê⁄ë±ÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();
					break;
				}
				case HANDLER_OAUTH_SUCCESS:
				{
					new Thread(instance).start();
					break;
				}
			}
		}
	};

	@Override
	public void run()
	{
		long lastUsedId = (Long) Client.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);

		for (Account account : AuthentificationDB.instance().findAll())
		{
			if (account.getUserId() == lastUsedId)
			{
				Client.setMainAccount(account);

				WarotterUserStreamListener usListener = new WarotterUserStreamListener();
				usListener.setHomeListAdapter(homeListAdapter);
				usListener.setMentionsListAdapter(mentionsListAdapter);
				twitterStream = TwitterManager.getTwitterStream(Client.getMainAccount());
				twitterStream.addListener(usListener);
				twitterStream.user();

				Future<List<StatusModel>> f1 = MyExecutor.getExecutor().submit(new AsyncTimelineGetter(account, new Paging(1)));
				Future<List<StatusModel>> f2 = MyExecutor.getExecutor().submit(new AsyncMentionsGetter(account, new Paging(1)));
				try
				{
					final List<StatusModel> oldTimeline = f1.get();
					final List<StatusModel> oldMentions = f2.get();
					new UiHandler()
					{

						@Override
						public void run()
						{
							homeListAdapter.addAll(oldTimeline);
							homeListAdapter.forceNotifyAdapter();
							mentionsListAdapter.addAll(oldMentions);
							mentionsListAdapter.forceNotifyAdapter();
						}
					}.post();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				break;
			}
		}
		handler.sendEmptyMessage(HANDLER_SETUPED);

	}

	public static MainActivity getInstance()
	{
		return instance;
	}

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.mainactivity_layout);
		LogHelper.print("main create");
		if (instance == null)
		{
			instance = this;
			isFirstLoad = true;
		}

		homeListAdapter = new StatusListAdapter(instance);
		mentionsListAdapter = new StatusListAdapter(instance);
		optionMenuAdapter = new OptionMenuAdapter(instance, "ÉÅÉjÉÖÅ[");
		eventNoticer = new EventNoticer(instance);
		authHelper = new AuthorizeHelper(instance, Consumers.getDedault());
		tweetViewManager = new TweetViewManager(instance);
		tweetViewManager.init();

		viewSetUp();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		LogHelper.print("main resume");
		if (isFirstLoad)
		{
			isFirstLoad = false;

			if (Client.hasAuthedAccount())
			{
				progressDialog = new ProgressDialog(instance);
				ProgressDialogHelper.makeProgressDialog(instance, progressDialog).show();
				new Thread(instance).start();
			}
			else
			{
				authDialog = new AuthDialogHelper(instance);
				final Dialog dialog = authDialog.getAuthDialog();
				authDialog.setOnComplete(new Runnable()
				{
					@Override
					public void run()
					{
						dialog.dismiss();
						authHelper.oauthSend();
					}
				});
				dialog.setOnCancelListener(new OnCancelListener()
				{

					@Override
					public void onCancel(DialogInterface dialog)
					{
						finish();
					}
				});
				dialog.show();
			}
		}
		else
		{
			titleView.invalidate();
			homeListView.invalidateViews();
			mentionsListView.invalidateViews();
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		LogHelper.print("main pause");
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		tweetViewManager.open();
		tweetViewManager.close();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		LogHelper.print("main restart");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		LogHelper.print("main destroy");
		instance = null;
		if (twitterStream != null)
		{
			twitterStream.shutdown();
		}
		IconCaches.clearCache();
		StatusStore.clearCache();
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		if (reqCode == EnumRequestCode.AUTHORIZE.ordinal() && resultCode == Activity.RESULT_OK)
		{
			final Uri uri = data.getData();

			Account account = authHelper.oauthRecieve(uri);
			if (account != null)
			{
				Client.setMainAccount(account);
			}
			progressDialog = new ProgressDialog(instance);
			ProgressDialogHelper.makeProgressDialog(instance, progressDialog).show();
			new Thread(instance).start();
		}
	}

	public void toggleTimeline()
	{
		boolean isHome = isHomeMode.toggle();
		homeListView.setVisibility(isHome ? View.VISIBLE : View.INVISIBLE);
		mentionsListView.setVisibility(isHome ? View.INVISIBLE : View.VISIBLE);
		String title = isHome ? "Home" : "Mentions";
		titleView.setText(title);
		titleView.refreshDrawableState();
		int res = isHomeMode.get() ? R.drawable.icon_mentions_w : R.drawable.icon_home_w;
		timelineImage.setImageResource(res);
	}

	public void toggleMenu()
	{
		if (!optionMenuAdapter.isShowing())
		{
			optionMenuAdapter.createMenuDialog(true).show();
		}
		else
		{
			optionMenuAdapter.dispose();
		}
	}

	public void toggleTweetView()
	{
		tweetViewManager.toggle();
	}

	public void openTweetView()
	{
		tweetViewManager.open();
	}

	public void openTweetViewToTweet(String str)
	{
		tweetViewManager.setText(str);
		tweetViewManager.open();
	}

	public void openTweetViewToReply(String _userName, long l, boolean append)
	{
		String text = tweetViewManager.getText();
		Pattern hasReply = Pattern.compile("^@([a-zA-Z0-9_]+).*");
		if (append || hasReply.matcher(text).find())
		{
			if (!text.contains("@" + _userName))
			{
				text = text + "@" + _userName + " ";
				if (!text.startsWith("."))
				{
					text = "." + text;
				}
			}
			else
			{
				text = "@" + _userName + " ";
			}
		}
		else
		{
			text = "@" + _userName + " ";
		}
		tweetViewManager.setText(text);
		tweetViewManager.setInReplyToStatusId(l);
		tweetViewManager.open();
	}

	public void closeTweetView()
	{
		tweetViewManager.close();
	}

	public void eventNotify(EventModel event)
	{
		eventNoticer.noticeEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (tweetViewManager.isOpening())
			{
				tweetViewManager.toggle();
				return false;
			}
			else if (!isHomeMode.get())
			{
				toggleTimeline();
				return false;
			}
			else
			{
				finish();
				return false;
			}
		}
		else if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			toggleMenu();
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	private void viewSetUp()
	{
		isHomeMode = new ExtendedBoolean(true);
		titleView = ((TextView) findViewById(R.id.textView_title));
		titleView.setText("Home");
		titleView.setTextSize(Client.getTextSize() + 3);

		tweetImage = (ImageView) findViewById(R.id.imageView_tweet);
		tweetImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleTweetView();
			}
		});
		timelineImage = (ImageView) findViewById(R.id.imageView_timeline);
		timelineImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleTimeline();
			}
		});
		timelineImage.setImageResource(R.drawable.icon_mentions_w);
		menuImage = (ImageView) findViewById(R.id.imageView_menu);
		menuImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleMenu();
			}
		});

		homeListView = (ListView) findViewById(R.id.listView_home);
		homeListView.setAdapter(homeListAdapter);
		homeListView.setVisibility(View.VISIBLE);
		homeListView.setFastScrollEnabled(true);
		homeListView.setAlwaysDrawnWithCacheEnabled(true);
		homeListView.setOnScrollListener(new TimelineScrollListener(homeListAdapter));
		mentionsListView = (ListView) findViewById(R.id.listView_mentions);
		mentionsListView.setAdapter(mentionsListAdapter);
		mentionsListView.setVisibility(View.INVISIBLE);
		mentionsListView.setFastScrollEnabled(true);
		mentionsListView.setOnScrollListener(new TimelineScrollListener(mentionsListAdapter));
	}

	public ListView getHomeListView()
	{
		return homeListView;
	}

	public ListView getMentionsListView()
	{
		return mentionsListView;
	}

	public StatusListAdapter getHomeListAdapter()
	{
		return homeListAdapter;
	}

	public StatusListAdapter getMentionsListAdapter()
	{
		return mentionsListAdapter;
	}

	public OptionMenuAdapter getOptionMenuAdapter()
	{
		return optionMenuAdapter;
	}

}
