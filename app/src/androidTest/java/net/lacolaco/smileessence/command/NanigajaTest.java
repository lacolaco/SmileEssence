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

package net.lacolaco.smileessence.command;

import android.content.Context;
import android.content.res.Configuration;
import android.test.ActivityInstrumentationTestCase2;

import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.status.StatusCommandNanigaja;
import net.lacolaco.smileessence.util.TwitterMock;

import java.util.Locale;

import twitter4j.Status;

public class NanigajaTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private TwitterMock mock;

    public NanigajaTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        this.mock = new TwitterMock(getInstrumentation().getContext());
        Context context = getActivity();
        Configuration config = context.getResources().getConfiguration();
        config.locale = Locale.JAPANESE;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public void testBuildNormal() throws Exception {
        Status status = mock.getStatusMock();
        StatusCommandNanigaja nanigaja = new StatusCommandNanigaja(getActivity(), status, mock.getTwitterMock(), mock.getAccount());
        assertEquals("な～にが" + status.getText() + "じゃ", nanigaja.build());
    }

    public void testBuildReply() throws Exception {
        Status status = mock.getReplyMock();
        StatusCommandNanigaja nanigaja = new StatusCommandNanigaja(getActivity(), status, mock.getTwitterMock(), mock.getAccount());
        assertTrue(nanigaja.build().startsWith("@" + status.getUser().getScreenName() + " な～にが"));
    }

    public void testBuildRetweet() throws Exception {
        Status status = mock.getRetweetMock();
        StatusCommandNanigaja nanigaja = new StatusCommandNanigaja(getActivity(), status, mock.getTwitterMock(), mock.getAccount());
        assertTrue(nanigaja.build().startsWith("@" + status.getRetweetedStatus().getUser().getScreenName() + " な～にが"));
    }

    @Override
    public void tearDown() throws Exception {
        getActivity().forceFinish();
    }
}
