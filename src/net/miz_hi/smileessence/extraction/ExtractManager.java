package net.miz_hi.smileessence.extraction;

import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWordManager;
import net.miz_hi.smileessence.model.status.tweet.EnumTweetType;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.statuslist.StatusList;
import net.miz_hi.smileessence.statuslist.StatusListManager;


public class ExtractManager
{

    public static void check(TweetModel status)
    {
        if (status.type != EnumTweetType.RETWEET)
        {
            StatusList mentions = StatusListManager.getMentionsTimeline();
            if (mentions.getStatusIndex(status) != -1)
            {
                return;
            }
            for (ExtraWord word : ExtraWordManager.getExtraWords())
            {
                if (status.getText().contains(word.getText()))
                {
                    mentions.addToTop(status);
                    mentions.apply();
                    return;
                }
            }
        }
    }
}
