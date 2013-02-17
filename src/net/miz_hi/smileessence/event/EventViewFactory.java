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

	public static View getToastView(Activity activity, EventModel model, View viewBase)
	{
		if(model instanceof StatusEventModel)
		{
			return getToastView(activity, (StatusEventModel)model, viewBase);
		}
		else if(model instanceof UserEventModel)
		{
			return getToastView(activity, (UserEventModel)model, viewBase);
		}
		else
		{
			return viewBase;
		}
	}
	
	private static View getToastView(Activity _activity, StatusEventModel model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(_activity);
		if (viewBase == null)
		{
			viewBase = layoutInflater.inflate(R.layout.eventtoast_layout, null);
		}

		int black = ColorUtils.setAlpha(Client.getResource().getColor(R.color.Black), 180);
		int gray = ColorUtils.setAlpha(Client.getResource().getColor(R.color.LightGray), 200);

		TextView viewText = (TextView) viewBase.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewBase.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewBase.findViewById(R.id.linearLayout_toastBody);
		viewText.setText(model.source.getScreenName() + model.type.getText());
		viewText.setTextColor(Client.getResource().getColor(R.color.White));

		viewBase.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		bodyLayout.removeAllViews();

		baseLayout.setVisibility(View.VISIBLE);
		View viewStatus = StatusViewFactory.getView(layoutInflater, model.targetModel);
		bodyLayout.addView(viewStatus);

		DisplayMetrics metrics = _activity.getResources().getDisplayMetrics();
		viewText.setWidth((int) (metrics.widthPixels * 0.8));

		return viewBase;
	}
	
	private static View getToastView(Activity _activity, UserEventModel model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(_activity);
		if (viewBase == null)
		{
			viewBase = layoutInflater.inflate(R.layout.eventtoast_layout, null);
		}

		int black = ColorUtils.setAlpha(Client.getColor(R.color.Black), 180);
		int gray = ColorUtils.setAlpha(Client.getColor(R.color.LightGray), 200);

		TextView viewText = (TextView) viewBase.findViewById(R.id.textView_toastText);
		RelativeLayout baseLayout = (RelativeLayout) viewBase.findViewById(R.id.relativeLayout_toastBase);
		LinearLayout bodyLayout = (LinearLayout) viewBase.findViewById(R.id.linearLayout_toastBody);
		viewText.setText(model.source.getScreenName() + model.type.getText());
		viewText.setTextColor(Client.getColor(R.color.White));

		viewBase.setBackgroundColor(gray);
		baseLayout.setBackgroundColor(black);
		bodyLayout.removeAllViews();
		baseLayout.setVisibility(View.GONE);

		DisplayMetrics metrics = _activity.getResources().getDisplayMetrics();
		viewText.setWidth((int) (metrics.widthPixels * 0.8));

		return viewBase;
	}
	
	public static View getView(Activity activity, EventModel model, View viewBase)
	{
		if(model instanceof StatusEventModel)
		{
			return getView(activity, (StatusEventModel)model, viewBase);
		}
		else if(model instanceof UserEventModel)
		{
			return getView(activity, (UserEventModel)model, viewBase);
		}
		else
		{
			return viewBase;
		}
	}
	
	public static View getView(Activity _activity, UserEventModel model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(_activity);
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
		viewText.setText(model.source.getScreenName() + model.type.getText());
		viewText.setTextColor(Client.getResource().getColor(R.color.White));

		baseLayout.setVisibility(View.GONE);

		DisplayMetrics metrics = _activity.getResources().getDisplayMetrics();
		viewText.setWidth((int) (metrics.widthPixels * 0.8));

		return viewBase;
	}
	
	public static View getView(Activity _activity, StatusEventModel model, View viewBase)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(_activity);
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

		return viewBase;
	}
}
