package net.miz_hi.smileessence.util;

import java.util.ArrayList;
import java.util.LinkedList;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.auth.Account;
import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.content.SharedPreferences;

public class TwitterManager
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
	private static Twitter _twitter;
	private static boolean _isStatusUpdateLimit = false;	
	private static Account _lastAccount;
	
	private static ConfigurationBuilder generateConfig(Account account)
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(account.getConsumerKey());
		cb.setOAuthConsumerSecret(account.getConsumerSecret());
		cb.setOAuthAccessToken(account.getAccessToken());
		cb.setOAuthAccessTokenSecret(account.getAccessTokenSecret());
		return cb;
	}
	
	public static Twitter getTwitter(Account account)
	{
		if(_lastAccount == null || !account.equals(_lastAccount) || _twitter == null)
		{
			_twitter = new TwitterFactory(generateConfig(account).build()).getInstance();
			_lastAccount = account;
		}
		return _twitter;
	}

	public static TwitterStream getTwitterStream(Account account)
	{
		ConfigurationBuilder cb = generateConfig(account);
		cb.setUserStreamRepliesAllEnabled(false);
		return new TwitterStreamFactory(cb.build()).getInstance();
	}	

	public static boolean isStatusUpdateLimit()
	{
		return _isStatusUpdateLimit;
	}

	public static String tweet(Account account, String str)
	{
		try
		{
			getTwitter(account).updateStatus(str);
			_isStatusUpdateLimit = false;
		}
		catch (TwitterException e)
		{
			int code = e.getStatusCode();
			String message = e.getErrorMessage();
			if (code == 403)
			{
				if (message.equals(ERROR_STATUS_LIMIT))
				{
					_isStatusUpdateLimit = true;
					return MESSAGE_TWEET_LIMIT;
				}
			}
			return MESSAGE_TWEET_DEPLICATE;
		}
		return MESSAGE_TWEET_SUCCESS;
	}
	
	public static String tweet(Account account, String str, long l)
	{
		try
		{
			StatusUpdate update = new StatusUpdate(str);
			update.setInReplyToStatusId(l);
			getTwitter(account).updateStatus(update);
			_isStatusUpdateLimit = false;
		}
		catch (TwitterException e)
		{
			int code = e.getStatusCode();
			String message = e.getErrorMessage();
			if (code == 403)
			{
				if (message.equals(ERROR_STATUS_LIMIT))
				{
					_isStatusUpdateLimit = true;
					return MESSAGE_TWEET_LIMIT;
				}
			}
			return MESSAGE_TWEET_DEPLICATE;
		}
		return MESSAGE_TWEET_SUCCESS;
	}
	
	public static String tweet(Account account, StatusUpdate st)
	{
		try
		{
			getTwitter(account).updateStatus(st);
			_isStatusUpdateLimit = false;
		}
		catch (TwitterException e)
		{
			int code = e.getStatusCode();
			String message = e.getErrorMessage();
			if (code == 403)
			{
				if (message.equals(ERROR_STATUS_LIMIT))
				{
					_isStatusUpdateLimit = true;
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
			getTwitter(account).retweetStatus(statusId);
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
			getTwitter(account).createFavorite(statusId);
		}
		catch (TwitterException e)
		{
			return MESSAGE_FAVORITE_DEPLICATE;
		}
		return MESSAGE_FAVORITE_SUCCESS;
	}
	
	public static boolean isOauthed(Account account)
	{
		return StringUtils.isNullOrEmpty(account.getAccessToken());
	}

	public static boolean isFollowing(Account account, long id)
	{
		if (id < 0)
		{
			return false;
		}
		try
		{
			Relationship relation = getTwitter(account).showFriendship(getTwitter(account).getId(), id);
			return relation.isSourceFollowingTarget();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isFollowed(Account account, long id)
	{
		if (id < 0)
		{
			return false;
		}
		try
		{
			Relationship relation = getTwitter(account).showFriendship(getTwitter(account).getId(), id);
			return relation.isSourceFollowedByTarget();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static LinkedList<Status> getOldTimeline(Account account)
	{
		LinkedList<Status> statuses = new LinkedList<Status>();
		try
		{
			ArrayList<Status> preload = new ArrayList<Status>();
			for (int i = 0; i < 2; i++)
			{
				preload.addAll(getTwitter(account).getHomeTimeline(new Paging(i + 1)));
			}
			for (Status st : preload)
			{
				statuses.offer(st);
			}
		}
		catch (TwitterException e)
		{
		}
		return statuses;
	}

	public static LinkedList<Status> getOldMentions(Account account)
	{
		LinkedList<Status> statuses = new LinkedList<Status>();
		try
		{
			ResponseList<Status> mentions = getTwitter(account).getMentions();
			for (Status st : mentions)
			{
				statuses.offer(st);
			}
		}
		catch (TwitterException e)
		{
		}
		return statuses;
	}

	public static User getUser(Account account, Object obj)
	{
		try
		{
			User user = null;
			if (obj instanceof String)
			{
				user = getTwitter(account).showUser((String) obj);
			}
			else if (obj instanceof Long)
			{
				user = getTwitter(account).showUser(((Long) obj).longValue());
			}
			return user;
		}
		catch (TwitterException e)
		{
			return null;
		}
	}

	public static boolean follow(Account account, String name)
	{
		try
		{
			getTwitter(account).createFriendship(name);
			return true;
		}
		catch (TwitterException e)
		{
			//TODO
		}
		return false;
	}

	public static boolean remove(Account account, String name)
	{
		try
		{
			getTwitter(account).destroyFriendship(name);
			return true;
		}
		catch (TwitterException e)
		{
			//TODO
		}
		return false;
	}

	public static boolean block(Account account, String name)
	{
		try
		{
			getTwitter(account).createBlock(name);
			return true;
		}
		catch (TwitterException e)
		{
			//TODO
		}
		return false;
	}

	public static boolean spam(Account account, String name)
	{
		try
		{
			getTwitter(account).reportSpam(name);
			return true;
		}
		catch (TwitterException e)
		{
			//TODO
		}
		return false;
	}
}
