package net.miz_hi.smileessence.async;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.util.TwitterApi;
import twitter4j.StatusUpdate;

public class AsyncTweetTask extends ConcurrentAsyncTask<String>
{	
	private Account account;
	private StatusUpdate status;
	private ViewModel viewModel;

	public AsyncTweetTask(StatusUpdate status, ViewModel viewModel)
	{
		this(Client.getMainAccount(), status, viewModel);
	}
	
	public AsyncTweetTask(Account account, StatusUpdate status, ViewModel viewModel)
	{
		this.account = account;
		this.status = status;
		this.viewModel = viewModel;
	}

	@Override
	protected String doInBackground(Object... arg0)
	{
		return TwitterApi.tweet(account, status);
	}

	@Override
	protected void onPostExecute(String result)
	{
		viewModel.toast(result);
	}

}
