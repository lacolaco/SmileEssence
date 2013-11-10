package net.miz_hi.smileessence.model.statuslist.impl;

import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.status.event.EventModel;
import net.miz_hi.smileessence.model.statuslist.StatusList;


public class HistoryList extends StatusList
{

    @Override
    public boolean checkStatus(IStatusModel status)
    {
        return status instanceof EventModel;
    }

}
