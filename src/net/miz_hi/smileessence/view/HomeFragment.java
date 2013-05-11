package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.NamedFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class HomeFragment extends NamedFragment
{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		LinearLayout page = (LinearLayout) inflater.inflate(R.layout.listpage_layout, container, false);
		ListView listView = (ListView)page.findViewById(R.id.listpage_listview);
		ProgressBar progress = new ProgressBar(getActivity());
		progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progress.setVisibility(View.GONE);
		((ViewGroup)listView.getParent()).addView(progress);
		listView.setEmptyView(progress);
		listView.setFastScrollEnabled(true);
		listView.setAdapter(MainSystem.getInstance().homeListAdapter);
		listView.setOnScrollListener(new TimelineScrollListener(MainSystem.getInstance().homeListAdapter));
		listView.destroyDrawingCache();
		return page;
	}

	@Override
	public String getTitle()
	{
		return "Home";
	}
	
}
