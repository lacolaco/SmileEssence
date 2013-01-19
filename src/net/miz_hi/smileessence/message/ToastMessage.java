package net.miz_hi.smileessence.message;

import net.miz_hi.smileessence.core.Message;

public class ToastMessage implements Message
{
	public String text;

	public ToastMessage(String string)
	{
		this.text = string;
	}

}
