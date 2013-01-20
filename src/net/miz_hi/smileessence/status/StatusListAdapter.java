package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.QueueAdapter;
import net.miz_hi.smileessence.listener.StatusOnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusListAdapter extends QueueAdapter<StatusModel>
{
	
	public StatusListAdapter(EventHandlerActivity activity)
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
			holder.viewSource = (TextView)convertedView.findViewById(R.id.textView_source);
			holder.viewRetweetedBy = (TextView)convertedView.findViewById(R.id.textView_retweetedBy);
			holder.viewCreatedAt = (TextView)convertedView.findViewById(R.id.textView_createdAt);
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
		IconCaches.setIconBitmapToView(model.user, holder.viewIcon);
		holder.viewScreenName.setText(model.screenName);
		holder.viewScreenName.setTextColor(model.nameColor);
		holder.viewName.setText(model.name);
		holder.viewName.setTextColor(model.nameColor);
		holder.viewText.setText(model.text);
		holder.viewText.setTextColor(model.textColor);
		holder.viewSource.setText(model.source);
		holder.viewSource.setTextColor(model.textColor);
		holder.viewCreatedAt.setText(model.createdAtString);
		holder.viewCreatedAt.setTextColor(model.textColor);
		holder.viewRetweetedBy.setText(model.retweetedBy);
		holder.viewRetweetedBy.setTextColor(model.textColor);
		if(model.isRetweet)
		{
			holder.viewRetweetedBy.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.viewRetweetedBy.setVisibility(View.GONE);
		}
		holder.viewBase.setOnClickListener(new StatusOnClickListener(getActivity(), model));
		return convertedView;
	}
	
	private static class ViewHolder
	{
		View viewBase;
		ImageView viewIcon;
		TextView viewScreenName, viewName, viewText, viewSource, viewRetweetedBy, viewCreatedAt;
	}

}
