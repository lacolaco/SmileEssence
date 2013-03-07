package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.event.ToastManager;

public class CommandAddTemplate extends MenuCommand
{

	private String text;

	public CommandAddTemplate(String text)
	{
		this.text = text;
	}

	@Override
	public String getName()
	{
		return "’èŒ^•¶‚É’Ç‰Á";
	}

	@Override
	public void workOnUiThread()
	{
		Templates.addTemplate(text);
		ToastManager.getInstance().toast("’Ç‰Á‚µ‚Ü‚µ‚½");
	}
}
