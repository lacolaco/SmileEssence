package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;

public abstract class StatusMenuItemBase extends MenuItemBase
{
	protected StatusModel model;
	
	public StatusMenuItemBase(EventHandlerActivity activity, DialogAdapter factory, StatusModel model)
	{
		super(activity, factory);
		this.model = model;
	}
	
	public abstract boolean isVisible();
}
