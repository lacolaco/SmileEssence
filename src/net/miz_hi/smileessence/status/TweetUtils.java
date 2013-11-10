package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.cache.TweetCache;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.model.status.ResponseConverter;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TweetUtils
{

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

    public static TweetModel getOrCreateStatusModel(final long id)
    {
        TweetModel statusModel = TweetCache.get(id);
        if (statusModel == null)
        {
            Future<Status> f = MyExecutor.submit(new Callable<Status>()
            {

                @Override
                public Status call() throws Exception
                {
                    return API.getStatus(Client.getMainAccount(), id);
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
            if (status == null)
            {
                return null;
            }
            statusModel = ResponseConverter.convert(status);
        }
        return statusModel;
    }

}
