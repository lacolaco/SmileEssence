package net.miz_hi.smileessence.statusmenu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.model.StatusModel;
import net.miz_hi.smileessence.optionmenu.MenuItemBase;

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
