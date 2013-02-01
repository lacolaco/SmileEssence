package net.miz_hi.smileessence.async;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.util.TwitterManager;
import net.miz_hi.smileessence.view.MainActivity;
import twitter4j.Paging;
import twitter4j.TwitterException;

public class AsyncTimelineGetter extends ConcurrentAsyncTask<List<twitter4j.Status>>
{

	private Account account;
	private Paging page;
	
	public AsyncTimelineGetter(Account account, Paging page)
	{
		this.account = account;
		this.page = page;
	}

	@Override
	protected void onPostExecute(List<twitter4j.Status> result)
	{
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
		if(MainActivity.getInstance().getHomeListAdapter() != null)
		{
			MainActivity.getInstance().getHomeListAdapter().addAllLast(list);
		}
	}

	@Override
	protected List<twitter4j.Status> doInBackground(Object... params)
	{
		return TwitterManager.getOldTimeline(account);
	}

}
