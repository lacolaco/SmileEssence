package net.miz_hi.smileessence.model.status;

import net.miz_hi.smileessence.cache.TweetCache;
import net.miz_hi.smileessence.cache.UserCache;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterResponse;
import twitter4j.User;


public final class ResponseConverter
{

    private final TwitterResponse response;
    private IStatusModel model;

    public static <T> T convert(TwitterResponse response)
    {
        ResponseConverter conv = new ResponseConverter(response);
        return (T) conv.build();
    }

    private ResponseConverter(TwitterResponse response)
    {
        this.response = response;
    }

    private void convert()
    {
        if (response instanceof Status)
        {
            model = new TweetModel((Status) response);
            TweetCache.put((TweetModel) model);
            return;
        }
        else if (response instanceof DirectMessage)
        {

            return;
        }
        else if (response instanceof User)
        {
            model = new UserModel((User) response);
            UserCache.put((UserModel) model);
            return;
        }
        else
        {
            return;
        }
    }

    private IStatusModel build()
    {
        convert();
        return model;
    }

}
