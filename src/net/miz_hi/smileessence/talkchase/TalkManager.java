package net.miz_hi.smileessence.talkchase;

import android.util.SparseArray;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

/**
 * Relationの管理
 * 実際のRelation処理はRelationLogicが行う
 */
public class TalkManager
{

    static int count = 0;
    static SparseArray<TalkChaser> chaserList = new SparseArray<TalkChaser>();

    public static void addTalkChaser(TalkChaser chaser)
    {
        chaserList.put(count++, chaser);
    }

    public static int getNextTalkId()
    {
        return count;
    }

    public static TalkChaser getChaser(int talkId)
    {
        return chaserList.get(talkId);
    }

    public static void removeTalkChaser(TalkChaser chaser)
    {
        chaserList.remove(chaserList.indexOfValue(chaser));
        count--;
    }

    public static TalkChaser getTalkByChasingId(long statusId)
    {
        for (int i = 0; i < chaserList.size(); i++)
        {
            TalkChaser chaser = chaserList.valueAt(i);
            if (chaser.chasingId == statusId)
            {
                return chaser;
            }
        }
        return null;
    }

    public static TalkChaser searchTalk(TweetModel tweet)
    {
        for (int i = 0; i < chaserList.size(); i++)
        {
            TalkChaser chaser = chaserList.valueAt(i);
            if (chaser.talkList.getStatusIndex(tweet) != -1)
            {
                return chaser;
            }
        }
        return null;
    }

    public static void check(TweetModel tweet)
    {
        for (int i = 0; i < chaserList.size(); i++)
        {
            TalkChaser chaser = chaserList.valueAt(i);
            if (tweet.getInReplyToStatusId() == chaser.chasingId)
            {
                chaser.hitNewTweet(tweet);
                break;
            }
        }
    }
}
