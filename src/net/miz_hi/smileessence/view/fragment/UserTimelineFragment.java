package net.miz_hi.smileessence.view.fragment;

import java.util.Collections;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.status.StatusChecker;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.view.IRemovable;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

@SuppressLint("ValidFragment")
public class UserTimelineFragment extends NamedFragment implements IRemovable, OnClickListener
{
	
	UserModel user;
	StatusListAdapter adapter;
	
	private UserTimelineFragment(){};
	
	public static UserTimelineFragment newInstance(UserModel user)
	{
		UserTimelineFragment fragment = new UserTimelineFragment();
		fragment.user = user;
		return fragment;
	}

	@Override
	public String getTitle()
	{
		return user.screenName + "'s Timeline";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View page = inflater.inflate(R.layout.listpage_refresh_layout, container, false);
		ListView listView = (ListView)page.findViewById(R.id.listpage_listview);
		ProgressBar progress = new ProgressBar(getActivity());
		progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progress.setVisibility(View.GONE);
		((ViewGroup)listView.getParent()).addView(progress);
		listView.setEmptyView(progress);
		listView.setFastScrollEnabled(true);
		if(adapter == null)
		{
			adapter = new StatusListAdapter(getActivity());
			getUserTimeline();
		}
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new TimelineScrollListener(adapter));
		Button refresh = (Button)page.findViewById(R.id.listpage_refresh);
		refresh.setOnClickListener(this);

		return page;
	}
	
	private void getUserTimeline()
	{
		final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "取得中...");
		MyExecutor.execute(new Runnable()
		{
			public void run()
			{
				User u = TwitterManager.getUser(Client.getMainAccount(), user.userId);
				user.updateData(u);
				List<StatusModel> list;
				if(adapter.getCount() > 0)
				{
					long lastId = ((StatusModel)adapter.getItem(0)).statusId;
					list = new AsyncTimelineGetter(Client.getMainAccount(), user.userId, new Paging(1, 20, lastId)).call();
				}
				else
				{
					list = new AsyncTimelineGetter(Client.getMainAccount(), user.userId, new Paging(1, 20)).call();
				}
				
				Collections.reverse(list);
				for(StatusModel status : list)
				{
					adapter.addFirst(status);
				}
				adapter.forceNotifyAdapter();
				pd.dismiss();
			}
		});
	}

	@Override
	public void onRemoved()
	{
		adapter.clear();
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.listpage_refresh)
		{
			getUserTimeline();
		}
	}

}
