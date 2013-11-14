package net.miz_hi.smileessence.model.status.event;

import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;

public abstract class StatusEvent extends EventModel
{

    public TweetModel tweet;

    public StatusEvent(UserModel retweeter, TweetModel tweet)
    {
        super(retweeter);
        this.tweet = tweet;
    }

    @Override
    public String getTextContent()
    {
        return tweet.getText();
    }
}
