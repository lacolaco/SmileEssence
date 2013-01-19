package net.miz_hi.warotter.view;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.core.EventSubscriber;
import net.miz_hi.warotter.core.Message;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import net.miz_hi.warotter.viewmodel.TweetViewModel;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends EventHandlerActivity
{
	public SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setContentView(R.layout.mainactivity_layout);
		
		ImageView tweet = (ImageView)findViewById(R.id.imageView_tweet);
		ImageView timeline = (ImageView)findViewById(R.id.imageView_timeline);
		ImageView menu = (ImageView)findViewById(R.id.imageView_menu);
		tweet.setOnClickListener(tweetClicked);
		timeline.setOnClickListener(timelineClicked);
		menu.setOnClickListener(menuClicked);
		
		messenger.subscribe("toggle", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String eventName, Message message)
			{
				slidingMenu.toggle();			
			}
		});
		
		TweetViewModel tweetViewModel = new TweetViewModel();
		registerViewModel(tweetViewModel);
		
		slidingMenu = createSlidingMenu();		
		tweetViewModel.init(this, slidingMenu);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public ViewModel getViewModel()
	{
		return MainActivityViewModel.initialize(this);
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
		View rootView = LayoutInflater.from(this).inflate(R.layout.tweet_layout, null);
		menu.setMenu(rootView);
		return menu;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode != KeyEvent.KEYCODE_BACK)
		{
			return super.onKeyDown(keyCode, event);
		}
		else if(slidingMenu.isMenuShowing())
		{
			slidingMenu.toggle();
			return false;
		}
		else
		{
			finish();
			return false;
		}
	}
	
	public OnClickListener tweetClicked = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			onEvent("tweet");			
		}
	};
	
	public OnClickListener timelineClicked = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			onEvent("timeline");		
			}
	};
	
	public OnClickListener menuClicked = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			onEvent("menu");			
		}
	};
}
