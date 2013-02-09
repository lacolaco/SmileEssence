package net.miz_hi.smileessence.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.util.TwitterManager;
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
		List<Status> resp = TwitterManager.getOldMentions(account, page);

		ArrayList<StatusModel> list = new ArrayList<StatusModel>();
		for (twitter4j.Status st : resp)
		{
			list.add(StatusStore.put(st));
		}
		return list;
	}

}
