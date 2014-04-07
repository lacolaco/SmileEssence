/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;

public class CustomListFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener
{

    public static final String FRAGMENT_INDEX = "fragmentIndex";

    private int fragmentIndex;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int fragmentIndex = args.getInt(FRAGMENT_INDEX);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_INDEX, fragmentIndex);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            fragmentIndex = savedInstanceState.getInt(FRAGMENT_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.fragment_list, container, false);
        SwipeRefreshLayout refreshLayout = getRefreshLayout(page);
        Bundle args = getArguments();
        int fragmentIndex = args.getInt(FRAGMENT_INDEX);
        ListView listView = getListView(page);
        ListAdapter adapter = getListAdapter(fragmentIndex);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        refreshLayout.setOnRefreshListener(this);
        return page;
    }

    private SwipeRefreshLayout getRefreshLayout(View page)
    {
        return (SwipeRefreshLayout)page.findViewById(R.id.fragment_list_refreshview);
    }

    private CustomListAdapter<?> getListAdapter(int fragmentIndex)
    {
        return ((MainActivity)getActivity()).getListAdapter(fragmentIndex);
    }

    private ListView getListView(View page)
    {
        return (ListView)page.findViewById(R.id.fragment_list_listview);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState)
    {
        Bundle args = getArguments();
        fragmentIndex = args.getInt(FRAGMENT_INDEX);
        CustomListAdapter<?> adapter = getListAdapter(fragmentIndex);
        adapter.setNotifiable(false);

        if(absListView.getFirstVisiblePosition() == 0 && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() == 0)
        {
            if(scrollState == SCROLL_STATE_IDLE)
            {
                adapter.setNotifiable(true);
                int before = adapter.getCount();
                adapter.notifyDataSetChanged();
                int after = adapter.getCount();
                int addCount = after - before;
                if(addCount > 0)
                {
                    absListView.setSelection(addCount + 1);
                    adapter.setNotifiable(false);
                    new Notificator(getActivity(), getString(R.string.notice_timeline_new, addCount)).publish();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3)
    {
    }

    @Override
    public void onRefresh()
    {
        getRefreshLayout(getView()).setRefreshing(false);
    }
}
