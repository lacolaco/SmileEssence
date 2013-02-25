package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.TemplateActivity;
import android.app.Activity;
import android.content.Intent;

public class MenuItemEditTemplate extends MenuItemBase
{

	public MenuItemEditTemplate(Activity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "íËå^ï∂ÇÃä«óù";
	}

	@Override
	public void work()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Intent intent = new Intent(activity, TemplateActivity.class);
				activity.startActivity(intent);
			}
		}.post();
	}

}
