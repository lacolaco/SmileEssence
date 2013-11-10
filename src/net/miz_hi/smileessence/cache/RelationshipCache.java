package net.miz_hi.smileessence.cache;

import twitter4j.Relationship;

import java.util.concurrent.ConcurrentHashMap;


public class RelationshipCache
{

    private static ConcurrentHashMap<Long, Relationship> relationshipMap = new ConcurrentHashMap<Long, Relationship>();

    public static void put(Relationship relationship)
    {
        if (relationshipMap.containsKey(relationship.getTargetUserId()))
        {
            relationshipMap.remove(relationship.getTargetUserId());
        }

        relationshipMap.put(relationship.getTargetUserId(), relationship);
    }

    public static Relationship get(long id)
    {
        return relationshipMap.get(id);
    }

    public static Relationship remove(long id)
    {
        return relationshipMap.remove(id);
    }

    public static void clearCache()
    {
        relationshipMap.clear();
    }
}
