package net.miz_hi.smileessence.view.fragment.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.util.CustomListAdapter;
import net.miz_hi.smileessence.view.fragment.NamedFragment;

public class HistoryFragment extends NamedFragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout page = (LinearLayout) inflater.inflate(R.layout.listpage_layout, container, false);
        ListView listView = (ListView) page.findViewById(R.id.listpage_listview);
        TextView text = new TextView(getActivity());
        text.setText("ふぁぼられたり、リツイートされたりした履歴が表示されます");
        text.setTextColor(Client.getColor(R.color.Gray2));
        text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        text.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(text);
        listView.setEmptyView(text);
        listView.setFastScrollEnabled(true);
        CustomListAdapter<?> adapter = StatusListManager.getAdapter(StatusListManager.getHistoryTimeline());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new TimelineScrollListener(adapter));

        return page;
    }

    @Override
    public String getTitle()
    {
        return "History";
    }

}
