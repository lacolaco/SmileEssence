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

import android.app.ListFragment;
import android.test.ActivityInstrumentationTestCase2;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.util.TwitterMock;
import net.lacolaco.smileessence.view.TestFragment;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.User;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class net.lacolaco.smileessence.activity.MainActivityTest \
 * net.lacolaco.smileessence.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    public MainActivityTest()
    {
        super(MainActivity.class);
    }

    public void testTitle() throws Exception
    {
        assertEquals("SmileEssence", getActivity().getTitle());
    }

    public void testNotification() throws Exception
    {

        final Notificator notificator = new Notificator(getActivity(), "Test Notice");
        assertNotNull(notificator.makeCrouton());
        assertNotNull(notificator.makeToast());
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Notificator.startNotification();
                notificator.publish();
                Notificator.stopNotification();
                notificator.publish();
            }
        });

    }

    public void testGetActionBar() throws Exception
    {
        assertNotNull(getActivity().getActionBar());
    }

    public void testGetViewPager() throws Exception
    {
        assertNotNull(getActivity().getViewPager());
    }

    public void testGetPagerAdapter() throws Exception
    {
        assertNotNull(getActivity().getPagerAdapter());
    }

    public void testAddPageTest() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                assertTrue(getActivity().addPage("test", TestFragment.class, null, true));
            }
        });
    }

    public void testRemovePageTest() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().initializePages();
                assertTrue(getActivity().removePage(0));
            }
        });
    }

    public void testRemoveCurrentPageTest() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().initializePages();
                assertTrue(getActivity().removeCurrentPage());
            }
        });
    }

    public void testStartStream() throws Exception
    {
        TwitterMock mock = new TwitterMock(getInstrumentation().getContext());
        final String token = mock.getAccessToken();
        final String secret = mock.getAccessTokenSecret();
        final User user = mock.getUserMock();
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Account account = new Account(token, secret, user.getId(), user.getScreenName());
                getActivity().setCurrentAccount(account);
                assertTrue(getActivity().startStream());
            }
        });
    }

    public void testStartTwitter() throws Exception
    {
        TwitterMock mock = new TwitterMock(getInstrumentation().getContext());
        final String token = mock.getAccessToken();
        final String secret = mock.getAccessTokenSecret();
        final User user = mock.getUserMock();
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Account account = new Account(token, secret, user.getId(), user.getScreenName());
                getActivity().setCurrentAccount(account);
                assertTrue(getActivity().startTwitter());
            }
        });
        Thread.sleep(3000);
        assertNotSame(0, getActivity().getListAdapter(MainActivity.PAGE_HOME).getCount());
    }

    public void testRegisterListPage() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                CustomListAdapter<StatusViewModel> adapter = new CustomListAdapter<>(getActivity(), StatusViewModel.class);
                assertTrue(getActivity().addListPage("", ListFragment.class, adapter, true));
                assertEquals(adapter, getActivity().getListAdapter(getActivity().getPageCount() - 1));
            }
        });
    }

    public void testInitializeTimelines() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().initializePages();
                assertEquals(getActivity().getString(R.string.page_name_post), getActivity().getPageInfo(0).getName());
                assertEquals(getActivity().getString(R.string.page_name_home), getActivity().getPageInfo(1).getName());
                assertEquals(getActivity().getString(R.string.page_name_mentions), getActivity().getPageInfo(2).getName());
                assertEquals(getActivity().getString(R.string.page_name_messages), getActivity().getPageInfo(3).getName());
                assertEquals(getActivity().getString(R.string.page_name_history), getActivity().getPageInfo(4).getName());
                assertNull(getActivity().getListAdapter(0));
                assertNotNull(getActivity().getListAdapter(1));
                assertNotNull(getActivity().getListAdapter(2));
                assertNotNull(getActivity().getListAdapter(3));
                assertNotNull(getActivity().getListAdapter(4));
            }
        });
    }

    public void testMovePage() throws Exception
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().initializePages();
            }
        });
        Thread.sleep(1000);
        assertEquals(MainActivity.PAGE_HOME, getActivity().getCurrentPageIndex());
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getActivity().setSelectedPageIndex(MainActivity.PAGE_POST);
            }
        });
        Thread.sleep(1000);
        assertEquals(MainActivity.PAGE_POST, getActivity().getCurrentPageIndex());
    }
}
