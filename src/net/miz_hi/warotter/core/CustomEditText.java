package net.miz_hi.warotter.core;

import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import android.content.Context;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CustomEditText extends EditText implements IBindableView<CustomEditText>
{
	private IBinder _token;
	private int _cursorPos = 0;

	public CustomEditText(Context context)
	{
		super(context);
	}
	
	public CustomEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public ViewAttribute<CustomEditText, ?> createViewAttribute(String arg0)
	{
		if(arg0.equals("token"))
		{
			return token;
		}
		else if(arg0.equals("cursorPos"))
		{
			return cursorPos;
		}
		return null;
	}
	
	@Override
	public void onFinishInflate()
	{
		super.onFinishInflate();
		cursorPos.set(0);
		setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				token.set(v.getWindowToken());				
			}
		});
		setOnKeyListener(new OnKeyListener()
		{
			
			@Override
			public boolean onKey(View v, int arg1, KeyEvent arg2)
			{
				cursorPos.set(((TextView) v).getSelectionStart());	
				return false;
			}
		});
		setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				cursorPos.set(((TextView) v).getSelectionStart());				
			}
		});
	}
	
	public ViewAttribute<CustomEditText, IBinder> token = 
			new ViewAttribute<CustomEditText, IBinder>(IBinder.class, CustomEditText.this, "token")
	{

		@Override
		protected void doSetAttributeValue(Object arg0)
		{			
			mHost._token = (IBinder) arg0;
		}

		@Override
		public IBinder get()
		{
			return mHost._token;
		}

	};

	public ViewAttribute<CustomEditText, Integer> cursorPos = 
			new ViewAttribute<CustomEditText, Integer>(Integer.class, CustomEditText.this, "cursorPos")
	{

		@Override
		public Integer get()
		{
			return Integer.valueOf(mHost._cursorPos);
		}

		@Override
		protected void doSetAttributeValue(Object arg0)
		{
			if(arg0 != null)
			{
				mHost._cursorPos = (Integer)arg0;
			}
		}
	};
}
