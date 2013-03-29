package net.miz_hi.smileessence.util;

import android.os.Handler;
import android.os.Looper;

public abstract class UiHandler extends Handler implements Runnable
{
	public UiHandler()
	{
		super(Looper.getMainLooper());
	}

	public UiHandler(Handler.Callback callback)
	{
		super(Looper.getMainLooper(), callback);
	}

	public boolean post()
	{
		return post(this);
	}

	public boolean postAtFrontOfQueue()
	{
		return postAtFrontOfQueue(this);
	}

	public boolean postAtTime(Object token, long uptimeMillis)
	{
		return postAtTime(this, token, uptimeMillis);
	}

	public boolean postAtTime(long uptimeMillis)
	{
		return postAtTime(this, uptimeMillis);
	}

	public boolean postDelayed(long delayMillis)
	{
		return postDelayed(this, delayMillis);
	}

	@Override
	public abstract void run();
}
