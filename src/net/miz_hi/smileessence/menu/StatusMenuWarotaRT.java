package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;

public class StatusMenuWarotaRT extends StatusMenuItemBase
{

	public StatusMenuWarotaRT(EventHandlerActivity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
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
