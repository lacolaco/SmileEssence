package net.miz_hi.smileessence.extraction;

import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.model.status.tweet.EnumTweetType;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;


public class ExtractManager
{

    public static boolean matches(TweetModel status)
    {
        if (status.type != EnumTweetType.RETWEET && !net.miz_hi.smileessence.data.extra.ExtractManager.getExtraWords().isEmpty())
        {
            for (ExtraWord word : net.miz_hi.smileessence.data.extra.ExtractManager.getExtraWords())
            {
                if (status.text.contains(word.getText()))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
