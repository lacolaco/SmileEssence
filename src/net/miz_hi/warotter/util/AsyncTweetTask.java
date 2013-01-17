package net.miz_hi.warotter.util;

import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Account;
import twitter4j.StatusUpdate;
import android.os.AsyncTask;

public class AsyncTweetTask extends AsyncTask<StatusUpdate, Integer, String>
{

	public ViewModel viewModel;
	public Account account;

	public AsyncTweetTask(Account account, ViewModel viewModel)
	{
		this.viewModel = viewModel;
		this.account = account;
	}

	@Override
	protected String doInBackground(StatusUpdate... arg0)
	{
		return TwitterApi.tweet(account, arg0[0]);
	}

	@Override
	protected void onPostExecute(String result)
	{
		viewModel.toast(result);
	}

}
