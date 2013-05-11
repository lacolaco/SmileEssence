package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.NamedFragment;
import twitter4j.Paging;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

@SuppressLint("ValidFragment")
public class UserTimelineFragment extends NamedFragment implements IRemovable
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
		View page = inflater.inflate(R.layout.listpage_layout, container, false);
		ListView listView = (ListView)page.findViewById(R.id.listpage_listview);
		ProgressBar progress = new ProgressBar(getActivity());
		progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progress.setVisibility(View.GONE);
		((ViewGroup)listView.getParent()).addView(progress);
		listView.setEmptyView(progress);
		listView.setFastScrollEnabled(true);
		adapter = new StatusListAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new TimelineScrollListener(adapter));
		getUserTimeline();
		return page;
	}
	
	private void getUserTimeline()
	{
		MyExecutor.execute(new Runnable()
		{
			public void run()
			{
				User u = TwitterManager.getUser(Client.getMainAccount(), user.userId);
				user.updateData(u);
				adapter.addAll(new AsyncTimelineGetter(Client.getMainAccount(), user.userId, new Paging(1)).call());
				adapter.forceNotifyAdapter();
			}
		});
	}

	@Override
	public void onRemoved()
	{
		adapter.clear();
	}

}
