package net.miz_hi.smileessence.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.dialog.AuthDialogHelper;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.dialog.ProgressDialogHelper;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.menu.MainMenu;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.util.LogHelper;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity
{

	private static MainActivity instance;
	public static final int HANDLER_NOT_AUTHED = 0;
	public static final int HANDLER_SETUPED = 1;
	public static final int HANDLER_OAUTH_SUCCESS = 2;
	public static final int HANDLER_NOT_CONNECTION = 3;
	private AuthorizeHelper authHelper;
	private AuthDialogHelper authDialog;
	private boolean isFirstLoad = false;
	private ViewPager viewPager;
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
					MyExecutor.execute(new Runnable()
					{
						
						@Override
						public void run()
						{
							MainSystem.getInstance().twitterSetup(handler);							
						}
					});
					break;
				}
				case HANDLER_NOT_CONNECTION:
				{
					ToastManager.show("接続出来ません");
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
		MainSystem.getInstance().start(instance);
		authHelper = new AuthorizeHelper(instance, Consumers.getDedault());		
		TweetView.init(instance);
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
				MyExecutor.execute(new Runnable()
				{
					
					@Override
					public void run()
					{
						MainSystem.getInstance().twitterSetup(handler);
					}
				});
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
			MainSystem.getInstance().refreshLists();
		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
		TweetView.open(); //幅の反映のために開閉
		TweetView.close(); 
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
		MainSystem.getInstance().finish();
		instance = null;
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		if(resultCode != Activity.RESULT_OK)
		{
			return;
		}
		
		if (reqCode == EnumRequestCode.AUTHORIZE.ordinal())
		{
			final Uri uri = data.getData();

			Account account = authHelper.oauthRecieve(uri);
			if (account != null)
			{
				Client.setMainAccount(account);
			}
			progressDialog = new ProgressDialog(instance);
			ProgressDialogHelper.makeProgressDialog(instance, progressDialog).show();
			MyExecutor.execute(new Runnable()
			{
				
				@Override
				public void run()
				{
					MainSystem.getInstance().twitterSetup(handler);
				}
			});
		}
		else if(reqCode == EnumRequestCode.PICTURE.ordinal() || reqCode == EnumRequestCode.CAMERA.ordinal())
		{
			Uri uri;
			if(reqCode == EnumRequestCode.PICTURE.ordinal())
			{
				uri = data.getData();
			}
			else
			{
				uri = MainSystem.getInstance().tempFilePath;
			}
			
			if(uri == null)
			{
				ToastManager.show("ファイルが存在しません");
				return;
			}
			ContentResolver cr = getContentResolver();
			String[] columns = { MediaStore.Images.Media.DATA };
			Cursor c = cr.query(uri, columns, null, null, null);
			c.moveToFirst();
			if(c.isNull(c.getColumnIndex(MediaStore.Images.Media.DATA)))
			{
				ToastManager.show("ファイルが存在しません");
				return;
			}
			
			File path = new File(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)));
			if (!path.exists())
			{
				ToastManager.show("ファイルが存在しません");
				return;
			}
			else
			{
				TweetSystem.getInstance().setPicturePath(path);
				ToastManager.show("画像をセットしました");
				TweetView.open();
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (TweetView.getInstance().isOpening())
			{
				TweetView.toggle();
				return false;
			}
			else if(viewPager.getCurrentItem() != 0)
			{
				viewPager.setCurrentItem(0, true);
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
			if(TweetView.getInstance().isOpening())
			{
				TweetMenu.getInstance().createMenuDialog(true).show();
			}
			else
			{
				MainMenu.getInstance().createMenuDialog(true).show();
			}
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	private void viewSetUp()
	{	
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setAdapter(MainSystem.getInstance().pageAdapter);
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
						TweetView.getInstance().getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);						
						break;
					}
					default:
					{
						TweetView.getInstance().getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
						break;
					}
				}
			}
		});
	}

	public ViewPager getViewPager()
	{
		return viewPager;
	}


}
