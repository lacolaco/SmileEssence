package net.miz_hi.smileessence.async;

import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.model.Account;
import net.miz_hi.smileessence.model.Client;
import net.miz_hi.smileessence.util.TwitterApi;

public class AsyncRetweetTask extends ConcurrentAsyncTask<String>
{	
	private Account account;
	private long statusId;
	private ViewModel viewModel;

	public AsyncRetweetTask(long statusId, ViewModel viewModel)
	{
		this(Client.getMainAccount(), statusId, viewModel);
	}
	
	public AsyncRetweetTask(Account account, long statusId, ViewModel viewModel)
	{
		this.account = account;
		this.statusId = statusId;
		this.viewModel = viewModel;
	}

	@Override
	protected String doInBackground(Object... arg0)
	{
		return TwitterApi.retweet(account, statusId);
	}

	@Override
	protected void onPostExecute(String result)
	{
		viewModel.toast(result);
	}

}
