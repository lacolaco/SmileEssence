package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.CustomListAdapter;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.listener.StatusOnClickListener;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class StatusListAdapter extends CustomListAdapter<StatusModel>
{

	public StatusListAdapter(Activity activity)
	{
		super(activity, 1000);
	}

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		if (convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.status_layout, null);
		}
		StatusModel model = (StatusModel) getItem(position);
		convertedView = StatusViewFactory.getView(getInflater(), model);
		convertedView.setOnClickListener(new StatusOnClickListener(getActivity(), model));
		return convertedView;
	}
}
