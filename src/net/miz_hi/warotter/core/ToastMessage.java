package net.miz_hi.warotter.core;

public class ToastMessage implements Message
{
	public String text;

	public ToastMessage(String string)
	{
		this.text = string;
	}

}
