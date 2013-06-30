
package net.miz_hi.smileessence.view.fragment;

import java.util.Collections;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.status.StatusChecker;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.view.IRemainable;
import net.miz_hi.smileessence.view.IRemovable;
import net.miz_hi.smileessence.view.activity.MainActivity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.UserList;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ListFragment extends NamedFragment implements IRemovable, IRemainable, OnClickListener
{
	
	String name;
	int id;
	StatusListAdapter adapter;
	boolean inited;

	public static ListFragment newInstance(String fullName, int id)
	{
		ListFragment fragment = new ListFragment();
		fragment.name = fullName;
		fragment.id = id;
		fragment.adapter = new StatusListAdapter(MainActivity.getInstance());
		fragment.inited = false;
		return fragment;
	}
	
	
	@Override
	public String getTitle()
	{
		return name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View page = inflater.inflate(R.layout.listpage_refresh_layout, container, false);
		ListView listView = (ListView)page.findViewById(R.id.listpage_listview);
//		ProgressBar progress = new ProgressBar(getActivity());
//		progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		progress.setVisibility(View.GONE);
//		((ViewGroup)listView.getParent()).addView(progress);
//		listView.setEmptyView(progress);
		listView.setFastScrollEnabled(true);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new TimelineScrollListener(adapter));
		Button refresh = (Button)page.findViewById(R.id.listpage_refresh);
		refresh.setOnClickListener(this);
		return page;
	}

	public void refresh()
	{
		inited = true;
		final ProgressDialog pd = ProgressDialog.show(MainActivity.getInstance(), "", name + "を取得中...");
		MyExecutor.execute(new Runnable()
		{
			public void run()
			{
				try
				{
					ResponseList<Status> resp;
					if(adapter.getCount() > 0)
					{
						long lastId = ((StatusModel)adapter.getItem(0)).statusId;
						resp = TwitterManager.getTwitter().getUserListStatuses(id, new Paging(1, 50, lastId));
					}
					else
					{
						resp = TwitterManager.getTwitter().getUserListStatuses(id, new Paging(1, 50));
					}			
					Collections.reverse(resp);
					for(Status status : resp)
					{
						StatusModel model = StatusStore.put(status);
						StatusChecker.check(model);
						adapter.addFirst(model);
					}
					adapter.forceNotifyAdapter();
				}
				catch (TwitterException e)
				{
					e.printStackTrace();
				}
				pd.dismiss();
			}
		});		
	}

	@Override
	public String save()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(",").append(id);
		return builder.toString();
	}


	@Override
	public void load(String data)
	{
		String[] array = data.split(",");
		name = array[0];
		id = Integer.parseInt(array[1]);
		MainActivity.addPage(this);
	}


	@Override
	public void onRemoved()
	{
	}


	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.listpage_refresh)
		{
			refresh();
		}
	}
	
	public boolean isInited()
	{
		return inited;
	}

}
