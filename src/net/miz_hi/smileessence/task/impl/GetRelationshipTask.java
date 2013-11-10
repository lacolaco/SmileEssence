package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.cache.RelationshipCache;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.Relationship;
import twitter4j.TwitterException;


public class GetRelationshipTask extends Task<Boolean>
{

    long userId;

    public GetRelationshipTask(long userId)
    {
        this.userId = userId;
    }

    @Override
    public Boolean call()
    {
        try
        {
            Relationship rel = API.getRelationship(Client.getMainAccount(), userId);
            RelationshipCache.put(rel);
            return true;
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onPreExecute()
    {
    }

    @Override
    public void onPostExecute(Boolean result)
    {

    }
}
