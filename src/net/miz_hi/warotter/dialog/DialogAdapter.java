package net.miz_hi.warotter.dialog;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.menuitem.MenuItemBase;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class DialogAdapter
{
	protected EventHandlerActivity activity;
	protected List<MenuItemBase> list;
	protected static LayoutInflater layoutInflater;
	protected static Dialog dialog;

	public DialogAdapter(EventHandlerActivity activity)
	{
		this.activity = activity;
		layoutInflater = LayoutInflater.from(activity);
		list = new ArrayList<MenuItemBase>();
	}

	public DialogAdapter setMenuItems(List<? extends MenuItemBase> list)
	{
		this.list.clear();
		this.list.addAll(list);
		return this;
	}
	
	public List<MenuItemBase> getList()
	{
		return list;
	}

	public abstract Dialog createMenuDialog();

	protected Dialog createMenuDialog(View viewTitle)
	{
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = layoutInflater.inflate(R.layout.dialog_menu_layout, null);
		LinearLayout titleLinearLayout = (LinearLayout)view.findViewById(R.id.linearLayout_dialogTitle);
		LinearLayout itemsLinearLayout = (LinearLayout)view.findViewById(R.id.linearLayout_dialogItems);
		titleLinearLayout.addView(viewTitle);
		for (MenuItemBase item : list)
		{
			itemsLinearLayout.addView(new MenuItemView(activity, item).getView());
		}
		itemsLinearLayout.setClickable(true);
		dialog.setContentView(view);
		LayoutParams lp = dialog.getWindow().getAttributes();
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		lp.width = (int) (metrics.widthPixels * 0.9);
		lp.height = (int) (metrics.heightPixels * 0.9);
		return dialog;
	}

	public void dispose()
	{
		if (dialog != null)
			dialog.dismiss();
	}
	
	public static class MenuItemView
	{
		private MenuItemBase item;
		private LayoutInflater layoutInflater;

		public MenuItemView(Activity activity, MenuItemBase item)
		{
			this.item = item;
			layoutInflater = LayoutInflater.from(activity);
		}

		public View getView()
		{
			View view = layoutInflater.inflate(R.layout.menuitem_layout, null);
			TextView textView = (TextView) view.findViewById(R.id.textView_menuItem);
			textView.setText(item.getText());
			view.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					view.setBackgroundColor(Color.rgb(30, 30, 120));
					item.run();
				}
			});
			return view;
		}

	}
}
