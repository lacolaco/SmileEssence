package net.miz_hi.smileessence.message;

import android.widget.Toast;
import net.miz_hi.smileessence.core.Message;

public class ToastMessage implements Message
{
	public String text;
	public int duration;

	public ToastMessage(String string)
	{
		this(string, Toast.LENGTH_SHORT);
	}
	
	public ToastMessage(String string, int duration)
	{
		this.text = string;
		this.duration = duration;
	}

}
