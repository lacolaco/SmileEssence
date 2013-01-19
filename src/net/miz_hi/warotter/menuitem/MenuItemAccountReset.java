package net.miz_hi.warotter.menuitem;

import net.miz_hi.warotter.auth.AuthentificationDB;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.dialog.DialogAdapter;
import net.miz_hi.warotter.model.Warotter;

public class MenuItemAccountReset extends MenuItemBase
{

	public MenuItemAccountReset(EventHandlerActivity activity, DialogAdapter factory)
	{
		super(activity, factory);
	}

	@Override
	public String getText()
	{
		return "認証情報のリセット";
	}

	@Override
	public void work()
	{
		toast("全ての認証情報をリセットします。再起動してください");
		AuthentificationDB.instance().deleteAll();
		activity.finish();
	}

}
