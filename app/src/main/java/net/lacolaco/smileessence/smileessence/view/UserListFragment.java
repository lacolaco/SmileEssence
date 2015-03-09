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

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.StatusFilter;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.UserListStatusesTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.SelectUserListDialogFragment;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import net.lacolaco.smileessence.viewmodel.UserListListAdapter;
import twitter4j.Paging;
import twitter4j.Twitter;

public class UserListFragment extends CustomListFragment implements View.OnClickListener
{

    // ------------------------------ FIELDS ------------------------------

    private TextView textListName;

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


    // --------------------- Interface OnClickListener ---------------------

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch(id)
        {
            case R.id.button_userlist_lists:
            {
                openUserListsDialog(getMainActivity());
                break;
            }
        }
    }

    // --------------------- Interface OnRefreshListener2 ---------------------

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = getMainActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final UserListListAdapter adapter = getListAdapter(activity);
        String listFullName = adapter.getListFullName();
        if(TextUtils.isEmpty(listFullName))
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    notifyTextEmpty(activity);
                    refreshView.onRefreshComplete();
                }
            }.post();
            return;
        }
        Paging paging = TwitterUtils.getPaging(TwitterUtils.getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setSinceId(adapter.getTopID());
        }
        new UserListStatusesTask(twitter, listFullName, activity, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(int i = statuses.length - 1; i >= 0; i--)
                {
                    twitter4j.Status status = statuses[i];
                    StatusViewModel statusViewModel = new StatusViewModel(status, activity.getCurrentAccount());
                    adapter.addToTop(statusViewModel);
                    StatusFilter.filter(activity, statusViewModel);
                }
                updateListViewWithNotice(refreshView.getRefreshableView(), adapter, true);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = getMainActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final UserListListAdapter adapter = getListAdapter(activity);
        String listFullName = adapter.getListFullName();
        if(TextUtils.isEmpty(listFullName))
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    notifyTextEmpty(activity);
                    refreshView.onRefreshComplete();
                }
            }.post();
            return;
        }
        Paging paging = TwitterUtils.getPaging(TwitterUtils.getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setMaxId(adapter.getLastID() - 1);
        }
        new UserListStatusesTask(twitter, listFullName, activity, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(int i = 0; i < statuses.length; i++)
                {
                    twitter4j.Status status = statuses[i];
                    StatusViewModel statusViewModel = new StatusViewModel(status, activity.getCurrentAccount());
                    adapter.addToBottom(statusViewModel);
                    StatusFilter.filter(activity, statusViewModel);
                }
                updateListViewWithNotice(refreshView.getRefreshableView(), adapter, false);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected PullToRefreshListView getListView(View page)
    {
        return (PullToRefreshListView) page.findViewById(R.id.listview_userlist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.fragment_userlist, container, false);
        PullToRefreshListView listView = getListView(page);
        UserListListAdapter adapter = getListAdapter((MainActivity) getActivity());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnRefreshListener(this);
        listView.setMode(getRefreshMode());
        ImageButton buttonUserLists = getUserListsButton(page);
        buttonUserLists.setOnClickListener(this);
        textListName = getTextListName(page);
        textListName.setText(adapter.getListFullName());
        return page;
    }

    private UserListListAdapter getListAdapter(MainActivity activity)
    {
        return (UserListListAdapter) getListAdapter(MainActivity.ADAPTER_USERLIST);
    }

    private TextView getTextListName(View page)
    {
        return (TextView) page.findViewById(R.id.textview_userlist_name);
    }

    private ImageButton getUserListsButton(View page)
    {
        return (ImageButton) page.findViewById(R.id.button_userlist_lists);
    }

    private void notifyTextEmpty(MainActivity activity)
    {
        Notificator.publish(activity, R.string.notice_userlist_not_selected);
    }

    private void openUserListsDialog(final MainActivity mainActivity)
    {
        DialogHelper.showDialog(mainActivity, new SelectUserListDialogFragment()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                super.onDismiss(dialog);
                textListName.setText(getListAdapter(mainActivity).getListFullName());
            }
        });
    }
}
