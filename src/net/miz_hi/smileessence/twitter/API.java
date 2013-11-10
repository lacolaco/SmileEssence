package net.miz_hi.smileessence.twitter;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import twitter4j.*;


public class API
{

	/*
	 * TWEET
	 */

    public static Status getStatus(Account account, long id) throws TwitterException
    {
        return TwitterManager.getTwitter(account).showStatus(id);
    }

    public static void favorite(Account account, long statusId) throws TwitterException
    {
        TwitterManager.getTwitter(account).createFavorite(statusId);
    }

    public static void unfavorite(Account account, long statusId) throws TwitterException
    {
        TwitterManager.getTwitter().destroyFavorite(statusId);
    }

    public static void retweet(Account account, long statusId) throws TwitterException
    {
        TwitterManager.getTwitter(account).retweetStatus(statusId);
    }

    public static void tweet(Account account, String str) throws TwitterException
    {
        Tweet.update(account, str);
    }

    public static void tweet(Account account, String str, long l) throws TwitterException
    {
        Tweet.update(account, str, l);
    }

    public static void tweet(Account account, StatusUpdate update) throws TwitterException
    {
        Tweet.update(account, update);
    }

    public static boolean isStatusUpdateLimit()
    {
        return Tweet.isStatusUpdateLimit();
    }
	
	/*
	 * USER
	 */

    public static User getUser(Account account, long id) throws TwitterException
    {
        return TwitterManager.getTwitter(account).showUser(id);
    }

    public static User getUser(Account account, String screenName) throws TwitterException
    {
        return TwitterManager.getTwitter(account).showUser(screenName);
    }

    public static void follow(Account account, String screenName) throws TwitterException
    {
        TwitterManager.getTwitter(account).createFriendship(screenName);
    }

    public static void unfollow(Account account, String screenName) throws TwitterException
    {
        TwitterManager.getTwitter(account).destroyFriendship(screenName);
    }

    public static void block(Account account, String screenName) throws TwitterException
    {
        TwitterManager.getTwitter(account).createBlock(screenName);
    }

    public static void unblock(Account account, String screenName) throws TwitterException
    {
        TwitterManager.getTwitter(account).destroyBlock(screenName);
    }

    public static void spam(Account account, String screenName) throws TwitterException
    {
        TwitterManager.getTwitter(account).reportSpam(screenName);
    }
	
	/*
	 * RELATIONSHIP
	 */

    public static Relationship getRelationship(Account account, long id) throws TwitterException
    {
        return TwitterManager.getTwitter(account).showFriendship(account.getUserId(), id);
    }

    public static Relationship getRelationship(Account account, String screenName) throws TwitterException
    {
        return TwitterManager.getTwitter(account).showFriendship(account.getScreenName(), screenName);
    }
	
	/*
	 * TIMELINE
	 */

    public static ResponseList<Status> getHomeTimeline(Account account, Paging page) throws TwitterException
    {
        return TwitterManager.getTwitter(account).getHomeTimeline(page);
    }

    public static ResponseList<Status> getMentions(Account account, Paging page) throws TwitterException
    {
        return TwitterManager.getTwitter(account).getMentionsTimeline(page);
    }

    public static ResponseList<Status> getUserTimeline(Account account, long userId, Paging page) throws TwitterException
    {
        return TwitterManager.getTwitter(account).getUserTimeline(userId, page);
    }

    public static ResponseList<Status> getUserTimeline(Account account, String screenName, Paging page) throws TwitterException
    {
        return TwitterManager.getTwitter(account).getUserTimeline(screenName, page);
    }

	/*
	 * LIST
	 */

    public static ResponseList<UserList> getReadableLists(Account account) throws TwitterException
    {
        return TwitterManager.getTwitter(account).getUserLists(Client.getMainAccount().getUserId());
    }

    public static ResponseList<Status> getListTimeline(Account account, int listId, Paging page) throws TwitterException
    {
        return TwitterManager.getTwitter(account).getUserListStatuses(listId, page);
    }
}
