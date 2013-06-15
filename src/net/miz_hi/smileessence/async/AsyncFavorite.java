package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.twitter.Favorite;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.SimpleAsyncTask;

public class AsyncFavorite extends SimpleAsyncTask<Boolean> implements Callable<Boolean>
{
	private Account account;
	private long statusId;

	public AsyncFavorite(long statusId)
	{
		this(Client.getMainAccount(), statusId);
	}

	public AsyncFavorite(Account account, long statusId)
	{
		this.account = account;
		this.statusId = statusId;
	}

	@Override
	public Boolean call()
	{
		return Favorite.favorite(account, statusId);
	}

	@Override
	protected Boolean doInBackground(Object... params)
	{
		return call();
	}
	
	@Override
	protected void onPostExecute(Boolean result)
	{
		if (result)
		{
			Notifier.info(TwitterManager.MESSAGE_FAVORITE_SUCCESS);
		}
		else
		{
			Notifier.info(TwitterManager.MESSAGE_FAVORITE_DEPLICATE);
		}
	}

}
