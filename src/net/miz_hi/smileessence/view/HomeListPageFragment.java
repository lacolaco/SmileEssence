package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeListPageFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		RelativeLayout page = (RelativeLayout) inflater.inflate(R.layout.listpage_layout, container, false);

		TextView textView = (TextView)page.findViewById(R.id.textView_listpage);
		ListView listView = (ListView)page.findViewById(R.id.listView_listpage);

		textView.setTextSize(Client.getTextSize() + 3);
		listView.setFastScrollEnabled(true);
		textView.setText("Home");
		listView.setAdapter(MainActivity.getInstance().getHomeListAdapter());
		listView.setOnScrollListener(new TimelineScrollListener(MainActivity.getInstance().getHomeListAdapter()));

		return page;
	}

}
