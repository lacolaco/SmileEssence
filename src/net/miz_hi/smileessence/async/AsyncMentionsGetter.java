package net.miz_hi.smileessence.async;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.status.StatusChecker;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import twitter4j.Paging;
import twitter4j.Status;

public class AsyncMentionsGetter implements Callable<List<StatusModel>>
{

	private Account account;
	private Paging page;

	public AsyncMentionsGetter(Account account, Paging page)
	{
		this.account = account;
		this.page = page;
	}

	@Override
	public List<StatusModel> call()
	{
		LinkedList<Status> resp = new LinkedList<Status>();
		resp.addAll(TwitterManager.getOldMentions(account, page));

		LinkedList<StatusModel> list = new LinkedList<StatusModel>();
		while(!resp.isEmpty())
		{
			StatusModel model = StatusStore.put(resp.pollLast());
			list.offerFirst(model);
			StatusChecker.check(model);
		}
		return list;
	}

}
