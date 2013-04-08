package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.SimpleAsyncTask;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;

public class AsyncTweetTask extends SimpleAsyncTask<Boolean> implements Callable<Boolean>
{
	private Account account;
	private StatusUpdate status;

	public AsyncTweetTask(StatusUpdate status)
	{
		this(Client.getMainAccount(), status);
	}

	public AsyncTweetTask(Account account, StatusUpdate status)
	{
		this.account = account;
		this.status = status;
	}

	@Override
	protected Boolean doInBackground(Object... arg0)
	{
		return TwitterManager.tweet(account, status);
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		if (result)
		{
			ToastManager.toast(TwitterManager.MESSAGE_TWEET_SUCCESS);
		}
		else
		{
			ToastManager.toast(TwitterManager.MESSAGE_TWEET_DEPLICATE);
		}
	}

	@Override
	public Boolean call()
	{
		return TwitterManager.tweet(account, status);
	}
}
