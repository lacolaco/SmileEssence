package net.miz_hi.warotter.dialog;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.menuitem.MenuItemAccountReset;
import net.miz_hi.warotter.menuitem.MenuItemSetting;
import net.miz_hi.warotter.model.Warotter;
import android.app.Activity;
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
		viewTitle.setTextColor(Warotter.getResource().getColor(R.color.White));
		viewTitle.setText(title);
		
		list.add(new MenuItemSetting(activity, this));
		list.add(new MenuItemAccountReset(activity, this));
		return super.createMenuDialog(viewTitle);
	}

}
