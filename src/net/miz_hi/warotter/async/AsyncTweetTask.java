package net.miz_hi.warotter.async;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.util.TwitterApi;
import twitter4j.StatusUpdate;
import android.os.AsyncTask;

public class AsyncTweetTask extends ConcurrentAsyncTask<String>
{	
	private Account account;
	private StatusUpdate status;
	private ViewModel viewModel;

	public AsyncTweetTask(StatusUpdate status, ViewModel viewModel)
	{
		this(Warotter.getMainAccount(), status, viewModel);
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
