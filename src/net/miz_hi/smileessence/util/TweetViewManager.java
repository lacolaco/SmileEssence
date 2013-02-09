package net.miz_hi.smileessence.util;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.async.AsyncTweetTask;
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
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class TweetViewManager
{

	private SlidingMenu menu;
	private String text;
	private long inReplyTo;
	private Activity activity;
	private TextView countView;
	private EditText editView;
	private ImageButton submitImage;
	private ImageButton clearImage;
	private ImageButton warotaImage;

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

	public void setInReplyToStatusId(long l)
	{
		inReplyTo = l;
	}

	public void init()
	{
		countView = (TextView) menu.findViewById(R.id.textView_count);
		editView = (EditText) menu.findViewById(R.id.editText_tweet);
		submitImage = (ImageButton) menu.findViewById(R.id.imageButton_submit);
		warotaImage = (ImageButton) menu.findViewById(R.id.imageButton_warota);
		clearImage = (ImageButton) menu.findViewById(R.id.imageButton_clean);

		countView.setText("140");
		editView.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				countView.setText(String.valueOf(140 - s.length()));
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

		submitImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				submit(editView.getText().toString());
				editView.setText("");
				MainActivity.getInstance().toggleTweetView();
			}
		});

		clearImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				editView.setText("");
			}
		});

		warotaImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				int cursor = editView.getSelectionEnd();
				StringBuilder sb = new StringBuilder(editView.getText().toString());
				sb.insert(cursor, "ƒƒƒ^‚—");
				editView.setText(sb.toString());
				cursor = cursor + sb.length();
				if (cursor > editView.getText().length())
				{
					cursor = editView.getText().length();
				}
				editView.setSelection(cursor);
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
			editView.setText(text);
			text = "";
		}
		editView.setTextSize(Client.getTextSize());
		editView.invalidate();
	}

	private void onCloseSlidingMenu()
	{
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editView.getWindowToken(), 0);
		if (!StringUtils.isNullOrEmpty(editView.getText().toString()))
		{
			text = editView.getText().toString();
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

	public String getText()
	{
		return text;
	}
}
