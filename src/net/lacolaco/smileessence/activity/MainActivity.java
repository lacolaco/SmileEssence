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

package net.lacolaco.smileessence.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import net.lacolaco.smileessence.Application;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.entity.SearchQuery;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.preference.AppPreferenceHelper;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.twitter.OAuthSession;
import net.lacolaco.smileessence.twitter.StatusExtractor;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.UserStreamListener;
import net.lacolaco.smileessence.twitter.task.*;
import net.lacolaco.smileessence.util.BitmapURLTask;
import net.lacolaco.smileessence.util.NetworkHelper;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.*;
import net.lacolaco.smileessence.view.adapter.*;
import net.lacolaco.smileessence.view.dialog.ConfirmDialogFragment;
import net.lacolaco.smileessence.viewmodel.MessageViewModel;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import net.lacolaco.smileessence.viewmodel.menu.MainActivityMenuHelper;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity
{

    // ------------------------------ FIELDS ------------------------------

    public static final int REQUEST_OAUTH = 10;
    public static final int REQUEST_GET_PICTURE_FROM_GALLERY = 11;
    public static final int REQUEST_GET_PICTURE_FROM_CAMERA = 12;
    public static final int PAGE_POST = 0;
    public static final int PAGE_HOME = 1;
    public static final int PAGE_MENTIONS = 2;
    public static final int PAGE_MESSAGES = 3;
    public static final int PAGE_HISTORY = 4;
    public static final int PAGE_SEARCH = 5;
    public static final int PAGE_LIST = 6;
    private static final String lastUsedAccountIDKey = "lastUsedAccountID";
    private ViewPager viewPager;
    private PageListAdapter pagerAdapter;
    private OAuthSession oauthSession;
    private Account currentAccount;
    private TwitterStream stream;
    private HashMap<Integer, CustomListAdapter<?>> adapterMap = new HashMap<>();
    private boolean streaming = false;
    private Uri cameraTempFilePath;

    // --------------------- GETTER / SETTER METHODS ---------------------

    private AppPreferenceHelper getAppPreferenceHelper()
    {
        return new AppPreferenceHelper(this);
    }

    public Uri getCameraTempFilePath()
    {
        return cameraTempFilePath;
    }

    public void setCameraTempFilePath(Uri cameraTempFilePath)
    {
        this.cameraTempFilePath = cameraTempFilePath;
    }

    public Account getCurrentAccount()
    {
        return currentAccount;
    }

    public void setCurrentAccount(Account account)
    {
        this.currentAccount = account;
    }

    public int getCurrentPageIndex()
    {
        return this.viewPager.getCurrentItem();
    }

    private long getLastUsedAccountID()
    {
        String id = getAppPreferenceHelper().getValue(lastUsedAccountIDKey, "");
        if(TextUtils.isEmpty(id))
        {
            return -1;
        }
        else
        {
            return Long.parseLong(id);
        }
    }

    public int getPageCount()
    {
        return pagerAdapter.getCount();
    }

    public PageListAdapter getPagerAdapter()
    {
        return pagerAdapter;
    }

    public int getThemeIndex()
    {
        return ((Application) getApplication()).getThemeIndex();
    }

    public UserPreferenceHelper getUserPreferenceHelper()
    {
        return new UserPreferenceHelper(this);
    }

    public String getVersion()
    {
        return getString(R.string.app_version);
    }

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    private boolean isAuthorized()
    {
        return getLastUsedAccountID() >= 0;
    }

    private boolean isFirstLaunchThisVersion()
    {
        return !getVersion().contentEquals(getAppPreferenceHelper().getValue("app.version", ""));
    }

    /**
     * Returns which twitter stream is running
     *
     * @return
     */
    public boolean isStreaming()
    {
        return streaming;
    }

    public void setStreaming(boolean streaming)
    {
        this.streaming = streaming;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface Callback ---------------------

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if(event.getAction() != KeyEvent.ACTION_DOWN)
        {
            return super.dispatchKeyEvent(event);
        }
        switch(event.getKeyCode())
        {
            case KeyEvent.KEYCODE_BACK:
            {
                finish();
                return false;
            }
            default:
            {
                return super.dispatchKeyEvent(event);
            }
        }
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case REQUEST_OAUTH:
            {
                receiveOAuth(requestCode, resultCode, data);
                break;
            }
            case REQUEST_GET_PICTURE_FROM_GALLERY:
            case REQUEST_GET_PICTURE_FROM_CAMERA:
            {
                getImageUri(requestCode, data);
                break;
            }
        }
    }

    private void getImageUri(int requestCode, Intent data)
    {
        try
        {
            Uri uri;
            if(requestCode == REQUEST_GET_PICTURE_FROM_GALLERY)
            {
                uri = data.getData();
            }
            else
            {
                uri = cameraTempFilePath;
            }
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            PostState.getState().beginTransaction().setMediaFilePath(path).commit();
            setSelectedPageIndex(PAGE_POST);
            new Notificator(this, R.string.notice_select_image_succeeded).publish();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            new Notificator(this, R.string.notice_select_image_failed).publish();
        }
    }

    public void setSelectedPageIndex(int position)
    {
        getViewPager().setCurrentItem(position, true);
    }

    private void receiveOAuth(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
        {
            Logger.error(requestCode);
            new Notificator(this, R.string.notice_error_authenticate).publish();
            finish();
        }
        else
        {
            AccessToken token = oauthSession.getAccessToken(data.getData());
            Account account = new Account(token.getToken(), token.getTokenSecret(), token.getUserId(), token.getScreenName());
            account.save();
            setCurrentAccount(account);
            getAppPreferenceHelper().putValue(lastUsedAccountIDKey, account.getId());
            startMainLogic();
        }
    }

    @Override
    public void finish()
    {
        if(getCurrentPageIndex() != PAGE_HOME)
        {
            getViewPager().setCurrentItem(1, true);
        }
        else
        {
            finish(getUserPreferenceHelper().getValue(R.string.key_setting_show_confirm_dialog, true));
        }
    }

    public void startMainLogic()
    {
        initializeView();
        versionCheck();
        startTwitter();
    }

    private void initializeView()
    {
        ActionBar bar = getActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PageListAdapter(this, viewPager);
        initializePages();
        //viewPager.setOffscreenPageLimit(viewPager.getChildCount());
    }

    private void initializePages()
    {
        addPage(getString(R.string.page_name_post), PostFragment.class, null, true);
        StatusListAdapter homeAdapter = new StatusListAdapter(this);
        StatusListAdapter mentionsAdapter = new StatusListAdapter(this);
        MessageListAdapter messagesAdapter = new MessageListAdapter(this);
        EventListAdapter historyAdapter = new EventListAdapter(this);
        SearchListAdapter searchAdapter = new SearchListAdapter(this);
        addListPage(getString(R.string.page_name_home), HomeFragment.class, homeAdapter, false);
        addListPage(getString(R.string.page_name_mentions), MentionsFragment.class, mentionsAdapter, false);
        addListPage(getString(R.string.page_name_messages), MessagesFragment.class, messagesAdapter, false);
        addListPage(getString(R.string.page_name_history), HistoryFragment.class, historyAdapter, false);
        addListPage(getString(R.string.page_name_search), SearchFragment.class, searchAdapter, false);
        pagerAdapter.refreshListNavigation();
        PostState.newState().beginTransaction().commit();
        setSelectedPageIndex(PAGE_HOME, false);
    }

    public boolean addListPage(String name, Class<? extends CustomListFragment> fragmentClass, CustomListAdapter<?> adapter, boolean withNotify)
    {
        int nextPosition = pagerAdapter.getCount();
        Bundle args = new Bundle();
        args.putInt(CustomListFragment.ADAPTER_INDEX, nextPosition);
        if(addPage(name, fragmentClass, args, withNotify))
        {
            adapterMap.put(nextPosition, adapter);
            return true;
        }
        return false;
    }

    public boolean addPage(String name, Class<? extends Fragment> fragmentClass, Bundle args, boolean withNotify)
    {
        if(withNotify)
        {
            return this.pagerAdapter.addPage(name, fragmentClass, args);
        }
        else
        {
            return this.pagerAdapter.addPageWithoutNotify(name, fragmentClass, args);
        }
    }

    public void setSelectedPageIndex(final int position, final boolean smooth)
    {
        new UIHandler()
        {
            @Override
            public void run()
            {
                getViewPager().setCurrentItem(position, smooth);
            }
        }.post();
    }

    public boolean startTwitter()
    {
        if(!startStream())
        {
            return false;
        }
        int count = getUserPreferenceHelper().getValue(R.string.key_setting_timelines, 20);
        Twitter twitter = new TwitterApi(currentAccount).getTwitter();
        Paging paging = new Paging().count(count);
        HomeTimelineTask homeTask = new HomeTimelineTask(twitter, this, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                CustomListAdapter<?> adapter = getListAdapter(PAGE_HOME);
                for(twitter4j.Status status : statuses)
                {
                    adapter.addToBottom(new StatusViewModel(status, currentAccount));
                }
                adapter.updateForce();
            }
        };
        MentionsTimelineTask mentionsTask = new MentionsTimelineTask(twitter, this, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                CustomListAdapter<?> adapter = getListAdapter(PAGE_MENTIONS);
                for(twitter4j.Status status : statuses)
                {
                    adapter.addToBottom(new StatusViewModel(status, currentAccount));
                }
                adapter.updateForce();
            }
        };
        DirectMessagesTask messagesTask = new DirectMessagesTask(twitter, this, paging)
        {
            @Override
            protected void onPostExecute(DirectMessage[] directMessages)
            {
                super.onPostExecute(directMessages);
                CustomListAdapter<?> adapter = getListAdapter(PAGE_MESSAGES);
                for(DirectMessage message : directMessages)
                {
                    adapter.addToBottom(new MessageViewModel(message, currentAccount));
                }
                adapter.updateForce();
            }
        };
        homeTask.execute();
        mentionsTask.execute();
        messagesTask.execute();
        updateActionBarIcon();
        return true;
    }

    public CustomListAdapter<?> getListAdapter(int i)
    {
        return adapterMap.get(i);
    }

    public boolean startStream()
    {
        if(!new NetworkHelper(this).canConnect())
        {
            return false;
        }
        if(stream != null)
        {
            stream.shutdown();
        }
        stream = new TwitterApi(currentAccount).getTwitterStream();
        UserStreamListener listener = new UserStreamListener(this);
        stream.addListener(listener);
        stream.addConnectionLifeCycleListener(listener);
        stream.user();
        return true;
    }

    public boolean updateActionBarIcon()
    {
        Twitter twitter = new TwitterApi(currentAccount).getTwitter();
        final ImageView homeIcon = (ImageView) findViewById(android.R.id.home);
        ShowUserTask userTask = new ShowUserTask(twitter, currentAccount.userID)
        {
            @Override
            protected void onPostExecute(User user)
            {
                super.onPostExecute(user);
                if(user != null)
                {
                    String urlHttps = user.getProfileImageURLHttps();
                    homeIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    new BitmapURLTask(urlHttps, homeIcon).execute();
                }
            }
        };
        userTask.execute();
        return true;
    }

    private void versionCheck()
    {
        if(isFirstLaunchThisVersion())
        {
            //TODO Show change log
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if(isAuthorized())
        {
            setupAccount();
            startMainLogic();
        }
        else
        {
            startOAuthSession();
        }
        Logger.debug("MainActivity:onCreate");
    }

    private void setTheme()
    {
        ((Application) getApplication()).setThemeIndex(getUserPreferenceHelper().getValue(R.string.key_setting_theme, 0));
        setTheme(Themes.getTheme(getThemeIndex()));
        //        setTheme(Themes.getTheme(0));
    }

    private void setupAccount()
    {
        Account account = Account.load(Account.class, getLastUsedAccountID());
        setCurrentAccount(account);
    }

    private void startOAuthSession()
    {
        oauthSession = new OAuthSession();
        String url = oauthSession.getAuthorizationURL();
        if(!TextUtils.isEmpty(url))
        {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.setData(Uri.parse(url));
            startActivityForResult(intent, REQUEST_OAUTH);
        }
        else
        {
            new Notificator(this, R.string.notice_error_authenticate_request).makeToast().show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MainActivityMenuHelper.addItemsToMenu(this, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(stream != null)
        {
            stream.shutdown();
        }
        Logger.debug("MainActivity:onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return MainActivityMenuHelper.onItemSelected(this, item);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Logger.debug("MainActivity:onPause");
        Notificator.stopNotification();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Logger.debug("MainActivity:onResume");
        Notificator.startNotification();
    }

    // -------------------------- OTHER METHODS --------------------------

    public void finish(boolean needConfirmDialog)
    {
        if(needConfirmDialog)
        {
            ConfirmDialogFragment.show(this, getString(R.string.dialog_confirm_finish_app), new Runnable()
            {

                @Override
                public void run()
                {
                    forceFinish();
                }
            });
        }
        else
        {
            forceFinish();
        }
    }

    private void forceFinish()
    {
        super.finish();
    }

    public void openPostPage()
    {
        setSelectedPageIndex(MainActivity.PAGE_POST);
    }

    /**
     * Open search page
     */
    public void openSearchPage()
    {
        setSelectedPageIndex(PAGE_SEARCH);
    }

    /**
     * Open search page with given query
     */
    public void openSearchPage(final String query)
    {
        SearchQuery.saveIfNotFound(query);
        final SearchListAdapter adapter = (SearchListAdapter) getListAdapter(PAGE_SEARCH);
        adapter.initSearch(query);
        adapter.clear();
        adapter.updateForce();
        new SearchTask(TwitterApi.getTwitter(getCurrentAccount()), query, this)
        {
            @Override
            protected void onPostExecute(QueryResult queryResult)
            {
                super.onPostExecute(queryResult);
                if(queryResult != null)
                {
                    List<twitter4j.Status> tweets = queryResult.getTweets();
                    for(int i = tweets.size() - 1; i >= 0; i--)
                    {
                        twitter4j.Status status = tweets.get(i);
                        if(!status.isRetweet())
                        {
                            StatusViewModel viewModel = new StatusViewModel(status, getCurrentAccount());
                            adapter.addToTop(viewModel);
                            StatusExtractor.filter(MainActivity.this, viewModel);
                        }
                    }
                    adapter.setTopID(queryResult.getMaxId());
                    adapter.updateForce();
                }
            }
        }.execute();
        setSelectedPageIndex(PAGE_SEARCH);
    }

    private void addSearchPages()
    {
        List<SearchQuery> searchQueries = SearchQuery.getAll();
        for(SearchQuery searchQuery : searchQueries)
        {
            openSearchPage(searchQuery.query);
        }
    }

    private void moveToLastPage()
    {
        setSelectedPageIndex(getPageCount() - 1);
    }
}
