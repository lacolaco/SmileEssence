package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.ColorUtils;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventViewFactory
{
	
	public static View getView(EventModel model, View baseView)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.getInstance());
		if (baseView == null)
		{
			baseView = layoutInflater.inflate(R.layout.event_layout, null);
		}

		ImageView viewIcon = (ImageView) baseView.findViewById(R.id.event_icon);
		TextView viewHeader = (TextView) baseView.findViewById(R.id.event_header);
		TextView viewText = (TextView) baseView.findViewById(R.id.event_text);
		TextView viewFooter = (TextView) baseView.findViewById(R.id.event_footer);

		baseView.setBackgroundColor(Client.getColor(R.color.White));
		
		viewIcon.setTag(model.source.userId);
		IconCaches.setIconBitmapToView(model.source, viewIcon);
		
		int textSize = Client.getTextSize();
		viewHeader.setText(model.getHeaderText());
		viewHeader.setTextColor(Client.getColor(R.color.DarkBlue));
		viewHeader.setTextSize(textSize);

		viewFooter.setText(model.getFooterText());
		viewFooter.setTextColor(Client.getColor(R.color.Gray2));
		viewFooter.setTextSize(textSize - 2);
		
		if(model instanceof StatusEventModel)
		{
			viewText.setVisibility(View.VISIBLE);
			viewText.setText(((StatusEventModel)model).targetModel.text);
			viewText.setTextColor(Client.getColor(R.color.Gray));
			viewText.setTextSize(textSize);
		}
		else
		{
			viewText.setText("");
			viewText.setVisibility(View.INVISIBLE);
		}
		
		return baseView;
	}

}
