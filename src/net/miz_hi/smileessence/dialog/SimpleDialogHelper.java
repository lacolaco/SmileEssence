package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.R;
import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

public class SimpleDialogHelper
{

	public static Dialog createDialog(Activity activity, View titleView, View contentView)
	{
		Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(activity).inflate(R.layout.dialog_base_layout, null);
		LinearLayout titleLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_dialogTitle);
		LinearLayout itemsLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_dialogItems);
		titleLinearLayout.addView(titleView);
		itemsLinearLayout.addView(contentView);
		dialog.setContentView(view);
		LayoutParams lp = dialog.getWindow().getAttributes();
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		lp.width = (int) (metrics.widthPixels * 0.9);
		lp.gravity = Gravity.CENTER;
		return dialog;
	}

}
