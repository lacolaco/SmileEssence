package net.miz_hi.smileessence.status;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.twitter.TwitterManager;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

public class StatusUtils
{

	public static boolean isMine(long id)
	{
		return isMine(StatusStore.get(id));
	}

	public static boolean isReply(Status st)
	{
		if (st == null)
		{
			return false;
		}
		for (UserMentionEntity ume : st.getUserMentionEntities())
		{
			if (ume.getScreenName().equals(Client.getMainAccount().getScreenName()))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isMine(StatusModel st)
	{
		return st.user.isMe();
	}
	
	public static StatusModel getOrCreateStatusModel(final long id)
	{
		StatusModel statusModel = StatusStore.get(id);
		if(statusModel == null)
		{
			Future<Status> f = MyExecutor.submit(new Callable<Status>()
			{

				@Override
				public Status call() throws Exception
				{
					return TwitterManager.getStatus(Client.getMainAccount(), id);
				}
			});
			Status status;
			try
			{
				status = f.get();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				status = null;
			}
			if(status == null)
			{
				return null;
			}
			statusModel = StatusStore.put(status);
		}
		return statusModel;
	}

}
