package net.miz_hi.warotter.util;

import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.model.Account;
import gueei.binding.labs.EventAggregator;
import twitter4j.StatusUpdate;
import android.os.AsyncTask;

public class AsyncTweetTask extends AsyncTask<StatusUpdate, Integer, String>
{

	public EventAggregator eventAggregator;
	public Account account;

	public AsyncTweetTask(Account account, EventAggregator eventAggregator)
	{
		this.eventAggregator = eventAggregator;
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
		eventAggregator.publish("toast", new ToastMessage(result), null);
	}

}
