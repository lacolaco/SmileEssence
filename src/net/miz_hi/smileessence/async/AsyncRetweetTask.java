package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;


import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class AsyncRetweetTask implements Callable<Boolean>
{
	private Account account;
	private long statusId;

	public AsyncRetweetTask(long statusId)
	{
		this(Client.getMainAccount(), statusId);
	}

	public AsyncRetweetTask(Account account, long statusId)
	{
		this.account = account;
		this.statusId = statusId;
	}

	@Override
	public Boolean call()
	{
		return TwitterManager.retweet(account, statusId);
	}

}
