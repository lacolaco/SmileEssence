package net.miz_hi.smileessence.command;

import java.util.ArrayList;
import java.util.HashMap;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.EnumPreferenceKey.EnumValueType;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.CheckBoxListDialogHelper;
import net.miz_hi.smileessence.dialog.CheckBoxListDialogHelper.CheckBoxItem;
import net.miz_hi.smileessence.menu.StatusMenuAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class CommandEditMenu extends MenuCommand
{

	private Activity activity;
	
	public CommandEditMenu(Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	public String getName()
	{
		return "メニュー表示設定";
	}

	@Override
	public void workOnUiThread()
	{
		final CheckBoxListDialogHelper helper = new CheckBoxListDialogHelper(activity);
		helper.setTitle("表示する項目を設定してください");
		
		ArrayList<CheckBoxItem> list = new ArrayList<CheckBoxItem>();
		final HashMap<CheckBoxItem, MenuCommand> map = new HashMap<CheckBoxItem, MenuCommand>();
		StatusModel nullModel = StatusModel.getNullStatusModel();
		StatusMenuAdapter adapter = new StatusMenuAdapter(activity, nullModel);
		for(MenuCommand item : adapter.getStatusMenu())
		{
			if(item instanceof IHideable)
			{
				boolean startValue = Client.getPreferenceHelper().getPreferenceValue(item.getClass().getSimpleName(), EnumValueType.BOOLEAN, true);
				CheckBoxItem checkBoxItem = new CheckBoxItem(item.getName(), startValue);
				list.add(checkBoxItem);
				map.put(checkBoxItem, item);
			}
		}
		
		helper.setItems((list.toArray(new CheckBoxItem[0])));
		
		helper.setOnClicked(new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case DialogInterface.BUTTON_POSITIVE:
					{
						CheckBoxItem[] items = helper.getItems();
						for(CheckBoxItem item : items)
						{
							boolean value = item.value;
							MenuCommand command = map.get(item);
							Client.getPreferenceHelper().putPreferenceValue(command.getClass().getSimpleName(), EnumValueType.BOOLEAN, item.value);
						}
						dialog.dismiss();
						break;
					}
					default:
					{
						dialog.dismiss();
					}
				}
			}
		});
		
		helper.createDialog().show();
	}

}
