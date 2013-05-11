package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.SimpleAsyncTask;
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
		return call();
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		if (result)
		{
			Notifier.info(TwitterManager.MESSAGE_TWEET_SUCCESS);
		}
		else
		{
			Notifier.info(TwitterManager.MESSAGE_TWEET_DEPLICATE);
		}
	}

	@Override
	public Boolean call()
	{
		return TwitterManager.tweet(account, status);
	}
}
