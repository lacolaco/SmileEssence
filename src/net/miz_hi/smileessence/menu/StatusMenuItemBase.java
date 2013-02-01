package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;

public abstract class StatusMenuItemBase extends MenuItemBase
{
	protected StatusModel model;
	
	public StatusMenuItemBase(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter);
		this.model = model;
	}
	
	@Override
	public abstract boolean isVisible();
}
