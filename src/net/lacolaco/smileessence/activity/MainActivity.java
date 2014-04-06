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
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.preference.AppPreferenceHelper;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.twitter.OAuthSession;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.UserStreamListener;
import net.lacolaco.smileessence.twitter.task.DirectMessagesTask;
import net.lacolaco.smileessence.twitter.task.HomeTimelineTask;
import net.lacolaco.smileessence.twitter.task.MentionsTimelineTask;
import net.lacolaco.smileessence.twitter.task.ShowUserTask;
import net.lacolaco.smileessence.util.BitmapURLTask;
import net.lacolaco.smileessence.util.NetworkHelper;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.CustomListFragment;
import net.lacolaco.smileessence.view.PostFragment;
import net.lacolaco.smileessence.view.adapter.*;
import net.lacolaco.smileessence.viewmodel.MessageViewModel;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import net.lacolaco.smileessence.viewmodel.menu.MainActivityMenuHelper;
import twitter4j.*;
import twitter4j.auth.AccessToken;

public class MainActivity extends Activity
{

    // ------------------------------ FIELDS ------------------------------

    public static final int REQUEST_OAUTH = 10;
    public static final int PAGE_POST = 0;
    public static final int PAGE_HOME = 1;
    public static final int PAGE_MENTIONS = 2;
    public static final int PAGE_MESSAGES = 3;
    public static final int PAGE_HISTORY = 4;
    private final String lastUsedAccountIDKey = "lastUsedAccountID";
    private UserPreferenceHelper userPref;
    private AppPreferenceHelper appPref;
    private ViewPager viewPager;
    private PageListAdapter pagerAdapter;
    private OAuthSession oauthSession;
    private Account currentAccount;
    private TwitterStream stream;
    private SparseArray<CustomListAdapter<?>> adapterSparseArray = new SparseArray<>();
    private boolean streaming = false;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Account getCurrentAccount()
    {
        return currentAccount;
    }

    public PageListAdapter getPagerAdapter()
    {
        return pagerAdapter;
    }

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    public boolean isStreaming()
    {
        return streaming;
    }

    public void setStreaming(boolean streaming)
    {
        this.streaming = streaming;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case REQUEST_OAUTH:
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
                    startMainLogic();
                }
            }
        }
    }

    public void setCurrentAccount(Account account)
    {
        this.currentAccount = account;
        appPref.putValue(lastUsedAccountIDKey, account.getId());
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
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new PageListAdapter(this, viewPager);
        initializePages();
    }

    private void initializePages()
    {
        addPage(getString(R.string.page_name_post), PostFragment.class, null, true);
        StatusListAdapter homeAdapter = new StatusListAdapter(this);
        StatusListAdapter mentionsAdapter = new StatusListAdapter(this);
        MessageListAdapter messagesAdapter = new MessageListAdapter(this);
        EventListAdapter historyAdapter = new EventListAdapter(this);
        addListPage(getString(R.string.page_name_home), CustomListFragment.class, homeAdapter, false);
        addListPage(getString(R.string.page_name_mentions), CustomListFragment.class, mentionsAdapter, false);
        addListPage(getString(R.string.page_name_messages), CustomListFragment.class, messagesAdapter, false);
        addListPage(getString(R.string.page_name_history), CustomListFragment.class, historyAdapter, false);
        pagerAdapter.notifyDataSetChanged();
        PostState.newState().beginTransaction().commit();
        setSelectedPageIndex(PAGE_HOME);
    }

    public boolean addListPage(String name, Class<? extends ListFragment> fragmentClass, CustomListAdapter<?> adapter, boolean withNotify)
    {
        int nextPosition = pagerAdapter.getCount();
        Bundle args = new Bundle();
        args.putInt(CustomListFragment.FRAGMENT_INDEX, nextPosition);
        if(addPage(name, fragmentClass, args, withNotify))
        {
            adapterSparseArray.append(nextPosition, adapter);
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

    public void setSelectedPageIndex(int position)
    {
        getActionBar().setSelectedNavigationItem(position);
    }

    public boolean startTwitter()
    {
        if(!startStream())
        {
            return false;
        }
        int count = userPref.getValue(R.string.key_setting_timelines, 20);
        Twitter twitter = new TwitterApi(currentAccount).getTwitter();
        Paging paging = new Paging().count(count);
        HomeTimelineTask homeTask = new HomeTimelineTask(twitter, this, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(twitter4j.Status status : statuses)
                {
                    getListAdapter(PAGE_HOME).addToBottom(new StatusViewModel(status));
                }
            }
        };
        MentionsTimelineTask mentionsTask = new MentionsTimelineTask(twitter, this, paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(twitter4j.Status status : statuses)
                {
                    getListAdapter(PAGE_MENTIONS).addToBottom(new StatusViewModel(status));
                }
            }
        };
        DirectMessagesTask messagesTask = new DirectMessagesTask(twitter, this, paging)
        {
            @Override
            protected void onPostExecute(DirectMessage[] directMessages)
            {
                super.onPostExecute(directMessages);
                for(DirectMessage message : directMessages)
                {
                    getListAdapter(PAGE_MESSAGES).addToBottom(new MessageViewModel(message));
                }
            }
        };
        homeTask.execute();
        mentionsTask.execute();
        messagesTask.execute();
        updateActionBarIcon();
        return true;
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
        final ImageView homeIcon = (ImageView)findViewById(android.R.id.home);
        ShowUserTask userTask = new ShowUserTask(twitter, currentAccount.userID)
        {
            @Override
            protected void onPostExecute(User user)
            {
                super.onPostExecute(user);
                if(user != null)
                {
                    String urlHttps = user.getProfileImageURLHttps();
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

    private boolean isFirstLaunchThisVersion()
    {
        return !getVersion().contentEquals(appPref.getValue("app.version", ""));
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setupHelpers();
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

    private boolean isAuthorized()
    {
        return getLastUsedAccountID() >= 0;
    }

    private long getLastUsedAccountID()
    {
        String id = appPref.getValue(lastUsedAccountIDKey, "");
        if(TextUtils.isEmpty(id))
        {
            return -1;
        }
        else
        {
            return Long.parseLong(id);
        }
    }

    private void setTheme()
    {
        //TODO on release userPref.getValue(R.string.key_setting_theme, 0)
        setTheme(Themes.getTheme(1));
    }

    private void setupAccount()
    {
        Account account = Account.load(Account.class, getLastUsedAccountID());
        setCurrentAccount(account);
    }

    private void setupHelpers()
    {
        userPref = new UserPreferenceHelper(this);
        appPref = new AppPreferenceHelper(this);
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

    public CustomListAdapter<?> getListAdapter(int i)
    {
        return adapterSparseArray.get(i);
    }

    public int getPageCount()
    {
        return pagerAdapter.getCount();
    }

    public String getVersion()
    {
        return getString(R.string.app_version);
    }

    public boolean removeCurrentPage()
    {
        return this.pagerAdapter.removePage(getCurrentPageIndex());
    }

    public int getCurrentPageIndex()
    {
        return this.viewPager.getCurrentItem();
    }

    public boolean removePage(int position)
    {
        return this.pagerAdapter.removePage(position);
    }
}
