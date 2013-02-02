package net.miz_hi.smileessence.async;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

public class AsyncMentionsGetter extends ConcurrentAsyncTask<List<twitter4j.Status>>
{

	private Account account;
	private Paging page;
	
	public AsyncMentionsGetter(Account account, Paging page)
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
			if(st.isRetweet())
			{
				StatusStore.put(st.getRetweetedStatus());
			}
			list.add(StatusModel.createInstance(st));
		}
		if(MainActivity.getInstance().getMentionsListAdapter() != null)
		{
			MainActivity.getInstance().getMentionsListAdapter().addAllLast(list);
		}
	}

	@Override
	protected List<twitter4j.Status> doInBackground(Object... params)
	{
		return TwitterManager.getOldMentions(account);
	}

}
