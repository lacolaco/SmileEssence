package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.listener.TweetViewTouchListener;
import net.miz_hi.smileessence.menu.TweetMenuAdapter;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.StatusUpdate;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class TweetViewManager
{

	private static TweetViewManager instance;
	private SlidingMenu menu;
	private long inReplyTo;
	private Activity activity;
	private EditText editTextTweet;
	private TweetMenuAdapter menuAdapter;

	private TweetViewManager(Activity activity)
	{
		this.activity = activity;
		createSlidingMenu();
		init();
		inReplyTo = -1;
	}
	
	public static void init(Activity activity)
	{
		instance = new TweetViewManager(activity);
	}
	
	public static TweetViewManager getInstance()
	{
		return instance;
	}
	
	public String getText()
	{
		return editTextTweet.getText().toString();
	}

	public void setText(String str)
	{
		editTextTweet.setText(str);
		editTextTweet.setSelection(str.length());
	}
	
	public void appendText(String str)
	{
		String text = editTextTweet.getText().toString() + str;
		setText(text);
	}
	
	public void insertText(String str)
	{
		int cursor = editTextTweet.getSelectionEnd();
		StringBuilder sb = new StringBuilder(editTextTweet.getText().toString());
		sb.insert(cursor, str);
		setText(sb.toString());
		cursor = cursor + sb.length();
		if (cursor > editTextTweet.getText().length())
		{
			cursor = editTextTweet.getText().length();
		}
		editTextTweet.setSelection(cursor);
	}

	public void setInReplyToStatusId(long statusId)
	{
		inReplyTo = statusId;
	}

	public void setReply(String userName, long statusId)
	{
		setText("@" + userName + " ");
		inReplyTo = statusId;
	}
	
	public void addReply(String userName)
	{
		String text = editTextTweet.getText().toString();
		if (text.startsWith("@"))
		{
			text = "." + text;
		}
		if (!text.contains("@" + userName))
		{
			text = text + " @" + userName + " ";
		}
		setText(text);
		inReplyTo = -1;
	}

	public void setCursor(int index)
	{
		editTextTweet.setSelection(index);
	}
	
	public void toggle()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				menu.toggle();
			}
		}.post();
	}

	public void open()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				menu.showMenu();
			}
		}.post();
	}
	
	public void close()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				menu.showContent();
			}
		}.post();
	}

	public boolean isOpening()
	{
		return menu.isMenuShowing();
	}
	
	private void submit(final String text)
	{
		if (StringUtils.isNullOrEmpty(text))
		{
			ToastManager.getInstance().toast("‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢");
		}
		else
		{
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					StatusUpdate update = new StatusUpdate(text);
					if (inReplyTo >= 0)
					{
						update.setInReplyToStatusId(inReplyTo);
						inReplyTo = -1;
					}
					new AsyncTweetTask(update).addToQueue();
				}
			}.postDelayed(10);
		}
	}

	private void init()
	{
		editTextTweet = (EditText) menu.findViewById(R.id.editText_tweet);
		final TextView textViewCount = (TextView) menu.findViewById(R.id.textView_count);
		ImageButton imageButtonSubmit = (ImageButton) menu.findViewById(R.id.imageButton_submit);
		ImageButton imageButtonClear = (ImageButton) menu.findViewById(R.id.imageButton_clean);
		ImageButton imageButtonMenu = (ImageButton)menu.findViewById(R.id.imageButton_menu);

		menuAdapter = new TweetMenuAdapter(activity);
		textViewCount.setText("140");
		editTextTweet.setFocusable(true);
		editTextTweet.setTextSize(Client.getTextSize());
		editTextTweet.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				textViewCount.setText(String.valueOf(140 - s.length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			
			@Override
			public void afterTextChanged(Editable s){}
		});

		imageButtonSubmit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				submit(editTextTweet.getText().toString());
				editTextTweet.setText("");
				if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.AFTER_SUBMIT))
				{
					close();
				}
			}
		});

		imageButtonClear.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				editTextTweet.setText("");
			}
		});

		imageButtonMenu.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);
				menuAdapter.createMenuDialog(true).show();
			}
		});
	}

	private void createSlidingMenu()
	{
		menu = new SlidingMenu(activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		View rootView = LayoutInflater.from(activity).inflate(R.layout.tweet_layout, null);
		menu.setMenu(rootView);
		rootView.setOnTouchListener(new TweetViewTouchListener());
		menu.setOnClosedListener(new OnClosedListener()
		{
			
			@Override
			public void onClosed()
			{
				onCloseSlidingMenu();
			}
		});
		menu.setOnOpenedListener(new OnOpenedListener()
		{
			
			@Override
			public void onOpened()
			{
				onOpenSlidingMenu();
			}
		});
	}

	private void onOpenSlidingMenu()
	{
		editTextTweet.setTextSize(Client.getTextSize());
		if(getText().contains(" RT @"))
		{
			editTextTweet.setSelection(0);
		}
		editTextTweet.requestFocus();
		if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.OPEN_IME))
		{
			InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editTextTweet, 0);
		}
	}

	private void onCloseSlidingMenu()
	{
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);
	}

	public SlidingMenu getSlidingMenu()
	{
		return menu;
	}
}
