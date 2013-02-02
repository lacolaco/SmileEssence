package net.miz_hi.smileessence.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.dialog.AuthDialogHelper;
import net.miz_hi.smileessence.dialog.OptionMenuAdapter;
import net.miz_hi.smileessence.dialog.ProgressDialogHelper;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.EventNoticer;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.listener.WarotterUserStreamListener;
import net.miz_hi.smileessence.status.IconCaches;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.TweetViewManager;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.User;
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

	private static MainActivity _instance;
	private static final int HANDLER_NOT_AUTHED = 0;
	private static final int HANDLER_SETUPED = 1;
	private static final int HANDLER_OAUTH_SUCCESS = 2;

	private ExtendedBoolean _isHomeMode;
	private TweetViewManager _tweetViewManager;
	private StatusListAdapter _homeListAdapter;
	private StatusListAdapter _mentionsListAdapter;
	private OptionMenuAdapter _optionMenuAdapter;
	private EventNoticer _eventNoticer;
	private AuthorizeHelper _authHelper;
	private AuthDialogHelper _authDialog;
	private TwitterStream _twitterStream;
	private boolean _isFirstLoad = false;
	private TextView _titleView;
	private ImageView _tweetImage;
	private ImageView _timelineImage;
	private ImageView _menuImage;
	private ListView _homeListView;
	private ListView _mentionsListView;
	private ProgressDialog _progressDialog;
	private Handler _handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			_progressDialog.dismiss();
			switch (msg.what)
			{
				case HANDLER_SETUPED:
				{
					Toast.makeText(_instance, "ê⁄ë±ÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();
					break;
				}
				case HANDLER_OAUTH_SUCCESS:
				{
					new Thread(_instance).start();
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

				final ArrayList<StatusModel> oldTimeline = new ArrayList<StatusModel>();
				final ArrayList<StatusModel> oldMentions = new ArrayList<StatusModel>();
				final List<Status> home = TwitterManager.getOldTimeline(account);
				final List<Status> mentions = TwitterManager.getOldMentions(account);

				runOnUiThread(new Runnable()
				{

					@Override
					public void run()
					{
						for (twitter4j.Status st : home)
						{
							StatusStore.put(st);
							if (st.isRetweet())
							{
								StatusStore.put(st.getRetweetedStatus());
							}
							oldTimeline.add(StatusModel.createInstance(st));
						}

						for (twitter4j.Status st : mentions)
						{
							StatusStore.put(st);
							if (st.isRetweet())
							{
								StatusStore.put(st.getRetweetedStatus());
							}
							oldMentions.add(StatusModel.createInstance(st));
						}
						_homeListAdapter.setList(oldTimeline);
						_mentionsListAdapter.setList(oldMentions);						
					}
				});


				WarotterUserStreamListener usListener = new WarotterUserStreamListener();
				usListener.setHomeListAdapter(_homeListAdapter);
				usListener.setMentionsListAdapter(_mentionsListAdapter);
				_twitterStream = TwitterManager.getTwitterStream(Client.getMainAccount());
				_twitterStream.addListener(usListener);
				_twitterStream.user();
				break;
			}
		}
		_handler.sendEmptyMessage(HANDLER_SETUPED);					

	}

	public static MainActivity getInstance()
	{
		return _instance;
	}

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.mainactivity_layout);
		LogHelper.print("main create");
		if (_instance == null)
		{
			_instance = this;
			_isFirstLoad = true;			
		}

		_homeListAdapter = new StatusListAdapter(_instance);
		_mentionsListAdapter = new StatusListAdapter(_instance);
		_optionMenuAdapter = new OptionMenuAdapter(_instance, "ÉÅÉjÉÖÅ[");
		_eventNoticer = new EventNoticer(_instance);
		_authHelper = new AuthorizeHelper(_instance, Consumers.getDedault());
		_tweetViewManager = new TweetViewManager(_instance);
		_tweetViewManager.init();

		viewSetUp();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		LogHelper.print("main resume");
		if (_isFirstLoad)
		{
			_isFirstLoad = false;

			if(Client.hasAuthedAccount())
			{
				_progressDialog = new ProgressDialog(_instance);
				ProgressDialogHelper.makeProgressDialog(_instance, _progressDialog).show();
				new Thread(_instance).start();
			}
			else
			{
				_authDialog = new AuthDialogHelper(_instance);
				final Dialog dialog = _authDialog.getAuthDialog();
				_authDialog.setOnComplete(new Runnable()
				{
					public void run()
					{
						dialog.dismiss();
						_authHelper.oauthSend();
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
			_titleView.invalidate();
			_homeListView.invalidateViews();
			_mentionsListView.invalidateViews();	

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
		_tweetViewManager.open();
		_tweetViewManager.close();
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
		_instance = null;
		if (_twitterStream != null)
		{
			_twitterStream = null;
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

			Account account = _authHelper.oauthRecieve(uri);
			if (account != null)
			{
				Client.setMainAccount(account);
			}
			_progressDialog = new ProgressDialog(_instance);
			ProgressDialogHelper.makeProgressDialog(_instance, _progressDialog).show();
			new Thread(_instance).start();
		}
	}

	public void toggleTimeline()
	{
		boolean isHome = _isHomeMode.toggle();
		_homeListView.setVisibility(isHome ? View.VISIBLE : View.INVISIBLE);
		_mentionsListView.setVisibility(isHome ? View.INVISIBLE : View.VISIBLE);
		String title = isHome ? "Home" : "Mentions";
		_titleView.setText(title);
		_titleView.refreshDrawableState();
		int res = _isHomeMode.get() ? R.drawable.icon_mentions_w : R.drawable.icon_home_w;
		_timelineImage.setImageResource(res);
	}

	public void toggleMenu()
	{
		if (!_optionMenuAdapter.isShowing())
		{
			_optionMenuAdapter.createMenuDialog(true).show();
		}
		else
		{
			_optionMenuAdapter.dispose();
		}
	}

	public void toggleTweetView()
	{
		_tweetViewManager.toggle();
	}

	public void openTweetView()
	{
		_tweetViewManager.open();
	}

	public void openTweetViewToTweet(String str)
	{
		_tweetViewManager.setText(str);
		_tweetViewManager.open();
	}

	public void openTweetViewToReply(String _userName, long l, boolean append)
	{
		String text = _tweetViewManager.getText();
		Pattern hasReply = Pattern.compile("^@([a-zA-Z0-9_]+).*");
		if(append || hasReply.matcher(text).find())
		{
			if(!text.contains("@"+ _userName))
			{
				text = text + "@" + _userName + " ";
				if(!text.startsWith("."))
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
		_tweetViewManager.setText(text);
		_tweetViewManager.setInReplyToStatusId(l);
		_tweetViewManager.open();
	}

	public void closeTweetView()
	{
		_tweetViewManager.close();
	}

	public void eventNotify(EventModel event)
	{
		_eventNoticer.noticeEvent(event);
	}

	public StatusListAdapter getHomeListAdapter()
	{
		return _homeListAdapter;
	}

	public StatusListAdapter getMentionsListAdapter()
	{
		return _mentionsListAdapter;
	}

	public OptionMenuAdapter getOptionMenuAdapter()
	{
		return _optionMenuAdapter;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (_tweetViewManager.isOpening())
			{
				_tweetViewManager.toggle();
				return false;
			}
			else if (!_isHomeMode.get())
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
		_isHomeMode = new ExtendedBoolean(true);
		_titleView = ((TextView) findViewById(R.id.textView_title));
		_titleView.setText("Home");
		_titleView.setTextSize(Client.getTextSize() + 3);

		_tweetImage = (ImageView) findViewById(R.id.imageView_tweet);
		_tweetImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleTweetView();
			}
		});
		_timelineImage = (ImageView) findViewById(R.id.imageView_timeline);
		_timelineImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleTimeline();
			}
		});
		_timelineImage.setImageResource(R.drawable.icon_mentions_w);
		_menuImage = (ImageView) findViewById(R.id.imageView_menu);
		_menuImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleMenu();
			}
		});

		_homeListView = (ListView) findViewById(R.id.listView_home);
		_homeListView.setAdapter(_homeListAdapter);
		_homeListView.setOnScrollListener(new TimelineScrollListener(_homeListAdapter));
		_homeListView.setVisibility(View.VISIBLE);
		_mentionsListView = (ListView) findViewById(R.id.listView_mentions);
		_mentionsListView.setAdapter(_mentionsListAdapter);
		_mentionsListView.setOnScrollListener(new TimelineScrollListener(_mentionsListAdapter));
		_mentionsListView.setVisibility(View.INVISIBLE);
	}

}
