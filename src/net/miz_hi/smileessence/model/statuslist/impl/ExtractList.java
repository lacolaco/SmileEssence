package net.miz_hi.smileessence.model.statuslist.impl;

import net.miz_hi.smileessence.extraction.ExtractManager;
import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.statuslist.StatusList;


public class ExtractList extends StatusList
{

    @Override
    public boolean checkStatus(IStatusModel status)
    {
        return status instanceof TweetModel && ExtractManager.matches((TweetModel) status);
    }

}
