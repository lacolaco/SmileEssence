package net.miz_hi.smileessence.async;

import java.util.ArrayList;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.viewmodel.MainActivityViewModel;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class AsyncTimelineGetter extends AsyncTask<Paging, Integer, ResponseList<twitter4j.Status>>
{

	private Account account;
	
	public AsyncTimelineGetter(Account account)
	{
		this.account = account;
	}

	@Override
	protected ResponseList<twitter4j.Status> doInBackground(Paging... arg0)
	{
		try
		{
			return Client.getTwitter(account).getHomeTimeline(arg0[0]);
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
		if(MainActivityViewModel.singleton().homeListAdapter != null)
		{
			MainActivityViewModel.singleton().homeListAdapter.addAllLast(list);
		}
	}

}
