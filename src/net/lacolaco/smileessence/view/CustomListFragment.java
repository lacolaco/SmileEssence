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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;

public class CustomListFragment extends Fragment implements AbsListView.OnScrollListener,
        PullToRefreshBase.OnRefreshListener2<ListView>
{

    // ------------------------------ FIELDS ------------------------------

    public static final String ADAPTER_INDEX = "fragmentIndex";
    public static final int SCROLL_DURATION = 1500;

    private int fragmentIndex;

    // --------------------- GETTER / SETTER METHODS ---------------------

    protected PullToRefreshBase.Mode getRefreshMode()
    {
        return PullToRefreshBase.Mode.DISABLED;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnRefreshListener2 ---------------------

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
    {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
    {

    }

    // --------------------- Interface OnScrollListener ---------------------

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState)
    {
        Bundle args = getArguments();
        fragmentIndex = args.getInt(ADAPTER_INDEX);
        CustomListAdapter<?> adapter = getListAdapter(fragmentIndex);
        adapter.setNotifiable(false);

        if(absListView.getFirstVisiblePosition() == 0 && absListView.getChildAt(0) != null && absListView.getChildAt(0).getTop() == 0)
        {
            if(scrollState == SCROLL_STATE_IDLE)
            {
                updateListViewWithNotice(absListView, adapter, true);
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3)
    {
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        fragmentIndex = args.getInt(ADAPTER_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle args = getArguments();
        int fragmentIndex = args.getInt(ADAPTER_INDEX);
        PullToRefreshListView listView = getListView(page);
        ListAdapter adapter = getListAdapter(fragmentIndex);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnRefreshListener(this);
        listView.setMode(getRefreshMode());
        return page;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(ADAPTER_INDEX, fragmentIndex);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            fragmentIndex = savedInstanceState.getInt(ADAPTER_INDEX);
        }
    }

    protected CustomListAdapter<?> getListAdapter(int fragmentIndex)
    {
        return ((MainActivity) getActivity()).getListAdapter(fragmentIndex);
    }

    protected PullToRefreshListView getListView(View page)
    {
        return (PullToRefreshListView) page.findViewById(R.id.fragment_list_listview);
    }

    protected void notifyListUpdated(int increments)
    {
        Notificator.publish(getActivity(), getString(R.string.notice_timeline_new, increments));
    }

    protected void updateListViewWithNotice(AbsListView absListView, CustomListAdapter<?> adapter, boolean addedToTop)
    {
        int before = adapter.getCount();
        adapter.notifyDataSetChanged(); // synchronized call (not adapter#updateForce())
        int after = adapter.getCount();
        int increments = after - before;
        if(increments > 0)
        {
            adapter.setNotifiable(false);
            notifyListUpdated(increments);
            if(addedToTop)
            {
                absListView.setSelection(increments + 1);
                absListView.smoothScrollToPositionFromTop(increments, 0);
                absListView.setSelection(increments);
            }
            else
            {
                absListView.smoothScrollToPositionFromTop(before, 0);
            }

            if(increments == 1)
            {
                adapter.setNotifiable(true);
            }
        }
        else
        {
            adapter.setNotifiable(true);
        }
    }
}
