package net.miz_hi.smileessence.model.statuslist.timeline;

import net.miz_hi.smileessence.model.statuslist.StatusList;

public abstract class Timeline extends StatusList
{

    public abstract void loadNewer();

    public abstract void loadOlder();

}
