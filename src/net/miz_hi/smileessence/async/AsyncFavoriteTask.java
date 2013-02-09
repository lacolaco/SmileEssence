package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.util.TwitterManager;

public class AsyncFavoriteTask implements Callable<Boolean>
{
	private Account account;
	private long statusId;

	public AsyncFavoriteTask(long statusId)
	{
		this(Client.getMainAccount(), statusId);
	}

	public AsyncFavoriteTask(Account account, long statusId)
	{
		this.account = account;
		this.statusId = statusId;
	}

	@Override
	public Boolean call()
	{
		return TwitterManager.favorite(account, statusId);
	}

}
