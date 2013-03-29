package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.TweetView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TweetViewTouchListener implements OnTouchListener
{

	// XˆÊ’u‚Ì“®ì”»’f
	private float MARGIN_X = 60.0f;
	// G‚ê‚½uŠÔ‚ÌXˆÊ’u
	private float mDownX;
	// G‚ê‚½uŠÔ‚ÌYˆÊ’u
	private float mDownY;
	// ‰EˆÚ“®
	private final static int MOVE_RIGHT = 1;
	// ¶ˆÚ“®
	private final static int MOVE_LEFT = -1;

	public boolean onTouch(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				actionDown(event);					
			}
			case MotionEvent.ACTION_UP:
			{
				return actionUp(event);
			}
		}
		return false;
	}
	
	private void actionDown(MotionEvent event)
	{
		// ˆÊ’u‚ð•Û‘¶
		mDownX = event.getX();
		mDownY = event.getY();
	}

	private boolean actionUp(MotionEvent event)
	{
		// Œ»ÝˆÊ’u‚Æ ACTION_DOWN Žž‚ÌˆÊ’u‚ð”äŠr
		float moveX = mDownX - event.getX();
		float moveY = mDownY - event.getY();
		// ˆÚ“®‹——£‚ª”ÍˆÍ“à‚È‚ç–³Ž‹
		if (Math.abs(moveX) > MARGIN_X )
		{
			changeList(moveX > 0 ? MOVE_RIGHT : MOVE_LEFT);
			return true;
		}
		return false;
	}

	private void changeList(int i)
	{
		switch (i)
		{
			case MOVE_LEFT:
			{
				break;
			}
			case MOVE_RIGHT:
			{
				TweetView.getInstance().close();
				break;
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		return onTouch(event);
	}

}
