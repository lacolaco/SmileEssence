package net.miz_hi.smileessence.async;

import android.widget.Toast;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.util.TwitterManager;
import net.miz_hi.smileessence.view.MainActivity;

public class AsyncFavoriteTask extends ConcurrentAsyncTask<String>
{	
	private Account account;
	private long statusId;

	public AsyncFavoriteTask(long statusId)
	{
		this(Client.getMainAccount(), statusId);
	}
	
	public AsyncFavoriteTask(Account account, long statusId)
	{
		this.account = account;
		this.statusId = statusId;
	}

	@Override
	protected String doInBackground(Object... arg0)
	{
		return TwitterManager.favorite(account, statusId);
	}

	@Override
	protected void onPostExecute(String result)
	{
		Toast.makeText(MainActivity.getInstance(), result, Toast.LENGTH_SHORT).show();
	}

}
