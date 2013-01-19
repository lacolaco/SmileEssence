package net.miz_hi.smileessence.util;

import net.miz_hi.smileessence.model.Account;
import net.miz_hi.smileessence.model.Client;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public class TwitterApi
{
	private static final String MESSAGE_TWEET_SUCCESS = "投稿しました";
	private static final String MESSAGE_TWEET_DEPLICATE = "投稿失敗しました";
	private static final String MESSAGE_RETWEET_SUCCESS = "リツイートしました";
	private static final String MESSAGE_RETWEET_DEPLICATE = "リツイート失敗しました";
	private static final String MESSAGE_FAVORITE_SUCCESS = "お気に入りに追加しました";
	private static final String MESSAGE_FAVORITE_DEPLICATE = "お気に入りの追加に失敗しました";
	private static final String MESSAGE_TWEET_LIMIT = "規制されています";
	private static final String ERROR_STATUS_DUPLICATE = "Status is a duplicate.";
	private static final String ERROR_STATUS_LIMIT = "User is over daily status update limit.";

	public static String tweet(Account account, StatusUpdate st)
	{
		try
		{
			Client.getTwitter(account).updateStatus(st);
		}
		catch (TwitterException e)
		{
			int code = e.getStatusCode();
			String message = e.getErrorMessage();
			if (code == 403)
			{
				if (message.equals(ERROR_STATUS_LIMIT))
				{
					return MESSAGE_TWEET_LIMIT;
				}
			}
			return MESSAGE_TWEET_DEPLICATE;
		}
		return MESSAGE_TWEET_SUCCESS;
	}
	
	public static String retweet(Account account, long statusId)
	{
		try
		{
			Client.getTwitter(account).retweetStatus(statusId);
		}
		catch (TwitterException e)
		{
			return MESSAGE_RETWEET_DEPLICATE;
		}
		return MESSAGE_RETWEET_SUCCESS;
	}
	
	public static String favorite(Account account, long statusId)
	{
		try
		{
			Client.getTwitter(account).createFavorite(statusId);
		}
		catch (TwitterException e)
		{
			return MESSAGE_FAVORITE_DEPLICATE;
		}
		return MESSAGE_FAVORITE_SUCCESS;
	}
}
