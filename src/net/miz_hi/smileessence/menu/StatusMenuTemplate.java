package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.widget.Toast;

public class StatusMenuTemplate extends StatusMenuItemBase
{

	public StatusMenuTemplate(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "–{•¶‚ð’èŒ^•¶‚É’Ç‰Á";
	}

	@Override
	public void work()
	{
		Templates.addTemplate(model.text);
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Toast.makeText(activity, "’Ç‰Á‚µ‚Ü‚µ‚½", Toast.LENGTH_SHORT).show();
			}
		}.post();
	}

}
