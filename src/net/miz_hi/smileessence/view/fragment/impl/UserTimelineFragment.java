package net.miz_hi.smileessence.view.fragment.impl;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.model.statuslist.timeline.Timeline;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.task.impl.GetUserTask;
import net.miz_hi.smileessence.task.impl.GetUserTimelineTask;
import net.miz_hi.smileessence.view.IRemovable;
import net.miz_hi.smileessence.view.fragment.NamedFragment;
import twitter4j.Paging;

import java.util.Collections;
import java.util.List;

@SuppressLint("ValidFragment")
public class UserTimelineFragment extends NamedFragment implements IRemovable, OnClickListener
{

    UserModel user;

    private UserTimelineFragment()
    {
    }

    ;

    public static UserTimelineFragment newInstance(UserModel user)
    {
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.user = user;
        return fragment;
    }

    @Override
    public String getTitle()
    {
        return user.screenName + "'s Timeline";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.listpage_refresh_layout, container, false);
        ListView listView = (ListView) page.findViewById(R.id.listpage_listview);
        ProgressBar progress = new ProgressBar(getActivity());
        progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        progress.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(progress);
        listView.setEmptyView(progress);
        listView.setFastScrollEnabled(true);

        StatusListAdapter adapter = StatusListManager.getAdapter(StatusListManager.getUserTimeline(user.userId));
        getUserTimeline();
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new TimelineScrollListener(adapter));
        Button refresh = (Button) page.findViewById(R.id.listpage_refresh);
        refresh.setOnClickListener(this);

        return page;
    }

    private void getUserTimeline()
    {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "取得中...");
        MyExecutor.execute(new Runnable()
        {
            public void run()
            {
                Timeline timeline = StatusListManager.getUserTimeline(user.userId);
                StatusListAdapter adapter = StatusListManager.getAdapter(timeline);
                user.updateData(new GetUserTask(user.userId).call());
                List<TweetModel> list;
                if (timeline.getStatusList().length > 0)
                {
                    long lastId = ((TweetModel) timeline.getStatus(0)).statusId;
                    list = new GetUserTimelineTask(Client.getMainAccount(), user.userId, new Paging(1, 20, lastId)).call();
                }
                else
                {
                    list = new GetUserTimelineTask(Client.getMainAccount(), user.userId, new Paging(1, 20)).call();
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
        StatusListManager.removeUserTimeline(user.userId);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.listpage_refresh)
        {
            getUserTimeline();
        }
    }

}
