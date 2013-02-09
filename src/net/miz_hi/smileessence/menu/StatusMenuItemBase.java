package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;

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
