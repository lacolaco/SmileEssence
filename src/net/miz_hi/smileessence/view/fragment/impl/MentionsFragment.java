package net.miz_hi.smileessence.view.fragment.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.util.CustomListAdapter;
import net.miz_hi.smileessence.view.fragment.NamedFragment;

public class MentionsFragment extends NamedFragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.listpage_layout, container, false);
        ListView listView = (ListView) page.findViewById(R.id.listpage_listview);
        ProgressBar progress = new ProgressBar(getActivity());
        progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        progress.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(progress);
        listView.setEmptyView(progress);
        listView.setFastScrollEnabled(true);
        CustomListAdapter<?> adapter = StatusListManager.getAdapter(StatusListManager.getMentionsTimeline());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new TimelineScrollListener(adapter));

        return page;
    }

    @Override
    public String getTitle()
    {
        return "Mentions";
    }

}
