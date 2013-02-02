package net.miz_hi.smileessence.async;

import android.widget.Toast;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;

public class AsyncTweetTask extends ConcurrentAsyncTask<String>
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
	protected String doInBackground(Object... arg0)
	{
		return TwitterManager.tweet(account, status);
	}

	@Override
	protected void onPostExecute(String result)
	{
		Toast.makeText(MainActivity.getInstance(), result, Toast.LENGTH_SHORT).show();
	}

}
