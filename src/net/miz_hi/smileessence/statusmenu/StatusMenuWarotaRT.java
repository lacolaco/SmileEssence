package net.miz_hi.smileessence.statusmenu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.model.StatusModel;

public class StatusMenuWarotaRT extends StatusMenuItemBase
{

	public StatusMenuWarotaRT(EventHandlerActivity activity, DialogAdapter factory, StatusModel model)
	{
		super(activity, factory, model);
	}

	@Override
	public String getText()
	{
		return "ÉèÉçÉ^éÆRT";
	}

	@Override
	public void work()
	{
		
	}

	@Override
	public boolean isVisible()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
