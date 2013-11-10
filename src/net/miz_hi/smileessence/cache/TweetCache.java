package net.miz_hi.smileessence.cache;

import net.miz_hi.smileessence.model.status.tweet.EnumTweetType;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TweetCache
{

    private static TweetCache instance = new TweetCache();

    private ConcurrentHashMap<Long, TweetModel> statusesMap = new ConcurrentHashMap<Long, TweetModel>();
    private CopyOnWriteArrayList<Long> favoriteList = new CopyOnWriteArrayList<Long>();
    private CopyOnWriteArrayList<String> hashtagList = new CopyOnWriteArrayList<String>();
    private CopyOnWriteArrayList<Long> readRetweetList = new CopyOnWriteArrayList<Long>();

    public static void put(TweetModel tweet)
    {
        if (instance.statusesMap.containsKey(tweet.statusId))
        {
            instance.statusesMap.remove(tweet.statusId);
        }
        if (tweet.type == EnumTweetType.RETWEET)
        {
            instance.readRetweetList.add(tweet.statusId);
        }
        instance.statusesMap.put(tweet.parentStatusId, tweet);
    }

    public static List<TweetModel> getList()
    {
        return new ArrayList<TweetModel>(instance.statusesMap.values());
    }

    public static TweetModel get(long id)
    {
        return instance.statusesMap.get(id);
    }

    public static TweetModel remove(long id)
    {
        return instance.statusesMap.remove(id);
    }

    public static void putFavoritedStatus(long id)
    {
        instance.favoriteList.add(id);
    }

    public static void removeFavoritedStatus(long id)
    {
        instance.favoriteList.remove(id);
    }

    public static boolean isFavorited(long id)
    {
        return instance.favoriteList.contains(id);
    }

    public static boolean isNotRead(long id)
    {
        int count = 0;
        for (Long l : instance.readRetweetList)
        {
            if (id == l)
            {
                count++;
            }
        }
        return count <= 1;
    }

    public static void putHashtag(String tag)
    {
        if (instance.hashtagList.contains(tag))
        {
            return;
        }
        instance.hashtagList.add(tag);
    }

    public static List<String> getHashtagList()
    {
        return instance.hashtagList;
    }

    public static void clearCache()
    {
        instance.statusesMap.clear();
        instance.favoriteList.clear();
        instance.hashtagList.clear();
        instance.readRetweetList.clear();
    }
}
