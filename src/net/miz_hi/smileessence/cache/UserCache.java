package net.miz_hi.smileessence.cache;

import net.miz_hi.smileessence.model.status.user.UserModel;

import java.util.concurrent.ConcurrentHashMap;

public class UserCache
{

    private static ConcurrentHashMap<Long, UserModel> usersMap = new ConcurrentHashMap<Long, UserModel>();

    public static void put(UserModel user)
    {
        if (usersMap.containsKey(user.userId))
        {
            usersMap.remove(user.userId);
        }

        usersMap.put(user.userId, user);
    }

    public static UserModel get(long id)
    {
        return usersMap.get(id);
    }

    public static UserModel remove(long id)
    {
        return usersMap.remove(id);
    }

    public static void clearCache()
    {
        usersMap.clear();
    }

}
