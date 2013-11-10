package net.miz_hi.smileessence.statuslist;

import android.app.Activity;
import android.util.SparseArray;
import net.miz_hi.smileessence.model.statuslist.StatusList;
import net.miz_hi.smileessence.model.statuslist.impl.HistoryList;
import net.miz_hi.smileessence.model.statuslist.timeline.Timeline;
import net.miz_hi.smileessence.model.statuslist.timeline.impl.HomeTimeline;
import net.miz_hi.smileessence.model.statuslist.timeline.impl.MentionsTimeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatusListManager
{

    private Timeline home;
    private Timeline mentions;
    private StatusList history;
    private ArrayList<StatusList> tweetLists = new ArrayList<StatusList>();
    private HashMap<Long, Timeline> userTimelineMap = new HashMap<Long, Timeline>();
    private SparseArray<Timeline> listTimelineMap = new SparseArray<Timeline>();
    private HashMap<StatusList, StatusListAdapter> adapterMap = new HashMap<StatusList, StatusListAdapter>();
    private static final StatusListManager instance = new StatusListManager();

    public static void initStatusLists(Activity activity)
    {
        Timeline ho = new HomeTimeline();
        MentionsTimeline m = new MentionsTimeline();
        StatusList hi = new HistoryList();
        setHomeTimeline(ho, new StatusListAdapter(activity, ho));
        setMentionsTimeline(m, new StatusListAdapter(activity, m));
        setHistoryTimeline(hi, new StatusListAdapter(activity, hi));
    }

    public static void registerTweetList(StatusList timeline, StatusListAdapter adapter)
    {
        instance.tweetLists.add(timeline);
        instance.adapterMap.put(timeline, adapter);
    }

    public static void removeTweetList(StatusList timeline)
    {
        instance.tweetLists.remove(timeline);
        instance.adapterMap.remove(timeline);
    }

    public static List<StatusList> getTweetLists()
    {
        return instance.tweetLists;
    }

    public static void setHomeTimeline(Timeline timeline, StatusListAdapter adapter)
    {
        instance.home = timeline;
        registerTweetList(timeline, adapter);
    }

    public static Timeline getHomeTimeline()
    {
        return instance.home;
    }

    public static void setMentionsTimeline(Timeline timeline, StatusListAdapter adapter)
    {
        instance.mentions = timeline;
        registerTweetList(timeline, adapter);
    }

    public static Timeline getMentionsTimeline()
    {
        return instance.mentions;
    }

    public static void setHistoryTimeline(StatusList timeline, StatusListAdapter adapter)
    {
        instance.history = timeline;
        instance.adapterMap.put(timeline, adapter);
    }

    public static StatusList getHistoryTimeline()
    {
        return instance.history;
    }

    public static void registerUserTimeline(long userId, Timeline timeline, StatusListAdapter adapter)
    {
        instance.userTimelineMap.put(userId, timeline);
        registerTweetList(timeline, adapter);
    }

    public static void removeUserTimeline(long userId)
    {
        instance.adapterMap.remove(instance.userTimelineMap.remove(userId));
    }

    public static Timeline getUserTimeline(long userId)
    {
        return instance.userTimelineMap.get(userId);
    }

    public static void registerListTimeline(int id, Timeline timeline, StatusListAdapter adapter)
    {
        instance.listTimelineMap.put(id, timeline);
        registerTweetList(timeline, adapter);
    }

    public static void removeUserTimeline(int id)
    {
        Timeline timeline = instance.listTimelineMap.get(id);
        instance.adapterMap.remove(timeline);
        instance.listTimelineMap.remove(id);
    }

    public static Timeline getListTimeline(int id)
    {
        return instance.listTimelineMap.get(id);
    }

    public static StatusListAdapter getAdapter(StatusList key)
    {
        return instance.adapterMap.get(key);
    }
}
