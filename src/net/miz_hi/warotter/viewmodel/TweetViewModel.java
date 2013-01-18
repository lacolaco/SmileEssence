package net.miz_hi.warotter.viewmodel;

import com.slidingmenu.lib.SlidingMenu;

import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.Observable;
import gueei.binding.converters.STITCH;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.async.AsyncTweetTask;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.util.StringUtils;
import net.miz_hi.warotter.view.MainActivity;
import twitter4j.StatusUpdate;
import android.content.Context;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TweetViewModel extends ViewModel
{
	public TweetViewModel()
	{
	}

	public StringObservable text = new StringObservable("");
	public DependentObservable<String> textCount = new DependentObservable<String>(String.class, text)
	{

		@Override
		public String calculateValue(Object... arg0) throws Exception
		{
			return String.valueOf(140 - text.get().length());
		}
	};
	
	@Override
	public void onActivityCreated(EventHandlerActivity activity)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityDestroy(EventHandlerActivity activity)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void init(EventHandlerActivity activity, SlidingMenu menu)
	{
		final TextView viewCount = (TextView)menu.findViewById(R.id.textView_count);
		final EditText viewEdit = (EditText)menu.findViewById(R.id.editText_tweet);
		final ImageButton viewSubmit = (ImageButton)menu.findViewById(R.id.imageButton_submit);
		final ImageButton viewWarota = (ImageButton)menu.findViewById(R.id.imageButton_warota);
		final ImageButton viewMorse = (ImageButton)menu.findViewById(R.id.imageButton_morse);
		final ImageButton viewTemplate = (ImageButton)menu.findViewById(R.id.imageButton_template);
		final ImageButton viewReplace = (ImageButton)menu.findViewById(R.id.imageButton_replace);
		final ImageButton viewHashtag = (ImageButton)menu.findViewById(R.id.imageButton_hashtag);
		
		viewCount.setText("140");
		viewEdit.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				viewCount.setText(String.valueOf(140 - s.length()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			
			@Override
			public void afterTextChanged(Editable s){}
		});
		
		viewSubmit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				submit(viewEdit.getText().toString());
				InputMethodManager imm = (InputMethodManager) Warotter.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(viewEdit.getWindowToken(), 0);
				messenger.raise("toggle", null);
				viewEdit.setText("");			
			}
		});
	}

	@Override
	public boolean onEvent(String eventName, EventHandlerActivity activity)
	{
		MainActivity mainActivity = (MainActivity)activity;
		return false;		
	}

	private void submit(String text)
	{
		if (StringUtils.isNullOrEmpty(text))
		{
			toast("ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢");
		}
		else
		{
			AsyncTweetTask.addTask(new AsyncTweetTask(new StatusUpdate(text), this));
		}
	}

	public Command commandWarota = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
//			StringBuilder sb = new StringBuilder(text.get());
//			sb.insert(cursorPos.get() != null ? cursorPos.get() : 0, "ÉèÉçÉ^Çó");
//			text.set(sb.toString());

		}
	};

	public Command commandReplace = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

	public Command commandTemplate = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

	public Command commandMorse = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

	public Command commandHashtag = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

}
