package net.miz_hi.warotter.message;

import net.miz_hi.warotter.core.Message;

public class ToastMessage implements Message
{
	public String text;

	public ToastMessage(String string)
	{
		this.text = string;
	}

}
