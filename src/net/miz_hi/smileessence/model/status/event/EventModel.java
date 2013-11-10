package net.miz_hi.smileessence.model.status.event;

import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.util.StringUtils;

import java.util.Date;

public abstract class EventModel implements Comparable<EventModel>, IStatusModel
{

    protected Date date;
    public UserModel source;

    protected EventModel(UserModel retweeter)
    {
        this.date = new Date();
        this.source = retweeter;
    }

    @Override
    public UserModel getUser()
    {
        return source;
    }

    @Override
    public abstract String getTextTop();

    @Override
    public abstract String getTextContent();

    @Override
    public String getTextBottom()
    {
        return StringUtils.dateToString(date);
    }

    @Override
    public int compareTo(EventModel another)
    {
        return this.date.compareTo(another.date);
    }

    public void raise()
    {
        Notificator.buildEvent(this).raise();
    }
}
