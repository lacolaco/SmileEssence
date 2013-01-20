package net.miz_hi.smileessence.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.EventSubscriber;
import net.miz_hi.smileessence.core.Message;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.event.EventNoticer;
import net.miz_hi.smileessence.message.EventMessage;
import net.miz_hi.smileessence.message.ReplyMessage;
import net.miz_hi.smileessence.message.TweetMessage;
import net.miz_hi.smileessence.viewmodel.MainActivityViewModel;
import net.miz_hi.smileessence.viewmodel.TweetViewModel;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

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
		
		appendMessenger();
		
		TweetViewModel tweetViewModel = TweetViewModel.singleton();
		registerViewModel(tweetViewModel);
		
		slidingMenu = createSlidingMenu();		
		tweetViewModel.init(this, slidingMenu);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	private void appendMessenger()
	{
		messenger.subscribe("toggle", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String eventName, Message message)
			{
				slidingMenu.toggle();
			}
		});
		messenger.subscribe("tweet", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String eventName, Message message)
			{
				TweetViewModel.singleton().text = ((TweetMessage)message).text;
				slidingMenu.toggle();			
			}
		});		
		messenger.subscribe("reply", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String eventName, Message message)
			{
				Pattern hasReply = Pattern.compile("^@([a-zA-Z0-9_]+).*");
				String text = TweetViewModel.singleton().text.toString();
				text = text + "@" + ((ReplyMessage)message).sendTo.getScreenName() + " ";
				TweetViewModel.singleton().text = text;
				TweetViewModel.singleton().inReplyTo = ((ReplyMessage)message).inReplyToStatusId;
				slidingMenu.toggle();			
			}
		});
		messenger.subscribe("event", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String eventName, Message message)
			{
				EventNoticer.noticeEvent(MainActivity.this, ((EventMessage)message).model);				
			}
		});
	}

	@Override
	public ViewModel getViewModel()
	{
		return MainActivityViewModel.singleton().initialize(this);
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
		
		menu.setOnCloseListener(new OnCloseListener()
		{
			
			@Override
			public void onClose()
			{
				TweetViewModel.singleton().onCloseSlidingMenu(slidingMenu);
			}
		});
		menu.setOnOpenListener(new OnOpenListener()
		{
			
			@Override
			public void onOpen()
			{
				TweetViewModel.singleton().onOpenSlidingMenu(slidingMenu);
			}
		});
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
		else if(!MainActivityViewModel.singleton().isHomeMode.get())
		{
			onEvent("timeline");
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
			slidingMenu.toggle();
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
