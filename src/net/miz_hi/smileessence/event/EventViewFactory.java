package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.EventOnClickListener;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.ColorUtils;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventViewFactory
{
	private static View viewToast;

	public static View getView(Activity _activity, EventModel model)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(_activity);
		if (viewToast == null)
		{
			viewToast = layoutInflater.inflate(R.layout.eventtoast_layout, null);
		}

		int black = ColorUtils.setAlpha(Client.getResource().getColor(R.color.Black), 180);
		int gray = ColorUtils.setAlpha(Client.getResource().getColor(R.color.LightGray), 200);

		TextView viewText = (TextView) viewToast.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewToast.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewToast.findViewById(R.id.linearLayout_toastBody);

		viewToast.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		bodyLayout.removeAllViews();
		viewText.setText(model.source.getScreenName() + model.type.getText());
		viewText.setTextColor(Client.getResource().getColor(R.color.White));

		if (model.targetModel == null)
		{
			baseLayout.setVisibility(View.GONE);
		}
		else
		{
			baseLayout.setVisibility(View.VISIBLE);
			View viewStatus = StatusViewFactory.getView(layoutInflater, model.targetModel);
			bodyLayout.addView(viewStatus);
		}

		DisplayMetrics metrics = _activity.getResources().getDisplayMetrics();
		viewText.setWidth((int) (metrics.widthPixels * 0.8));

		viewToast.setOnClickListener(new EventOnClickListener(_activity, model));

		return viewToast;
	}
}
