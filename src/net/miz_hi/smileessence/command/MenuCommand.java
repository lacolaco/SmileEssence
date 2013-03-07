package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.dialog.DialogAdapter;

public abstract class MenuCommand implements ICommand
{

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
