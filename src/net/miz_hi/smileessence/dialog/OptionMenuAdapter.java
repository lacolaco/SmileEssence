package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.model.Client;
import net.miz_hi.smileessence.optionmenu.MenuItemAccountReset;
import net.miz_hi.smileessence.optionmenu.MenuItemSetting;
import android.app.Dialog;
import android.widget.TextView;

public class OptionMenuAdapter extends DialogAdapter
{
	private String title;
	private int textSize = 15;
	
	public OptionMenuAdapter(EventHandlerActivity activity, String title)
	{
		super(activity);
		this.title = title;
	}

	@Override
	public Dialog createMenuDialog()
	{
		TextView viewTitle = new TextView(activity);
		viewTitle.setTextSize(textSize);
		viewTitle.setTextColor(Client.getResource().getColor(R.color.White));
		viewTitle.setText(title);
		viewTitle.setPadding(5, 10, 0, 10);
		list.add(new MenuItemSetting(activity, this));
		list.add(new MenuItemAccountReset(activity, this));
		return super.createMenuDialog(viewTitle);
	}

}
