package net.miz_hi.smileessence.view;

import java.util.regex.Pattern;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.dialog.TweetMenuAdapter;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class TweetViewManager
{

	private SlidingMenu menu;
	private String text;
	private long inReplyTo;
	private Activity activity;
	private TextView textViewCount;
	private EditText editTextTweet;
	private ImageButton imageButtonSubmit;
	private ImageButton imageButtonClear;
	private ImageButton imageButtonMenu;
	private TweetMenuAdapter menuAdapter;

	public TweetViewManager(Activity activity)
	{
		this.activity = activity;
		menu = createSlidingMenu();
		text = "";
		inReplyTo = -1;
	}

	public void setText(String str)
	{
		text = str;
	}
	
	public void appendText(String str)
	{
		text = text + " " + str;
	}

	public void setInReplyToStatusId(long l)
	{
		inReplyTo = l;
	}

	public void init()
	{
		textViewCount = (TextView) menu.findViewById(R.id.textView_count);
		editTextTweet = (EditText) menu.findViewById(R.id.editText_tweet);
		imageButtonSubmit = (ImageButton) menu.findViewById(R.id.imageButton_submit);
		imageButtonClear = (ImageButton) menu.findViewById(R.id.imageButton_clean);
		imageButtonMenu = (ImageButton)menu.findViewById(R.id.imageButton_menu);

		menuAdapter = new TweetMenuAdapter(activity, this);
		
		textViewCount.setText("140");
		editTextTweet.setFocusable(true);
		editTextTweet.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				textViewCount.setText(String.valueOf(140 - s.length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});

		imageButtonSubmit.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				submit(editTextTweet.getText().toString());
				editTextTweet.setText("");
				MainActivity.getInstance().toggleTweetView();
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
				menuAdapter.createMenuDialog(true).show();
			}
		});
	}

	public void toggle()
	{
		menu.toggle();
	}

	public void open()
	{
		menu.showMenu();
	}

	public void close()
	{
		menu.showContent();
	}

	public boolean isOpening()
	{
		return menu.isMenuShowing();
	}

	private SlidingMenu createSlidingMenu()
	{
		SlidingMenu menu = new SlidingMenu(activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		View rootView = LayoutInflater.from(activity).inflate(R.layout.tweet_layout, null);
		menu.setMenu(rootView);

		menu.setOnCloseListener(new OnCloseListener()
		{

			@Override
			public void onClose()
			{
				onCloseSlidingMenu();
			}
		});
		menu.setOnOpenListener(new OnOpenListener()
		{

			@Override
			public void onOpen()
			{
				onOpenSlidingMenu();
			}
		});
		return menu;
	}

	private void onOpenSlidingMenu()
	{
		if (!StringUtils.isNullOrEmpty(text))
		{
			editTextTweet.setText(text);
			text = "";
		}
		editTextTweet.setTextSize(Client.getTextSize());
		editTextTweet.invalidate();
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editTextTweet, InputMethodManager.SHOW_IMPLICIT);
		editTextTweet.requestFocus();
	}

	private void onCloseSlidingMenu()
	{
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextTweet.getWindowToken(), 0);
		if (!StringUtils.isNullOrEmpty(editTextTweet.getText().toString()))
		{
			text = editTextTweet.getText().toString();
		}
	}

	private void submit(String text)
	{
		if (StringUtils.isNullOrEmpty(text))
		{
			Toast.makeText(activity, "‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢", Toast.LENGTH_SHORT).show();
		}
		else
		{
			StatusUpdate update = new StatusUpdate(text);
			if (inReplyTo >= 0)
			{
				update.setInReplyToStatusId(inReplyTo);
				inReplyTo = -1;
			}
			new AsyncTweetTask(update).addToQueue();
		}
	}
	
	public void addReply(String userName, long l, boolean append)
	{
		Pattern hasReply = Pattern.compile("^@([a-zA-Z0-9_]+).*");
		if (append || hasReply.matcher(text).find())
		{
			if (!text.contains("@" + userName))
			{
				text = text + "@" + userName + " ";
				if (!text.startsWith("."))
				{
					text = "." + text;
				}
			}
		}
		else
		{
			text = "@" + userName + " ";
		}
		inReplyTo = l;
	}
	
	public void openToReply(String userName, long l, boolean append)
	{
		addReply(userName, l, append);
		open();
	}

	public String getText()
	{
		return text;
	}

	public EditText getEditTextTweet()
	{
		return editTextTweet;
	}
}
