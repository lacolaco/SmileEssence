package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.ColorUtils;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventViewFactory
{

	public static View getToastView(Activity activity, EventModel model, View viewBase)
	{
		if(model instanceof StatusEventModel)
		{
			return getToastView(activity, (StatusEventModel)model, viewBase);
		}
		else if(model instanceof UserEvent)
		{
			return getToastView(activity, (UserEvent)model, viewBase);
		}
		else
		{
			return viewBase;
		}
	}
	
	private static View getToastView(Activity activity, StatusEventModel model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		if (viewBase == null)
		{
			viewBase = layoutInflater.inflate(R.layout.eventtoast_layout, null);
		}

		int black = ColorUtils.setAlpha(Client.getResource().getColor(R.color.Black), 180);
		int gray = ColorUtils.setAlpha(Client.getResource().getColor(R.color.LightGray), 200);

		TextView viewText = (TextView) viewBase.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewBase.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewBase.findViewById(R.id.linearLayout_toastBody);
		viewText.setText(model.getText());
		viewText.setTextColor(Client.getResource().getColor(R.color.White));

		viewBase.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		bodyLayout.removeAllViews();

		bodyLayout.setVisibility(View.VISIBLE);
		View viewStatus = StatusViewFactory.getView(layoutInflater, model.targetModel);
		bodyLayout.addView(viewStatus);

		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		viewText.setWidth((int) (metrics.widthPixels * 0.8));

		return viewBase;
	}
	
	private static View getToastView(Activity activity, UserEvent model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		if (viewBase == null)
		{
			viewBase = layoutInflater.inflate(R.layout.eventtoast_layout, null);
		}

		int black = ColorUtils.setAlpha(Client.getColor(R.color.Black), 180);
		int gray = ColorUtils.setAlpha(Client.getColor(R.color.LightGray), 200);

		TextView viewText = (TextView) viewBase.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewBase.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewBase.findViewById(R.id.linearLayout_toastBody);
		viewText.setText(model.getText());
		viewText.setTextColor(Client.getColor(R.color.White));

		viewBase.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		bodyLayout.removeAllViews();
		bodyLayout.setVisibility(View.GONE);

		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		viewText.setWidth((int) (metrics.widthPixels * 0.8));

		return viewBase;
	}
	
	public static View getView(EventModel model, View viewBase)
	{
		if(model instanceof StatusEventModel)
		{
			return getView((StatusEventModel)model, viewBase);
		}
		else if(model instanceof UserEvent)
		{
			return getView((UserEvent)model, viewBase);
		}
		else
		{
			return viewBase;
		}
	}
	
	private static View getView(UserEvent model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.getInstance());
		if (viewBase == null)
		{
			viewBase = layoutInflater.inflate(R.layout.event_layout, null);
		}

		int black = Client.getColor(R.color.Black);
		int gray = Client.getColor(R.color.LightGray);

		TextView viewText = (TextView) viewBase.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewBase.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewBase.findViewById(R.id.linearLayout_toastBody);

		viewBase.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		viewText.setText(model.getText());
		viewText.setTextColor(Client.getResource().getColor(R.color.White));
		bodyLayout.setVisibility(View.GONE);

		return viewBase;
	}
	
	private static View getView(StatusEventModel model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.getInstance());
		if (viewBase == null)
		{
			viewBase = layoutInflater.inflate(R.layout.event_layout, null);
		}

		int black = Client.getColor(R.color.Black);
		int gray = Client.getColor(R.color.LightGray);

		TextView viewText = (TextView) viewBase.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewBase.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewBase.findViewById(R.id.linearLayout_toastBody);

		viewBase.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		bodyLayout.removeAllViews();
		viewText.setText(model.getText());
		viewText.setTextColor(Client.getResource().getColor(R.color.White));

		if (model.targetModel == null)
		{
			bodyLayout.setVisibility(View.GONE);
		}
		else
		{
			bodyLayout.setVisibility(View.VISIBLE);
			View viewStatus = StatusViewFactory.getView(layoutInflater, model.targetModel);
			bodyLayout.addView(viewStatus);
		}

		return viewBase;
	}
}
