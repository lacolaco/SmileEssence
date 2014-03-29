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
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.preference.AppPreferenceHelper;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.twitter.OAuthSession;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.UserStreamListener;
import net.lacolaco.smileessence.util.NetworkHelper;
import net.lacolaco.smileessence.view.CustomListFragment;
import net.lacolaco.smileessence.view.PostFragment;
import net.lacolaco.smileessence.view.adapter.*;
import net.lacolaco.smileessence.viewmodel.menu.MainActivityMenuFactory;
import twitter4j.TwitterStream;
import twitter4j.auth.AccessToken;

import java.io.IOException;

public class MainActivity extends Activity
{

    public static final int REQUEST_OAUTH = 10;
    public static final int PAGE_POST = 0;
    public static final int PAGE_HOME = 1;
    public static final int PAGE_MENTIONS = 2;
    public static final int PAGE_MESSAGES = 3;
    public static final int PAGE_HISTORY = 4;
    private UserPreferenceHelper userPref;
    private AppPreferenceHelper appPref;
    private ViewPager viewPager;
    private PageListAdapter pagerAdapter;
    private OAuthSession oauthSession;
    private Account currentAccount;
    private TwitterStream stream;
    private SparseArray<CustomListAdapter<?>> adapterSparseArray = new SparseArray<>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        try
        {
            setupHelpers();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            finish();
        }
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeView();
        //TODO account check
        initializePages();
        Logger.debug("MainActivity:onCreate");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Logger.debug("MainActivity:onResume");
        //        //account check
        //        long id = getLastUsedAccountID();
        //        if(id < 0)
        //        {
        //            oauthSession = new OAuthSession();
        //            String url = oauthSession.getAuthorizationURL();
        //            if(!TextUtils.isEmpty(url))
        //            {
        //                Intent intent = new Intent(this, WebViewActivity.class);
        //                intent.setData(Uri.parse(url));
        //                startActivityForResult(intent, REQUEST_OAUTH);
        //            }
        //            else
        //            {
        //                //TODO Notify auth error
        //            }
        //        }
        //        else
        //        {
        //            //Login and initialize
        //            Account account = Account.load(Account.class, id);
        //            setCurrentAccount(account);
        //            if(isFirstLaunchThisVersion())
        //            {
        //                //TODO Show change log
        //            }
        //            initializeTimelines();
        //        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case REQUEST_OAUTH:
            {
                if(resultCode != RESULT_OK)
                {
                    //TODO Notify error
                    finish();
                }
                else
                {
                    AccessToken token = oauthSession.getAccessToken(data.getData());
                    Account account = new Account(token.getToken(), token.getTokenSecret(), token.getUserId(), token.getScreenName());
                    account.save();
                    setCurrentAccount(account);
                }
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Logger.debug("MainActivity:onPause");
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
    protected void onStop()
    {
        super.onStop();
        Logger.debug("MainActivity:onStop");
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MainActivityMenuFactory factory = new MainActivityMenuFactory(this.getBaseContext());
        factory.addItemsToMenu(menu);
        return true;
    }

    private void setupHelpers() throws IOException
    {
        userPref = new UserPreferenceHelper(this);
        appPref = new AppPreferenceHelper(this);
    }

    private void setTheme()
    {
        //TODO on release switch(userPref.getValue(R.string.key_setting_theme, 0))
        switch(1)
        {
            case 0:
            {
                setTheme(R.style.theme_dark);
                Logger.debug("Theme:Dark");
                break;
            }
            case 1:
            {
                setTheme(R.style.theme_light);
                Logger.debug("Theme:Light");
                break;
            }
            default:
            {
                setTheme(R.style.theme_dark);
                Logger.debug("Theme:Default");
                break;
            }
        }
    }


    private void initializeView()
    {
        ActionBar bar = getActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new PageListAdapter(this, viewPager);
    }

    private long getLastUsedAccountID()
    {
        String id = appPref.getValue("lastUsedAccountID", "");
        if(TextUtils.isEmpty(id))
        {
            return -1;
        }
        else
        {
            return Long.getLong(id);
        }
    }

    private String getVersion()
    {
        return getString(R.string.app_version);
    }

    private boolean isFirstLaunchThisVersion()
    {
        return !getVersion().contentEquals(appPref.getValue("app.version", ""));
    }

    public UserPreferenceHelper getUserPref()
    {
        return userPref;
    }

    public AppPreferenceHelper getAppPref()
    {
        return appPref;
    }

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    public PageListAdapter getPagerAdapter()
    {
        return pagerAdapter;
    }

    public Account getCurrentAccount()
    {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount)
    {
        this.currentAccount = currentAccount;
    }

    /**
     * Add page.
     *
     * @see PageListAdapter#addPage(String, Class, android.os.Bundle)
     */
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

    /**
     * Remove page.
     *
     * @see PageListAdapter#removePage(int)
     */
    public boolean removePage(int position)
    {
        return this.pagerAdapter.removePage(position);
    }

    /**
     * Remove current page.
     *
     * @return successfully
     */
    public boolean removeCurrentPage()
    {
        return this.pagerAdapter.removePage(getCurrentPageIndex());
    }

    /**
     * Get current page index.
     *
     * @return page index
     */
    public int getCurrentPageIndex()
    {
        return this.viewPager.getCurrentItem();
    }

    /**
     * Get PageInfo at position.
     *
     * @param position page position
     * @return PageInfo
     */
    public PageListAdapter.PageInfo getPageInfo(int position)
    {
        return pagerAdapter.getPage(position);
    }

    public int getPageCount()
    {
        return pagerAdapter.getCount();
    }

    public boolean addPostPage()
    {
        return addPage(getString(R.string.page_name_post), PostFragment.class, null, true);
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

    public CustomListAdapter<?> getListAdapter(int i)
    {
        return adapterSparseArray.get(i);
    }

    public boolean startTwitter()
    {
        if(!startStream())
        {
            return false;
        }

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

    /**
     * Initialize basic pages.
     */
    public void initializePages()
    {
        addPostPage();
        StatusListAdapter homeAdapter = new StatusListAdapter(this);
        StatusListAdapter mentionsAdapter = new StatusListAdapter(this);
        MessageListAdapter messagesAdapter = new MessageListAdapter(this);
        EventListAdapter historyAdapter = new EventListAdapter(this);
        addListPage(getString(R.string.page_name_home), CustomListFragment.class, homeAdapter, false);
        addListPage(getString(R.string.page_name_mentions), CustomListFragment.class, mentionsAdapter, false);
        addListPage(getString(R.string.page_name_messages), CustomListFragment.class, messagesAdapter, false);
        addListPage(getString(R.string.page_name_history), CustomListFragment.class, historyAdapter, false);
        pagerAdapter.notifyDataSetChanged();
        PostState.newState();
    }
}
