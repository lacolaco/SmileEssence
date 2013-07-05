package net.miz_hi.smileessence.view.activity;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.listener.PageChangeListener;
import net.miz_hi.smileessence.menu.MainMenu;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.system.IntentRouter;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.util.NamedFragmentPagerAdapter;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.fragment.HistoryFragment;
import net.miz_hi.smileessence.view.fragment.HomeFragment;
import net.miz_hi.smileessence.view.fragment.MentionsFragment;
import net.miz_hi.smileessence.view.fragment.PostFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.OnCenterItemClickListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainActivity extends FragmentActivity
{

	private static MainActivity instance;
	public static final int PAGE_POST = 0;
	NamedFragmentPagerAdapter adapter;
	ViewPager pager;
	TitlePageIndicator indicator;
	
	public static MainActivity getInstance()
	{
		return instance;
	}

	public static void moveViewPage(final int index)
	{
		new UiHandler()
		{

			@Override
			public void run()
			{
				instance.pager.setCurrentItem(index, false);
			}
		}.post();
	}

	public static int getCurrentPage()
	{
		return instance.pager.getCurrentItem();
	}

	public static void addPage(final NamedFragment fragment)
	{
		new UiHandler()
		{

			@Override
			public void run()
			{
				instance.adapter.add(fragment);
			}
		}.post();
	}

	public static void removePage()
	{
		new UiHandler()
		{

			@Override
			public void run()
			{
				int current = instance.pager.getCurrentItem();
				instance.adapter.remove(current);
				List<NamedFragment> list = new ArrayList<NamedFragment>();
				list.addAll(instance.adapter.getList());
				instance.adapter = new NamedFragmentPagerAdapter(instance.getSupportFragmentManager(), list); //Refresh page caches...
				instance.pager.setAdapter(instance.adapter);
				instance.pager.setCurrentItem(current);
			}
		}.post();

	}

	public static int getPagerCount()
	{
		return instance.adapter.getCount();
	}

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		LogHelper.d("main create");
		instance = this;
		initializeViews();
		MainSystem.getInstance().initialize(instance);
		IntentRouter.onNewIntent(getIntent());
	}

	private void initializeViews()
	{
		adapter = new NamedFragmentPagerAdapter(getSupportFragmentManager());
		adapter.add((NamedFragment) Fragment.instantiate(instance, PostFragment.class.getName()));
//		adapter.add(new PostFragment());
		adapter.add((NamedFragment) Fragment.instantiate(instance, HomeFragment.class.getName()));
		adapter.add((NamedFragment) Fragment.instantiate(instance, MentionsFragment.class.getName()));
		adapter.add((NamedFragment) Fragment.instantiate(instance, HistoryFragment.class.getName()));
		pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setAdapter(adapter);
		pager.destroyDrawingCache();
		
		indicator = (TitlePageIndicator)findViewById(R.id.indicator);
		indicator.setTextSize(21);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new PageChangeListener());
		indicator.setOnCenterItemClickListener(new OnCenterItemClickListener()
		{
			
			@Override
			public void onCenterItemClick(int position)
			{
				new MainMenu(instance).create().show();
			}
		});
		
		new UiHandler()
		{

			@Override
			public void run()
			{
				moveViewPage(1);
			}
		}.post();
	}	

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		MainSystem.getInstance().setup(instance);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		LogHelper.d("main resume");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		LogHelper.d("main pause");
		MainSystem.getInstance().saveRamainablePages();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		LogHelper.d("main destroy");		
		Crouton.cancelAllCroutons();
		MainSystem.getInstance().onDestroyed();
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
			MainSystem.getInstance().authorize(instance, data.getData());
		}
		else if(reqCode == EnumRequestCode.PICTURE.ordinal() || reqCode == EnumRequestCode.CAMERA.ordinal())
		{
			MainSystem.getInstance().receivePicture(instance, data, reqCode);
		}
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		IntentRouter.onNewIntent(intent);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		if(event.getAction() != KeyEvent.ACTION_DOWN)
		{
			return super.dispatchKeyEvent(event);
		}
		switch(event.getKeyCode())
		{
			case KeyEvent.KEYCODE_BACK:
			{
				finish();
				return false;
			}
			case KeyEvent.KEYCODE_MENU:
			{				
				openMenu();
				return true;
			}
			default:
			{
				return super.dispatchKeyEvent(event);
			}
		}

	}	

	@Override
	public void finish()
	{
		if(pager.getCurrentItem() != 1)
		{
			pager.setCurrentItem(1, true);
		}
		else
		{
			if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
			{
				ConfirmDialog.show(this, "終了しますか？", new Runnable()
				{

					@Override
					public void run()
					{
						MainActivity.super.finish();
					}
				});
			}
			else
			{
				super.finish();
			}
		}
	}
	
	public void openMenu()
	{
		new MainMenu(instance).create().show();
	}

	public ViewPager getViewPager()
	{
		return pager;
	}

	public NamedFragmentPagerAdapter getPagerAdapter()
	{
		return adapter;
	}

}