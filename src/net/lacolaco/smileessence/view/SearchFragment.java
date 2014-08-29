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

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenSearch;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.entity.SearchQuery;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.StatusFilter;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.SearchTask;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.adapter.SearchListAdapter;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.SelectSearchQueryDialogFragment;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import java.util.List;

public class SearchFragment extends CustomListFragment implements View.OnClickListener, View.OnFocusChangeListener,
        SearchListAdapter.OnQueryChangeListener
{

    // ------------------------------ FIELDS ------------------------------

    public static final String SEARCH_QUERY_DIALOG = "searchQueryDialog";
    private EditText editText;

    // --------------------- GETTER / SETTER METHODS ---------------------

    private int getAdapterIndex()
    {
        return getArguments().getInt(ADAPTER_INDEX);
    }

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
        switch(v.getId())
        {
            case R.id.button_search_queries:
            {
                openSearchQueryDialog(getMainActivity());
                break;
            }
            case R.id.button_search_execute:
            {
                search();
                break;
            }
            case R.id.button_search_save:
            {
                saveQuery();
            }
        }
    }

    // --------------------- Interface OnFocusChangeListener ---------------------

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(!hasFocus)
        {
            hideIME();
        }
    }

    // --------------------- Interface OnQueryChangeListener ---------------------

    @Override
    public void onQueryChange(String newQuery)
    {
        if(editText != null)
        {
            editText.setText(newQuery);
        }
    }

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
                    notifyTextEmpty(activity);
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
                            StatusViewModel viewModel = new StatusViewModel(status, currentAccount);
                            adapter.addToTop(viewModel);
                            StatusFilter.filter(activity, viewModel);
                        }
                    }
                    updateListViewWithNotice(refreshView.getRefreshableView(), adapter, true);
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
                    notifyTextEmpty(activity);
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
                            StatusViewModel viewModel = new StatusViewModel(status, currentAccount);
                            adapter.addToBottom(viewModel);
                            StatusFilter.filter(activity, viewModel);
                        }
                    }
                    updateListViewWithNotice(refreshView.getRefreshableView(), adapter, false);
                    refreshView.onRefreshComplete();
                }
            }
        }.execute();
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected PullToRefreshListView getListView(View page)
    {
        return (PullToRefreshListView) page.findViewById(R.id.listview_search);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.actionbar_search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View page = inflater.inflate(R.layout.fragment_search, container, false);
        int fragmentIndex = getAdapterIndex();
        PullToRefreshListView listView = getListView(page);
        SearchListAdapter adapter = (SearchListAdapter) getListAdapter(fragmentIndex);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnRefreshListener(this);
        listView.setMode(getRefreshMode());
        ImageButton buttonQueries = getQueriesButton(page);
        buttonQueries.setOnClickListener(this);
        ImageButton buttonExecute = getExecuteButton(page);
        buttonExecute.setOnClickListener(this);
        ImageButton buttonSave = getSaveButton(page);
        buttonSave.setOnClickListener(this);
        editText = getEditText(page);
        editText.setOnFocusChangeListener(this);
        editText.setText(adapter.getQuery());
        editText.setMovementMethod(new ArrowKeyMovementMethod()
        {
            @Override
            protected boolean right(TextView widget, Spannable buffer)
            {
                //Don't move page
                return widget.getSelectionEnd() == widget.length() || super.right(widget, buffer);
            }

            @Override
            protected boolean left(TextView widget, Spannable buffer)
            {
                //Don't move page
                return widget.getSelectionStart() == 0 || super.left(widget, buffer);
            }
        });
        adapter.setOnQueryChangeListener(this);
        return page;
    }

    private EditText getEditText(View page)
    {
        return (EditText) page.findViewById(R.id.edittext_search);
    }

    private ImageButton getExecuteButton(View page)
    {
        return (ImageButton) page.findViewById(R.id.button_search_execute);
    }

    private SearchListAdapter getListAdapter(MainActivity activity)
    {
        return (SearchListAdapter) activity.getListAdapter(MainActivity.ADAPTER_SEARCH);
    }

    private ImageButton getQueriesButton(View page)
    {
        return (ImageButton) page.findViewById(R.id.button_search_queries);
    }

    private ImageButton getSaveButton(View page)
    {
        return (ImageButton) page.findViewById(R.id.button_search_save);
    }

    private void hideIME()
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void notifyTextEmpty(MainActivity activity)
    {
        Notificator.publish(activity, R.string.notice_search_text_empty);
    }

    private void openSearchQueryDialog(final MainActivity mainActivity)
    {
        if(SearchQuery.getAll().size() == 0)
        {
            Notificator.publish(mainActivity, R.string.notice_no_query_exists);
            return;
        }
        DialogHelper.showDialog(mainActivity, new SelectSearchQueryDialogFragment()
        {
            @Override
            protected void deleteQuery(CommandOpenSearch command)
            {
                super.deleteQuery(command);
                if(editText.getText().toString().contentEquals(command.getQuery().query))
                {
                    editText.setText("");
                    mainActivity.setLastSearch("");
                }
                else
                {
                    mainActivity.setLastSearch(editText.getText().toString());
                }
            }

            @Override
            protected void executeCommand(Command command)
            {
                super.executeCommand(command);
                SearchQuery query = ((CommandOpenSearch) command).getQuery();
                editText.setText(query.query);
                hideIME();
            }
        }, SEARCH_QUERY_DIALOG);
    }

    private void saveQuery()
    {
        String text = editText.getText().toString();
        if(TextUtils.isEmpty(text))
        {
            Notificator.publish(getMainActivity(), R.string.notice_query_is_empty, NotificationType.ALERT);
        }
        else
        {
            SearchQuery.saveIfNotFound(text);
            Notificator.publish(getMainActivity(), R.string.notice_query_saved);
        }
    }

    private void search()
    {
        if(editText != null)
        {
            String text = editText.getText().toString();
            if(TextUtils.isEmpty(text))
            {
                Notificator.publish(getMainActivity(), R.string.notice_query_is_empty, NotificationType.ALERT);
            }
            else
            {
                getMainActivity().openSearchPage(text);
                hideIME();
            }
        }
    }
}
