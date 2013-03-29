package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.UiHandler;

public abstract class MenuCommand implements ICommand
{

	@Override
	public boolean getDefaultVisibility()
	{
		return true;
	}

	@Override
	public final void run()
	{
		new UiHandler()
		{

			@Override
			public void run()
			{
				DialogAdapter.dispose();
				workOnUiThread();
			}
		}.post();
	}

	public abstract void workOnUiThread();

}
