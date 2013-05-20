package net.miz_hi.smileessence.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.view.IRemainable;
import net.miz_hi.smileessence.view.IRemovable;
import net.miz_hi.smileessence.view.activity.MainActivity;

@SuppressLint("ValidFragment")
public class ExtractFragment extends NamedFragment implements IRemovable, IRemainable
{

	public static ExtractFragment singleton = new ExtractFragment();
	public static boolean isShowing;
	
	private ExtractFragment()
	{
	}
	
	public static ExtractFragment singleton()
	{
		return singleton;
	}
	
	@Override
	public String getTitle()
	{
		return "Extracted Tweets";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View page = inflater.inflate(R.layout.listpage_layout, container, false);
		ListView listView = (ListView)page.findViewById(R.id.listpage_listview);
		listView.setFastScrollEnabled(true);
		StatusListAdapter adapter = getAdapter();
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new TimelineScrollListener(adapter));
		isShowing = true;
		return page;
	}

	public StatusListAdapter getAdapter()
	{
		return MainSystem.getInstance().extractListAdapter;
	}

	@Override
	public String save()
	{
		return String.valueOf(isShowing);
	}

	@Override
	public void load(String data)
	{
		isShowing = Boolean.parseBoolean(data);
		if(isShowing)
		{
			MainActivity.addPage(singleton);
		}
	}

	@Override
	public void onRemoved()
	{
		isShowing = false;
	}

}
