package net.miz_hi.smileessence.view;

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
import net.miz_hi.smileessence.event.EventListAdapter;
import net.miz_hi.smileessence.event.StatusEventModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.listener.WarotterUserStreamListener;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.ConnectionLifeCycleListener;
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
	private static final int HANDLER_NOT_CONNECTION = 3;
	private static final int LIST_HOME = 0;
	private static final int LIST_MENTIONS = 1;
	private static final int LIST_HISTORY = 2;
	private static int currentList;

	private TweetViewManager tweetViewManager;
	private StatusListAdapter homeListAdapter;
	private StatusListAdapter mentionsListAdapter;
	private EventListAdapter historyListAdapter;
	private OptionMenuAdapter optionMenuAdapter;
	private AuthorizeHelper authHelper;
	private AuthDialogHelper authDialog;
	private WarotterUserStreamListener usListener;
	private TwitterStream twitterStream;
	private boolean isFirstLoad = false;
	private TextView textViewTitle;
	private ImageView imageViewTweet;
	private ImageView imageViewMentions;
	private ImageView imageViewHistory;
	private ListView homeListView;
	private ListView mentionsListView;
	private ListView historyListView;
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
					break;
				}
				case HANDLER_OAUTH_SUCCESS:
				{
					new Thread(instance).start();
					break;
				}
				case HANDLER_NOT_CONNECTION:
				{
					Toast.makeText(instance, "ê⁄ë±èoóàÇ‹ÇπÇÒ", Toast.LENGTH_LONG).show();
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

				usListener = new WarotterUserStreamListener();
				usListener.setHomeListAdapter(homeListAdapter);
				usListener.setMentionsListAdapter(mentionsListAdapter);
				usListener.setEventListAdapter(historyListAdapter);
				twitterStream = TwitterManager.getTwitterStream(Client.getMainAccount());
				twitterStream.addListener(usListener);
				twitterStream.addConnectionLifeCycleListener(usListener);
				boolean canConnect = TwitterManager.canConnect();
				
				if(canConnect)
				{
					connectUserStream();

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
								historyListAdapter.forceNotifyAdapter();
							}
						}.post();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					handler.sendEmptyMessage(HANDLER_NOT_CONNECTION);
					return;
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
		historyListAdapter = new EventListAdapter(instance);
		optionMenuAdapter = new OptionMenuAdapter(instance, "ÉÅÉjÉÖÅ[");
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
			textViewTitle.invalidate();
			homeListView.invalidateViews();
			mentionsListView.invalidateViews();
			historyListView.invalidateViews();
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
			twitterStream = null;
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
	
	public void connectUserStream()
	{
		if(!TwitterManager.canConnect())
		{
			handler.sendEmptyMessage(HANDLER_NOT_CONNECTION);
			return;
		}
		if(twitterStream != null)
		{
			twitterStream.shutdown();
			twitterStream.user();
		}
	}
	
	public int getCurrentList()
	{
		return currentList;
	}

	public void changeVisibleList(int i)
	{
		if(i == currentList)
		{
			return;
		}
		switch (currentList)
		{
			case LIST_HOME:
			{
				switch (i)
				{
					case LIST_MENTIONS:
					{
						homeListView.setVisibility(View.INVISIBLE);
						mentionsListView.setVisibility(View.VISIBLE);
						historyListView.setVisibility(View.INVISIBLE);
						textViewTitle.setText("Mentions");
						imageViewMentions.setImageResource(R.drawable.icon_home_w);
						imageViewHistory.setImageResource(R.drawable.icon_history);
						break;
					}
					case LIST_HISTORY:
					{
						homeListView.setVisibility(View.INVISIBLE);
						mentionsListView.setVisibility(View.INVISIBLE);
						historyListView.setVisibility(View.VISIBLE);
						textViewTitle.setText("History");
						imageViewMentions.setImageResource(R.drawable.icon_mentions_w);
						imageViewHistory.setImageResource(R.drawable.icon_home_w);
						break;
					}
				}
				break;
			}
			case LIST_MENTIONS:
			{
				switch (i)
				{
					case LIST_HOME:
					{
						homeListView.setVisibility(View.VISIBLE);
						mentionsListView.setVisibility(View.INVISIBLE);
						historyListView.setVisibility(View.INVISIBLE);
						textViewTitle.setText("Home");
						imageViewMentions.setImageResource(R.drawable.icon_mentions_w);
						imageViewHistory.setImageResource(R.drawable.icon_history);
						break;
					}
					case LIST_HISTORY:
					{
						homeListView.setVisibility(View.INVISIBLE);
						mentionsListView.setVisibility(View.INVISIBLE);
						historyListView.setVisibility(View.VISIBLE);
						textViewTitle.setText("History");
						imageViewMentions.setImageResource(R.drawable.icon_mentions_w);
						imageViewHistory.setImageResource(R.drawable.icon_home_w);
						break;
					}
				}
				break;				
			}
			case LIST_HISTORY:
			{
				switch (i)
				{
					case LIST_HOME:
					{
						homeListView.setVisibility(View.VISIBLE);
						mentionsListView.setVisibility(View.INVISIBLE);
						historyListView.setVisibility(View.INVISIBLE);
						textViewTitle.setText("Home");
						imageViewMentions.setImageResource(R.drawable.icon_mentions_w);
						imageViewHistory.setImageResource(R.drawable.icon_history);
						break;
					}
					case LIST_MENTIONS:
					{
						homeListView.setVisibility(View.INVISIBLE);
						mentionsListView.setVisibility(View.VISIBLE);
						historyListView.setVisibility(View.INVISIBLE);
						textViewTitle.setText("Mentions");
						imageViewMentions.setImageResource(R.drawable.icon_home_w);
						imageViewHistory.setImageResource(R.drawable.icon_history);
						break;
					}
				}
				break;
			}
		}
		currentList = i;
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

	public void openTweetViewToReply(String userName, long l, boolean append)
	{
		tweetViewManager.openToReply(userName, l, append);
	}

	public void closeTweetView()
	{
		tweetViewManager.close();
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
			else if (currentList != LIST_HOME)
			{
				changeVisibleList(LIST_HOME);
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
		currentList = LIST_HOME;
		textViewTitle = ((TextView) findViewById(R.id.textView_title));
		textViewTitle.setText("Home");
		textViewTitle.setTextSize(Client.getTextSize() + 3);

		imageViewTweet = (ImageView) findViewById(R.id.imageView_tweet);
		imageViewTweet.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleTweetView();
			}
		});
		
		imageViewMentions = (ImageView) findViewById(R.id.imageView_timeline);
		imageViewMentions.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				changeVisibleList(currentList != LIST_MENTIONS ? LIST_MENTIONS: LIST_HOME);
			}
		});
		imageViewMentions.setImageResource(R.drawable.icon_mentions_w);
		
		imageViewHistory = (ImageView) findViewById(R.id.imageView_history);
		imageViewHistory.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				changeVisibleList(currentList != LIST_HISTORY ? LIST_HISTORY : LIST_HOME);
			}
		});
		imageViewHistory.setImageResource(R.drawable.icon_history);
		
		homeListView = (ListView) findViewById(R.id.listView_home);
		homeListView.setAdapter(homeListAdapter);
		homeListView.setVisibility(View.VISIBLE);
		homeListView.setFastScrollEnabled(true);
		homeListView.setOnScrollListener(new TimelineScrollListener(homeListAdapter));
		mentionsListView = (ListView) findViewById(R.id.listView_mentions);
		mentionsListView.setAdapter(mentionsListAdapter);
		mentionsListView.setVisibility(View.INVISIBLE);
		mentionsListView.setFastScrollEnabled(true);
		mentionsListView.setOnScrollListener(new TimelineScrollListener(mentionsListAdapter));
		historyListView = (ListView) findViewById(R.id.listView_history);
		historyListView.setAdapter(historyListAdapter);
		historyListView.setVisibility(View.INVISIBLE);
		historyListView.setFastScrollEnabled(true);
		historyListView.setOnScrollListener(new TimelineScrollListener(historyListAdapter));
	}

	public ListView getHomeListView()
	{
		return homeListView;
	}

	public ListView getMentionsListView()
	{
		return mentionsListView;
	}

	public ListView getHistoryListView()
	{
		return historyListView;
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
