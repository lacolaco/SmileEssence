package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.CustomListAdapter;
import net.miz_hi.smileessence.listener.StatusOnClickListener;
import net.miz_hi.smileessence.status.IconCaches.Icon;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusListAdapter extends CustomListAdapter<StatusModel>
{
	
	public StatusListAdapter(Activity activity)
	{
		super(activity, 5000);
	}

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		StatusModel model = (StatusModel) getItem(position);
		ViewHolder holder;
		if(convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.status_layout, null);
			holder = new ViewHolder();
			holder.viewBase = convertedView;
			holder.viewIcon = (ImageView)convertedView.findViewById(R.id.imageView_icon);
			holder.viewScreenName = (TextView)convertedView.findViewById(R.id.textView_screenName);
			holder.viewName = (TextView)convertedView.findViewById(R.id.textView_name);
			holder.viewText = (TextView)convertedView.findViewById(R.id.textView_text);
			holder.viewFooter = (TextView)convertedView.findViewById(R.id.textView_footer);
			convertedView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertedView.getTag();
		}
		
		if(!model.isRetweet && !model.isReply())
		{
			model.backgroundColor = Client.getResource().getColor(position % 2 == 0 ? R.color.White : R.color.LightGray);
		}
		holder.viewBase.setBackgroundColor(model.backgroundColor);

		holder.viewIcon.setImageBitmap(IconCaches.getEmptyIcon());
		if(IconCaches.getIcon(model.user.getId()) != null)
		{
			holder.viewIcon.setImageBitmap(IconCaches.getIcon(model.user.getId()).use());
		}
		else
		{
			holder.viewIcon.setTag(model.user.getId());
			IconCaches.setIconBitmapToView(model.user, holder.viewIcon);
		}
		int textSize = Client.getTextSize();
		holder.viewScreenName.setText(model.screenName);
		holder.viewScreenName.setTextColor(model.nameColor);
		holder.viewScreenName.setTextSize(textSize);
		holder.viewName.setText(model.name);
		holder.viewName.setTextColor(model.nameColor);
		holder.viewName.setTextSize(textSize);
		holder.viewText.setText(model.text);
		holder.viewText.setTextColor(model.textColor);
		holder.viewText.setTextSize(textSize);
		holder.viewFooter.setText(model.footer);
		holder.viewFooter.setTextColor(model.textColor);
		holder.viewFooter.setTextSize(textSize - 1);
		holder.viewBase.setOnClickListener(new StatusOnClickListener(getActivity(), model));
		return convertedView;
	}
	
	private static class ViewHolder
	{
		View viewBase;
		ImageView viewIcon;
		TextView viewScreenName, viewName, viewText, viewFooter;
	}

}
