package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.CommandMenuClose;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.preference.PreferenceHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class DialogAdapter
{
	
	protected Activity activity;
	protected List<ICommand> list;
	protected static LayoutInflater layoutInflater;
	protected static Dialog dialog;
	protected View[] titleViews;

	public DialogAdapter(Activity activity)
	{
		this.activity = activity;
		this.layoutInflater = LayoutInflater.from(activity);
		this.list = new ArrayList<ICommand>();
	}

	public DialogAdapter setMenuItems(List<? extends ICommand> list)
	{
		this.list.clear();
		this.list.addAll(list);
		return this;
	}

	public List<ICommand> getList()
	{
		return list;
	}

	public static Dialog getDialog()
	{
		return dialog;
	}

	public boolean isShowing()
	{
		if (dialog == null)
		{
			return false;
		}
		else
		{
			return dialog.isShowing();
		}
	}
	
	public void setTitle(View... views)
	{
		titleViews = views;
	}
	
	public View[] getTitleViews()
	{
		return titleViews; 
	}
	
	public void setTitle(String string)
	{
		TextView title = new TextView(activity);
		title.setTextSize(15);
		title.setTextColor(Client.getResource().getColor(R.color.White));
		title.setText(string);
		title.setPadding(10, 15, 0, 15);
		setTitle(title);
	}

	public abstract Dialog createMenuDialog(boolean init);
	
	protected Dialog createMenuDialog()
	{
		dispose();
		
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = layoutInflater.inflate(R.layout.dialog_base_layout, null);
		LinearLayout titleLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_dialogTitle);
		LinearLayout itemsLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_dialogItems);
		
		titleLinearLayout.removeAllViews();
		itemsLinearLayout.removeAllViews();
		
		for (View v : titleViews)
		{			
			if(v.getParent() != null)
			{
				((ViewGroup)v.getParent()).removeView(v);
			}
			titleLinearLayout.addView(v);
		}

		for (ICommand item : list)
		{
			boolean isEnabled = true;
			
			if(item instanceof IHideable)
			{
				PreferenceHelper pref = Client.getPreferenceHelper();
				isEnabled = pref.getPreferenceValue(item.getClass().getSimpleName(), EnumValueType.BOOLEAN, false);
			}
			
			if(item.getDefaultVisibility() && isEnabled)
			{
				itemsLinearLayout.addView(new MenuItemView(activity, item).getView());
			}
		}
		itemsLinearLayout.addView(new MenuItemView(activity, new CommandMenuClose()).getView());
		itemsLinearLayout.setClickable(true);
		dialog.setContentView(view);
		LayoutParams lp = dialog.getWindow().getAttributes();
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		lp.width = (int) (metrics.widthPixels * 0.95);
		lp.gravity = Gravity.BOTTOM;
		return dialog;
	}

	public static void dispose()
	{
		if (dialog != null)
		{
			dialog.dismiss();
		}
	}

	public static class MenuItemView
	{
		private Activity activity;
		private ICommand item;
		private LayoutInflater layoutInflater;

		public MenuItemView(Activity activity, ICommand item)
		{
			this.activity = activity;
			this.item = item;
			layoutInflater = LayoutInflater.from(activity);
		}

		public View getView()
		{
			View view = layoutInflater.inflate(R.layout.menuitem_layout, null);
			TextView textView = (TextView) view.findViewById(R.id.textView_menuItem);
			textView.setText(item.getName());
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					view.setBackgroundColor(Color.rgb(30, 30, 120));
					if(item instanceof IConfirmable && Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
					{
						YesNoDialogHelper.show(activity, item.getName(), "é¿çsÇµÇ‹Ç∑Ç©ÅH", new Runnable()
						{
							public void run()
							{
								item.run();
							}
						},
						new Runnable()
						{
							
							@Override
							public void run()
							{
								dispose();
							}
						});
					}
					else
					{
						item.run();
					}
				}
			});
			return view;
		}

	}
}
