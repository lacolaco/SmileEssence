package net.miz_hi.warotter.view;

import gueei.binding.Binder;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.EventBindingActivity;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.util.EnumRequestCode;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import net.miz_hi.warotter.viewmodel.TweetViewModel;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends EventBindingActivity
{
	public SlidingMenu menu;

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setAndBindRootView(R.layout.mainactivity_layout, viewModel);
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		final TweetViewModel tweetViewModel = new TweetViewModel();
		tweetViewModel.setEventAggregator(viewModel.eventAggregator);
		View tweetView = Binder.bindView(menu.getContext(), Binder.inflateView(this, R.layout.tweet_layout, null, true), tweetViewModel);
		menu.setMenu(tweetView);
	}

	@Override
	public ViewModel getViewModel()
	{
		return MainActivityViewModel.getSingleton();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		viewModel.onDispose();
		finishActivity(EnumRequestCode.MAIN.ordinal());
	}

	@Override
	public EventAggregator getAggregator()
	{
		EventAggregator ea = super.getAggregator();
		ea.subscribe("toggle", new EventSubscriber()
		{

			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						menu.toggle();
					}
				});

			}
		});
		return ea;
	}

}
