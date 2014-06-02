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

import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.StatusFilter;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.HomeTimelineTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.adapter.StatusListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Paging;
import twitter4j.Twitter;

public class HomeFragment extends CustomListFragment
{

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    protected PullToRefreshBase.Mode getRefreshMode()
    {
        return PullToRefreshBase.Mode.BOTH;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnRefreshListener2 ---------------------

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = (MainActivity) getActivity();
        if(activity.isStreaming())
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    StatusListAdapter adapter = getListAdapter(activity);
                    updateListViewWithNotice(refreshView.getRefreshableView(), adapter, true);
                    refreshView.onRefreshComplete();
                }
            }.post();
            return;
        }
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final StatusListAdapter adapter = getListAdapter(activity);
        Paging paging = TwitterUtils.getPaging(TwitterUtils.getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setSinceId(adapter.getTopID());
        }
        new HomeTimelineTask(twitter, activity, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(int i = statuses.length - 1; i >= 0; i--)
                {
                    twitter4j.Status status = statuses[i];
                    StatusViewModel viewModel = new StatusViewModel(status, currentAccount);
                    adapter.addToTop(viewModel);
                    StatusFilter.filter(activity, viewModel);
                }
                updateListViewWithNotice(refreshView.getRefreshableView(), adapter, true);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final StatusListAdapter adapter = getListAdapter(activity);
        Paging paging = TwitterUtils.getPaging(TwitterUtils.getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setMaxId(adapter.getLastID() - 1);
        }
        new HomeTimelineTask(twitter, activity, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(twitter4j.Status status : statuses)
                {
                    StatusViewModel viewModel = new StatusViewModel(status, currentAccount);
                    adapter.addToBottom(viewModel);
                    StatusFilter.filter(activity, viewModel);
                }
                updateListViewWithNotice(refreshView.getRefreshableView(), adapter, false);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    private StatusListAdapter getListAdapter(MainActivity activity)
    {
        return (StatusListAdapter) activity.getListAdapter(MainActivity.ADAPTER_HOME);
    }
}
