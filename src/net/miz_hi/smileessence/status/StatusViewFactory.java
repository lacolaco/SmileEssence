package net.miz_hi.smileessence.status;

import java.util.WeakHashMap;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusViewFactory
{

	public static View getView(LayoutInflater layoutInflater, StatusModel model)
	{
		View viewStatus = layoutInflater.inflate(R.layout.status_layout, null);
		ImageView viewIcon = (ImageView)viewStatus.findViewById(R.id.imageView_icon);
		TextView viewScreenName = (TextView)viewStatus.findViewById(R.id.textView_screenName);
		TextView viewName = (TextView)viewStatus.findViewById(R.id.textView_name);
		TextView viewText = (TextView)viewStatus.findViewById(R.id.textView_text);
		TextView viewSource = (TextView)viewStatus.findViewById(R.id.textView_source);
		TextView viewRetweetedBy = (TextView)viewStatus.findViewById(R.id.textView_retweetedBy);
		TextView viewCreatedAt = (TextView)viewStatus.findViewById(R.id.textView_createdAt);
		if(!model.isRetweet && !model.isReply())
		{
			model.backgroundColor = Client.getResource().getColor(R.color.White);
		}
		viewStatus.setBackgroundColor(model.backgroundColor);
		viewIcon.setImageBitmap(IconCaches.getEmptyIcon());
		if(IconCaches.getIcon(model.user.getId()) != null)
		{
			viewIcon.setImageBitmap(IconCaches.getIcon(model.user.getId()).use());
		}
		else
		{
			IconCaches.setIconBitmapToView(model.user, viewIcon);
		}
		viewScreenName.setText(model.screenName);
		viewScreenName.setTextColor(model.nameColor);
		viewScreenName.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE));
		viewName.setText(model.name);
		viewName.setTextColor(model.nameColor);
		viewName.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE));
		viewText.setText(model.text);
		viewText.setTextColor(model.textColor);
		viewText.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE));
		viewSource.setText(model.source);
		viewSource.setTextColor(model.textColor);
		viewSource.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE) - 1);
		viewCreatedAt.setText(model.createdAtString);
		viewCreatedAt.setTextColor(model.textColor);
		viewCreatedAt.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE) - 1);
		viewRetweetedBy.setText(model.retweetedBy);
		viewRetweetedBy.setTextColor(model.textColor);
		viewRetweetedBy.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE) - 1);
		if(model.isRetweet)
		{
			viewRetweetedBy.setVisibility(View.VISIBLE);
		}
		else
		{
			viewRetweetedBy.setVisibility(View.GONE);
		}
		return viewStatus;
	}
}
