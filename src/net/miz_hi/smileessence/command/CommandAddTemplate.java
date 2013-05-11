package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.data.template.Templates;

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
		Notifier.info("’Ç‰Á‚µ‚Ü‚µ‚½");
	}
}
