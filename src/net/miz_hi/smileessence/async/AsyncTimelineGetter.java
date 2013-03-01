package net.miz_hi.smileessence.async;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.Paging;
import twitter4j.Status;

public class AsyncTimelineGetter implements Callable<List<StatusModel>>
{

	private Account account;
	private Paging page;
	private long userId;

	public AsyncTimelineGetter(Account account, Paging page)
	{
		this(account, -1, page);
	}

	public AsyncTimelineGetter(Account account, long userId, Paging page)
	{
		this.account = account;
		this.page = page;
		this.userId = userId;
	}

	@Override
	public List<StatusModel> call()
	{
		List<Status> resp = new LinkedList<Status>();
		if (userId >= 0)
		{
			resp.addAll(TwitterManager.getUserTimeline(account, userId, page));
		}
		else
		{
			resp.addAll(TwitterManager.getOldTimeline(account, page));
		}

		List<StatusModel> list = new ArrayList<StatusModel>();

		for (Status st : resp)
		{
			list.add(StatusStore.put(st));
		}

		return list;
	}

}
