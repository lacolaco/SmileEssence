package net.miz_hi.smileessence.twitter;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.util.CountUpInteger;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Tweet
{
	
	private static boolean isStatusUpdateLimit = false;
	private static CountUpInteger count = new CountUpInteger(5);
	public static final String MESSAGE_TWEET_SUCCESS = "投稿しました";
	public static final String MESSAGE_TWEET_DEPLICATE = "投稿失敗しました";
	private static final String ERROR_STATUS_DUPLICATE = "Status is a duplicate";
	private static final String ERROR_STATUS_LIMIT = "User is over daily status update limit";
	
	protected static void update(Account account, String str) throws TwitterException
	{
		StatusUpdate update = new StatusUpdate(str);
		update(account, update);
	}

	protected static void update(Account account, String str, long l) throws TwitterException
	{
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(l);
		update(account, update);
	}

	protected static void update(Account account, StatusUpdate update) throws TwitterException
	{
		try
		{
			Twitter twitter = TwitterManager.getTwitter(account);
			twitter.updateStatus(update);
			isStatusUpdateLimit = false;
			count.reset();
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
			int code = e.getStatusCode();
			String message = e.getErrorMessage();
			if (code == 403)
			{
				if(message != null)
				{
					if(message.equals(ERROR_STATUS_DUPLICATE))
					{
						if(!count.countUp())
						{
							String str = update.getStatus() + "　";
							long id = update.getInReplyToStatusId();
							StatusUpdate update1 = new StatusUpdate(str);
							update1.setInReplyToStatusId(id);
							update(account, update1);
							return;
						}
					}
					else if (message.equals(ERROR_STATUS_LIMIT))
					{
						isStatusUpdateLimit = true;
						Notificator.alert("規制されています");
						return;
					}
				}				
			}
			throw e;
		}
	}
	
	protected static boolean isStatusUpdateLimit()
	{
		return isStatusUpdateLimit;
	}
}
