package net.miz_hi.warotter.core;

import android.widget.Toast;

public class ToastMessage
{
	public final String text;
	public final int duration;

	public ToastMessage(String text)
	{
		this(text, Toast.LENGTH_SHORT);
	}

	public ToastMessage(String text, int duration)
	{
		this.text = text;
		this.duration = duration;
	}
}
