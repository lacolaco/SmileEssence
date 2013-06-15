package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.twitter.Favorite;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.SimpleAsyncTask;

public class AsyncUnFavorite extends SimpleAsyncTask<Boolean> implements Callable<Boolean>
{
	private Account account;
	private long statusId;

	public AsyncUnFavorite(long statusId)
	{
		this(Client.getMainAccount(), statusId);
	}

	public AsyncUnFavorite(Account account, long statusId)
	{
		this.account = account;
		this.statusId = statusId;
	}

	@Override
	public Boolean call()
	{
		return Favorite.unfavorite(account, statusId);
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
			Notifier.info("お気に入りを削除しました");
		}
		else
		{
			Notifier.info("お気に入りの削除に失敗しました");
		}
	}

}
