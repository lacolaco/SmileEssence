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

import android.text.TextUtils;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.SearchTask;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.adapter.SearchListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import java.util.List;

public class SearchFragment extends CustomListFragment
{

// --------------------- GETTER / SETTER METHODS ---------------------

    private MainActivity getMainActivity()
    {
        return (MainActivity) getActivity();
    }

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
        final MainActivity activity = getMainActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final SearchListAdapter adapter = getListAdapter(activity);
        String queryString = adapter.getQuery();
        if(TextUtils.isEmpty(queryString))
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    refreshView.onRefreshComplete();
                }
            }.post();
            return;
        }
        final Query query = SearchTask.getBaseQuery(activity, queryString);
        if(adapter.getCount() > 0)
        {
            query.setSinceId(adapter.getTopID());
        }
        new SearchTask(twitter, query, activity)
        {
            @Override
            protected void onPostExecute(QueryResult queryResult)
            {
                super.onPostExecute(queryResult);
                if(queryResult != null)
                {
                    java.util.List<twitter4j.Status> tweets = queryResult.getTweets();
                    for(int i = tweets.size() - 1; i >= 0; i--)
                    {
                        twitter4j.Status status = tweets.get(i);
                        if(!status.isRetweet())
                        {
                            adapter.addToTop(new StatusViewModel(status, currentAccount));
                        }
                    }
                    updateListViewWithNotice(refreshView.getRefreshableView(), adapter, false);
                    adapter.setTopID(queryResult.getMaxId());
                    refreshView.onRefreshComplete();
                }
            }
        }.execute();
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = getMainActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final SearchListAdapter adapter = getListAdapter(activity);
        String queryString = adapter.getQuery();
        if(TextUtils.isEmpty(queryString))
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    refreshView.onRefreshComplete();
                }
            }.post();
            return;
        }
        final Query query = SearchTask.getBaseQuery(activity, queryString);
        if(adapter.getCount() > 0)
        {
            query.setMaxId(adapter.getLastID() - 1);
        }
        new SearchTask(twitter, query, activity)
        {
            @Override
            protected void onPostExecute(QueryResult queryResult)
            {
                super.onPostExecute(queryResult);
                if(queryResult != null)
                {
                    List<twitter4j.Status> tweets = queryResult.getTweets();
                    for(twitter4j.Status status : tweets)
                    {
                        if(!status.isRetweet())
                        {
                            adapter.addToBottom(new StatusViewModel(status, currentAccount));
                        }
                    }
                    updateListViewWithNotice(refreshView.getRefreshableView(), adapter, false);
                    refreshView.onRefreshComplete();
                }
            }
        }.execute();
    }

    private SearchListAdapter getListAdapter(MainActivity activity)
    {
        return (SearchListAdapter) activity.getListAdapter(MainActivity.PAGE_SEARCH);
    }
}
