package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.StatusOnClickListener;
import net.miz_hi.smileessence.util.CustomListAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class StatusListAdapter extends CustomListAdapter<StatusModel>
{

	public StatusListAdapter(Activity activity)
	{
		super(activity, 5000);
	}	

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		if (convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.status_layout, null);
		}
		StatusModel model = (StatusModel) getItem(position);
		convertedView = StatusViewFactory.getView(getInflater(), model, convertedView);
		if(!model.isRetweet && !model.isReply)
		{
			if(position % 2 == 0)
			{				
				model.backgroundColor = Client.getColor(R.color.White);
			}
			else
			{
				model.backgroundColor = Client.getColor(R.color.LightGray);
			}
		}

		convertedView.setBackgroundColor(model.backgroundColor);
		
		convertedView.setOnClickListener(new StatusOnClickListener(getActivity(), model));
		return convertedView;
	}
}
