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

import android.test.ActivityInstrumentationTestCase2;
import net.lacolaco.smileessence.notification.Notificator;

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

    public void testIsFirstLaunchByVersion() throws Exception
    {
        assertTrue(getActivity().IsFirstLaunchByVersion());
    }

    public void testGetResourceHelper() throws Exception
    {
        assertNotNull(getActivity().getResourceHelper());
    }

    public void testGetPreferenceHelper() throws Exception
    {
        assertNotNull(getActivity().getPreferenceHelper());
    }

    public void testGetPropertyHelper() throws Exception
    {
        assertNotNull(getActivity().getPropertyHelper());
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
}
