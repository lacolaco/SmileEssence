package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;

public abstract class StatusMenuItemBase extends MenuItemBase
{
	protected StatusModel model;
	
	public StatusMenuItemBase(EventHandlerActivity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter);
		this.model = model;
	}
	
	@Override
	public abstract boolean isVisible();
}
