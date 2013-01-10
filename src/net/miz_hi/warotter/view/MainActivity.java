package net.miz_hi.warotter.view;

import gueei.binding.Binder;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.widgets.BindableLinearLayout;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.MenuDialogMessage;
import net.miz_hi.warotter.core.EventBindingActivity;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import net.miz_hi.warotter.viewmodel.MenuViewModel;
import net.miz_hi.warotter.viewmodel.TweetViewModel;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends EventBindingActivity
{
	public SlidingMenu slidingMenu;
	public AlertDialog dialog;

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setAndBindRootView(R.layout.mainactivity_layout, viewModel);
		TweetViewModel tweetViewModel = new TweetViewModel(this);
		tweetViewModel.setEventAggregator(viewModel.eventAggregator);
		slidingMenu = createSlidingMenu();
		View tweetView = Binder.bindView(slidingMenu.getContext(), Binder.inflateView(this, R.layout.tweet_layout, null, true), tweetViewModel);
		slidingMenu.setMenu(tweetView);
	}

	@Override
	public ViewModel getViewModel()
	{
		return MainActivityViewModel.initialize(this);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		viewModel.onDispose();
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
						slidingMenu.toggle();
					}
				});
			}
		});
		return ea;
	}
	
	public SlidingMenu createSlidingMenu()
	{
		SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		return menu;
	}
}
