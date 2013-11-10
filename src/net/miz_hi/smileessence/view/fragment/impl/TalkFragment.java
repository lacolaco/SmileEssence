package net.miz_hi.smileessence.view.fragment.impl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.talkchase.TalkChaser;
import net.miz_hi.smileessence.talkchase.TalkManager;
import net.miz_hi.smileessence.view.IRemovable;
import net.miz_hi.smileessence.view.fragment.NamedFragment;

@SuppressLint("ValidFragment")
public class TalkFragment extends NamedFragment implements IRemovable
{

    long chasingId = -1;
    int talkId;
    ListView listView;

    private TalkFragment()
    {
    }

    public static TalkFragment newInstance(int talkId, long chasingId)
    {
        TalkFragment relFragment = new TalkFragment();
        relFragment.talkId = talkId;
        relFragment.chasingId = chasingId;
        return relFragment;
    }

    public long getChasingId()
    {
        return chasingId;
    }

    public void setChasingId(long id)
    {
        chasingId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.listpage_layout, container, false);
        listView = (ListView) page.findViewById(R.id.listpage_listview);
        ProgressBar progress = new ProgressBar(getActivity());
        progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        progress.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(progress);
        listView.setEmptyView(progress);
        listView.setFastScrollEnabled(true);
        TalkChaser chaser = TalkManager.getChaser(talkId);
        StatusListAdapter adapter = StatusListManager.getAdapter(chaser.talkList);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new TimelineScrollListener(adapter));
        return page;
    }

    @Override
    public String getTitle()
    {
        return "Talk-" + (talkId + 1);
    }

    @Override
    public void onRemoved()
    {
        TalkChaser chaser = TalkManager.getChaser(talkId);
        chaser.stopRelation(this);
    }
}
