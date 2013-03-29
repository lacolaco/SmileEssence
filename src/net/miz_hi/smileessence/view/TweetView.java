package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.listener.TweetViewTouchListener;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.util.UiHandler;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class TweetView
{

	private static TweetView instance;
	private SlidingMenu menu;
	private Activity activity;
	private EditText editTextTweet;
	private ListView listViewReply;
	private LinearLayout linearLayoutObj;

	private TweetView(Activity activity)
	{
		this.activity = activity;
		init();
	}
	
	public static void init(Activity activity)
	{
		instance = new TweetView(activity);
	}
	
	public static TweetView getInstance()
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
	
	public int getCursor()
	{
		return editTextTweet.getSelectionEnd();
	}

	public void setCursor(int index)
	{
		editTextTweet.setSelection(index);
	}
	
	public void removeObjects()
	{
		linearLayoutObj.removeAllViews();
	}
	
	public void setInReplyToStatus(StatusModel status)
	{
		linearLayoutObj.removeAllViews();
		View viewStatus = StatusViewFactory.getView(activity.getLayoutInflater(), status);
		linearLayoutObj.addView(viewStatus);
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

	private void init()
	{
		TweetSystem.start(activity);
		createSlidingMenu();
		editTextTweet = (EditText) menu.findViewById(R.id.editText_tweet);
		final TextView textViewCount = (TextView) menu.findViewById(R.id.textView_count);
		ImageButton imageButtonSubmit = (ImageButton) menu.findViewById(R.id.imageButton_submit);
		ImageButton imageButtonClear = (ImageButton) menu.findViewById(R.id.imageButton_clean);
		ImageButton imageButtonMenu = (ImageButton)menu.findViewById(R.id.imageButton_menu);
		ImageButton imageButtonPict = (ImageButton)menu.findViewById(R.id.imageButton_pict);
		ImageButton imageButtonCamera = (ImageButton)menu.findViewById(R.id.imageButton_camera);
		linearLayoutObj = (LinearLayout)menu.findViewById(R.id.linearLayout_tweet_obj);
		
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
				TweetSystem.getInstance().setText(editTextTweet.getText().toString());
				TweetSystem.getInstance().submit();
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
				TweetSystem.getInstance().clear();
			}
		});

		imageButtonMenu.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);	
				TweetMenu.getInstance().createMenuDialog(true).show();
			}
		});
	}
	
	private void onOpeningMenu()
	{
		String text = TweetSystem.getInstance().getText();
		editTextTweet.setText(text);
		
		if(text.startsWith(" RT"))
		{
			editTextTweet.setSelection(0);
		}
		else
		{
			editTextTweet.setSelection(text.length());
		}
	}

	private void onOpenedMenu()
	{
		editTextTweet.setTextSize(Client.getTextSize());
		editTextTweet.requestFocus();
				
		if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.OPEN_IME))
		{
			InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(editTextTweet, 0);
		}
	}
	
	private void onClosingMenu()
	{
		TweetSystem.getInstance().setText(editTextTweet.getText().toString());
	}

	private void onClosedMenu()
	{
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);
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

		menu.setOnOpenListener(new OnOpenListener()
		{
			
			@Override
			public void onOpen()
			{
				onOpeningMenu();
			}
		});
		menu.setOnOpenedListener(new OnOpenedListener()
		{
			
			@Override
			public void onOpened()
			{
				onOpenedMenu();
			}
		});
		menu.setOnCloseListener(new OnCloseListener()
		{
			
			@Override
			public void onClose()
			{
				onClosingMenu();				
			}
		});
		menu.setOnClosedListener(new OnClosedListener()
		{
			
			@Override
			public void onClosed()
			{
				onClosedMenu();
			}
		});
	}

	public SlidingMenu getSlidingMenu()
	{
		return menu;
	}
}
