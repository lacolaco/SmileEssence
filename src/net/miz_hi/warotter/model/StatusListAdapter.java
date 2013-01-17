package net.miz_hi.warotter.model;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.CustomListAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusListAdapter extends CustomListAdapter<StatusModel>
{
	
	public StatusListAdapter(Activity activity)
	{
		super(activity);
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
		holder.viewBase.setBackgroundColor(model.backgroundColor);
		holder.viewIcon.setImageBitmap(model.iconBitmap);
		holder.viewScreenName.setText(model.screenName);
		holder.viewScreenName.setBackgroundColor(model.nameColor);
		holder.viewName.setText(model.name);
		holder.viewName.setBackgroundColor(model.nameColor);
		holder.viewText.setText(model.text);
		holder.viewSource.setText(model.source);
		holder.viewCreatedAt.setText(model.createdAt.toLocaleString());
		if(model.isRetweet())
		{
			holder.viewRetweetedBy.setText(model.retweetedBy);
		}
		else
		{
			holder.viewRetweetedBy.setVisibility(View.INVISIBLE);
		}		
		return convertedView;
	}
	
	private static class ViewHolder
	{
		View viewBase;
		ImageView viewIcon;
		TextView viewScreenName, viewName, viewText, viewSource, viewRetweetedBy, viewCreatedAt;
	}

}
