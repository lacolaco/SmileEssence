package net.miz_hi.smileessence.view.fragment.impl;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.statuslist.timeline.Timeline;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.task.impl.GetListTimelineTask;
import net.miz_hi.smileessence.view.IRemovable;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.NamedFragment;
import twitter4j.Paging;

import java.util.Collections;
import java.util.List;

public class ListFragment extends NamedFragment implements IRemovable, OnClickListener
{

    String name;
    int id;
    boolean inited;

    public static ListFragment newInstance(int id, String fullName)
    {
        ListFragment fragment = new ListFragment();
        fragment.name = fullName;
        fragment.id = id;
        fragment.inited = false;
        return fragment;
    }


    @Override
    public String getTitle()
    {
        return name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.listpage_refresh_layout, container, false);
        ListView listView = (ListView) page.findViewById(R.id.listpage_listview);
        listView.setFastScrollEnabled(true);
        StatusListAdapter adapter = StatusListManager.getAdapter(StatusListManager.getListTimeline(id));
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new TimelineScrollListener(adapter));
        Button refresh = (Button) page.findViewById(R.id.listpage_refresh);
        refresh.setOnClickListener(this);
        return page;
    }

    public void refresh()
    {
        inited = true;
        final ProgressDialog pd = ProgressDialog.show(MainActivity.getInstance(), "", name + "を取得中...");
        MyExecutor.execute(new Runnable()
        {
            public void run()
            {

                Timeline timeline = StatusListManager.getListTimeline(id);
                StatusListAdapter adapter = StatusListManager.getAdapter(timeline);
                List<TweetModel> list;
                if (timeline.getStatusList().length > 0)
                {
                    long lastId = ((TweetModel) timeline.getStatus(0)).statusId;
                    list = new GetListTimelineTask(Client.getMainAccount(), id, new Paging(1, 50, lastId)).call();
                }
                else
                {
                    list = new GetListTimelineTask(Client.getMainAccount(), id, new Paging(1, 50)).call();
                }

                Collections.reverse(list);
                for (TweetModel status : list)
                {
                    timeline.addToTop(status);
                }
                adapter.forceNotifyAdapter();
                pd.dismiss();
            }
        });
    }

    @Override
    public void onRemoved()
    {
        StatusListManager.removeListTimeline(id);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.listpage_refresh)
        {
            refresh();
        }
    }

    public boolean isNotInited()
    {
        return !inited;
    }

}
