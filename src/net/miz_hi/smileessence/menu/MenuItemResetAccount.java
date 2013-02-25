package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.dialog.YesNoDialogHelper;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class MenuItemResetAccount extends MenuItemBase
{

	public MenuItemResetAccount(Activity activity, DialogAdapter factory)
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
		YesNoDialogHelper helper = new YesNoDialogHelper(activity, "本当にリセットしますか？");
		helper.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
					case DialogInterface.BUTTON_POSITIVE:
					{
						Toast.makeText(activity, "全ての認証情報をリセットします。再起動してください", Toast.LENGTH_SHORT).show();
						AuthentificationDB.instance().deleteAll();
						activity.finish();
						break;
					}
					default:
					{
						dialog.dismiss();
					}
				}

			}
		});
		helper.createYesNoAlert().show();
	}

}
