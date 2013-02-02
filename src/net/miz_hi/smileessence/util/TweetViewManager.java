package net.miz_hi.smileessence.util;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.ConcurrentAsyncTaskHelper;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.ViewModel;
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
	
	private SlidingMenu _menu;
	private String _text;
	private long _inReplyTo;
	private Activity _activity;
	private TextView _countView;
	private EditText _editView;
	private ImageButton _submitImage;
	private ImageButton _clearImage;
	private ImageButton _warotaImage;
	
	public TweetViewManager(Activity activity)
	{
		_activity = activity;
		_menu = createSlidingMenu();
		_text = "";
		_inReplyTo = -1;
		
	}
	
	public void setText(String str)
	{
		_text = str;
	}
	
	public void setInReplyToStatusId(long l)
	{
		_inReplyTo = l;
	}
	
	public void init()
	{
		_countView = (TextView)_menu.findViewById(R.id.textView_count);
		_editView = (EditText)_menu.findViewById(R.id.editText_tweet);
		_submitImage = (ImageButton)_menu.findViewById(R.id.imageButton_submit);
		_warotaImage = (ImageButton)_menu.findViewById(R.id.imageButton_warota);
		_clearImage = (ImageButton)_menu.findViewById(R.id.imageButton_clean);
		
		_countView.setText("140");
		_editView.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				_countView.setText(String.valueOf(140 - s.length()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			
			@Override
			public void afterTextChanged(Editable s){}
		});
		
		_submitImage.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				submit(_editView.getText().toString());
				_editView.setText("");	
				MainActivity.getInstance().toggleTweetView();
			}
		});
		
		_clearImage.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				_editView.setText("");				
			}
		});
		
		_warotaImage.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int cursor = _editView.getSelectionEnd();
				StringBuilder sb = new StringBuilder(_editView.getText().toString());
				sb.insert(cursor, "ƒƒƒ^‚—");
				_editView.setText(sb.toString());
				cursor = cursor + sb.length();
				if(cursor > _editView.getText().length())
				{
					cursor = _editView.getText().length();
				}
				_editView.setSelection(cursor);
			}
		});
	}
	
	public void toggle()
	{
		_menu.toggle();
	}
	
	public void open()
	{
		_menu.showMenu();
	}
	
	public void close()
	{
		_menu.showContent();
	}
	
	public boolean isOpening()
	{
		return _menu.isMenuShowing();
	}
	
	private SlidingMenu createSlidingMenu()
	{
		SlidingMenu menu = new SlidingMenu(_activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.attachToActivity(_activity, SlidingMenu.SLIDING_WINDOW);
		View rootView = LayoutInflater.from(_activity).inflate(R.layout.tweet_layout, null);
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
		if(!StringUtils.isNullOrEmpty(_text))
		{				
			_editView.setText(_text);
			_text = "";
		}
		_editView.setTextSize(Client.getTextSize());
		_editView.invalidate();
	}

	private void onCloseSlidingMenu()
	{
		InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(_editView.getWindowToken(), 0);
		if(!StringUtils.isNullOrEmpty(_editView.getText().toString()))
		{
			_text = _editView.getText().toString();
		}
	}

	private void submit(String text)
	{
		if (StringUtils.isNullOrEmpty(text))
		{
			Toast.makeText(_activity, "‰½‚©“ü—Í‚µ‚Ä‚­‚¾‚³‚¢", Toast.LENGTH_SHORT).show();
		}
		else
		{
			StatusUpdate status = new StatusUpdate(text);
			if(_inReplyTo >= 0)
			{
				status.setInReplyToStatusId(_inReplyTo);
				_inReplyTo = -1;
			}
			ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncTweetTask(status));
		}
	}

	public String getText()
	{
		return _text;
	}
}
