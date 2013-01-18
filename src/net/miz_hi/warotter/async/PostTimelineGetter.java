package net.miz_hi.warotter.async;

import java.util.ArrayList;

import net.miz_hi.warotter.core.QueueAdapter;
import net.miz_hi.warotter.model.StatusListAdapter;
import net.miz_hi.warotter.model.StatusModel;
import net.miz_hi.warotter.model.StatusStore;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class PostTimelineGetter extends AsyncTask<Paging, Integer, ResponseList<twitter4j.Status>>
{
	private StatusListAdapter homeListAdapter;

	public PostTimelineGetter(StatusListAdapter homeListAdapter)
	{
		this.homeListAdapter = homeListAdapter;
	}

	@Override
	protected ResponseList<twitter4j.Status> doInBackground(Paging... arg0)
	{
		try
		{
			return Warotter.getTwitter(Warotter.getMainAccount()).getHomeTimeline(arg0[0]);
		}
		catch (TwitterException e)
		{
		}
		return null;
	}

	@Override
	protected void onPostExecute(ResponseList<twitter4j.Status> result)
	{
		if (result == null)
		{
			return;
		}
		ArrayList<StatusModel> list = new ArrayList<StatusModel>();
		for (twitter4j.Status st : result)
		{
			StatusStore.put(st);
			if (st.isRetweet())
			{
				StatusStore.put(st.getRetweetedStatus());
			}
			list.add(StatusModel.createInstance(st));
		}
		homeListAdapter.addAllLast(list);
	}

}
