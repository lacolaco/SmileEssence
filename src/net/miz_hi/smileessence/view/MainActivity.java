package net.miz_hi.smileessence.view;

import java.util.List;
import java.util.concurrent.Future;

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
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.dialog.AuthDialogHelper;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.dialog.ProgressDialogHelper;
import net.miz_hi.smileessence.event.HistoryListAdapter;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.listener.WarotterUserStreamListener;
import net.miz_hi.smileessence.menu.OptionMenuAdapter;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.LogHelper;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity implements Runnable
{

	private static MainActivity instance;
	private static final int HANDLER_NOT_AUTHED = 0;
	private static final int HANDLER_SETUPED = 1;
	private static final int HANDLER_OAUTH_SUCCESS = 2;
	private static final int HANDLER_NOT_CONNECTION = 3;

	private FragmentPagerAdapter pageAdapter;
	private StatusListAdapter homeListAdapter;
	private StatusListAdapter mentionsListAdapter;
	private HistoryListAdapter historyListAdapter;
	private OptionMenuAdapter optionMenuAdapter;
	private AuthorizeHelper authHelper;
	private AuthDialogHelper authDialog;
	private WarotterUserStreamListener usListener;
	private TwitterStream twitterStream;
	private boolean isFirstLoad = false;
	private ProgressDialog progressDialog;
	private ViewPager viewPager;
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
					MyExecutor.execute(instance);
					break;
				}
				case HANDLER_NOT_CONNECTION:
				{
					ToastManager.getInstance().toast("ê⁄ë±èoóàÇ‹ÇπÇÒ");
					break;
				}
			}
		}
	};
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity_layout);
		LogHelper.printD("main create");

		instance = this;
		isFirstLoad = true;
		
		homeListAdapter = new StatusListAdapter(instance);
		mentionsListAdapter = new StatusListAdapter(instance);
		historyListAdapter = new HistoryListAdapter(instance);
		optionMenuAdapter = new OptionMenuAdapter(instance);
		authHelper = new AuthorizeHelper(instance, Consumers.getDedault());
		
		TweetViewManager.init(instance);
		viewSetUp();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		LogHelper.printD("main resume");
		if (isFirstLoad)
		{
			isFirstLoad = false;

			if (Client.hasAuthedAccount())
			{
				progressDialog = new ProgressDialog(instance);
				ProgressDialogHelper.makeProgressDialog(instance, progressDialog).show();
				MyExecutor.execute(instance);
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
			refreshViews();
		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

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

					final Future<List<StatusModel>> resp_home = MyExecutor.submit(new AsyncTimelineGetter(account, null));					
					final Future<List<StatusModel>> resp_mentions = MyExecutor.submit(new AsyncMentionsGetter(account, new Paging(1)));
					try
					{
						List<StatusModel> oldTimeline = resp_home.get();
						homeListAdapter.addAll(oldTimeline);
						List<StatusModel> oldMentions = resp_mentions.get();
						mentionsListAdapter.addAll(oldMentions);
						homeListAdapter.forceNotifyAdapter();
						mentionsListAdapter.forceNotifyAdapter();
						historyListAdapter.forceNotifyAdapter();
						
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					handler.sendEmptyMessage(HANDLER_NOT_CONNECTION);
				}

				handler.sendEmptyMessage(HANDLER_SETUPED);
				return;
			}
		}

	}

	public static MainActivity getInstance()
	{
		return instance;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		LogHelper.printD("main pause");
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		TweetViewManager.getInstance().open(); //ïùÇÃîΩâfÇÃÇΩÇﬂÇ…äJï¬
		TweetViewManager.getInstance().close(); 
		DialogAdapter.dispose();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		LogHelper.printD("main restart");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		LogHelper.printD("main destroy");
		if (twitterStream != null)
		{
			twitterStream.shutdown();
			twitterStream = null;
		}
		IconCaches.clearCache();
		StatusStore.clearCache();
		UserStore.clearCache();
		MyExecutor.shutdown();
		instance = null;
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
			MyExecutor.execute(instance);
		}
	}
	
	public void refreshViews()
	{
		homeListAdapter.forceNotifyAdapter();
		mentionsListAdapter.forceNotifyAdapter();
		historyListAdapter.forceNotifyAdapter();
		pageAdapter.notifyDataSetChanged();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (TweetViewManager.getInstance().isOpening())
			{
				TweetViewManager.getInstance().toggle();
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
		
		Fragment[] fragments = new Fragment[3];
		fragments[0] = Fragment.instantiate(this, HomeListPageFragment.class.getName());
		fragments[1] = Fragment.instantiate(this, MentionsListPageFragment.class.getName());
		fragments[2] = Fragment.instantiate(this, HistoryListPageFragment.class.getName());
		
		pageAdapter = new ListPagerAdapter(super.getSupportFragmentManager(), fragments);
		
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setAdapter(pageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() 
		{
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{ 
			}

			@Override
			public void onPageSelected(int position) 
			{
				switch (position)
				{
					case 0:
					{
						TweetViewManager.getInstance().getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);						
						break;
					}
					default:
					{
						TweetViewManager.getInstance().getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
						break;
					}
				}
			}
		});
	}
	
	public StatusListAdapter getHomeListAdapter()
	{
		return homeListAdapter;
	}

	public StatusListAdapter getMentionsListAdapter()
	{
		return mentionsListAdapter;
	}

	public HistoryListAdapter getHistoryListAdapter()
	{
		return historyListAdapter;
	}

	public OptionMenuAdapter getOptionMenuAdapter()
	{
		return optionMenuAdapter;
	}


}
