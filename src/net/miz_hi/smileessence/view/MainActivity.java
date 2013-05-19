package net.miz_hi.smileessence.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.listener.PageChangeListener;
import net.miz_hi.smileessence.menu.MainMenu;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.util.NamedFragmentPagerAdapter;
import net.miz_hi.smileessence.util.UiHandler;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.viewpagerindicator.TitlePageIndicator;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainActivity extends FragmentActivity
{

	private static MainActivity instance;
	public static final int PAGE_POST = 0;
	NamedFragmentPagerAdapter adapter;
	ViewPager pager;
	TitlePageIndicator indicator;
	View footerBar;

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
	}

	private void initializeViews()
	{
		adapter = new NamedFragmentPagerAdapter(getSupportFragmentManager());
		adapter.add((NamedFragment) PostFragment.instantiate(instance, PostFragment.class.getName()));
		adapter.add((NamedFragment) HomeFragment.instantiate(instance, HomeFragment.class.getName()));
		adapter.add((NamedFragment) MentionsFragment.instantiate(instance, MentionsFragment.class.getName()));
		adapter.add((NamedFragment) HistoryFragment.instantiate(instance, HistoryFragment.class.getName()));
		pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setAdapter(adapter);
		pager.destroyDrawingCache();
		indicator = (TitlePageIndicator)findViewById(R.id.indicator);
		indicator.setTextSize(21);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new PageChangeListener());
		footerBar = findViewById(R.id.footer);
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
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
		if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.VISIBLE_FOORER))
		{
			footerBar.setVisibility(View.VISIBLE);
		}
		else
		{
			footerBar.setVisibility(View.GONE);
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		LogHelper.d("main pause");
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

	public void onEditClick(View v)
	{
		moveViewPage(PAGE_POST);
	}

	public void onMenuClick(View v)
	{
		dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MENU));
	}

	public void onTwitterClick(View v)
	{
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/")));
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
				if(pager.getCurrentItem() != 1)
				{
					pager.setCurrentItem(1, true);
				}
				else
				{
					if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
					{
						ConfirmDialog.show(this, "èIóπÇµÇ‹Ç∑Ç©ÅH", new Runnable()
						{

							@Override
							public void run()
							{
								finish();
							}
						});
					}
					else
					{
						finish();
					}
				}
				return false;
			}
			case KeyEvent.KEYCODE_MENU:
			{				
				openOptionsMenu();
				return true;
			}
			default:
			{
				return super.dispatchKeyEvent(event);
			}
		}

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		Fragment fragment = (NamedFragment) adapter.getItem(getCurrentPage());
		if(fragment != null && fragment instanceof IRemovable)
		{
			menu.findItem(R.id.menu_remove).setVisible(true);
		}
		else
		{
			menu.findItem(R.id.menu_remove).setVisible(false);
		}
		boolean extract = Client.<Boolean>getPreferenceValue(EnumPreferenceKey.EXTRACT_TO) && !ExtractFragment.isShowing;
		menu.findItem(R.id.menu_extract).setVisible(extract);
		
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menu_post:
			{
				PostSystem.openPostPage();
				break;
			}
			case R.id.menu_home:
			{
				moveViewPage(1);
				break;
			}
			case R.id.menu_option:
			{
				new MainMenu(instance).create().show();
				break;
			}
			case R.id.menu_extract:
			{
				ExtractFragment fragment = ExtractFragment.singleton();
				addPage(fragment);
				moveViewPage(getPagerCount());
				break;
			}
			case R.id.menu_remove:
			{
				removePage();
				break;
			}
			case R.id.menu_exit:
			{
				finish();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public ViewPager getViewPager()
	{
		return pager;
	}

	public NamedFragmentPagerAdapter getFragmentAdapter()
	{
		return adapter;
	}

}
