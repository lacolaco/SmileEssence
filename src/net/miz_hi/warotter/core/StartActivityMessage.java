package net.miz_hi.warotter.core;

import android.content.Intent;

public class StartActivityMessage
{
	public final Intent intent;
	public final Class clazz;
	public final int reqCode;
	public final ActivityCallback callback;

	public StartActivityMessage(Intent intent, Class clazz)
	{
		this(intent, clazz, -1, null);
	}

	public StartActivityMessage(Intent intent, Class clazz, int reqCode, ActivityCallback callback)
	{
		this.intent = intent;
		this.clazz = clazz;
		this.reqCode = reqCode;
		this.callback = callback;
	}

	public boolean hasCallback()
	{
		return callback != null;
	}
}
