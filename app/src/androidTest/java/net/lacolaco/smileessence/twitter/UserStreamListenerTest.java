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

package net.lacolaco.smileessence.twitter;

import android.test.ActivityInstrumentationTestCase2;

import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.util.TwitterMock;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;

public class UserStreamListenerTest extends ActivityInstrumentationTestCase2<MainActivity> {

    TwitterMock mock;
    UserStreamListener listener;
    private String token;
    private User user;
    private String secret;

    public UserStreamListenerTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        mock = new TwitterMock(getInstrumentation().getContext());
        listener = new UserStreamListener(getActivity());
        token = mock.getAccessToken();
        secret = mock.getAccessTokenSecret();
        user = mock.getUserMock();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Account account = new Account(token, secret, user.getId(), user.getScreenName());
                getActivity().setCurrentAccount(account);
                getActivity().initializeView();
            }
        });
        Thread.sleep(500);
    }

    public void testOnStatus() throws Exception {
        final Status status = mock.getStatusMock();
        CustomListAdapter<?> home = getActivity().getListAdapter(MainActivity.ADAPTER_HOME);
        listener.onStatus(status);
        home.updateForce();
        Thread.sleep(500);
        assertEquals(1, home.getCount());
        listener.onDeletionNotice(new StatusDeletionNotice() {
            @Override
            public long getStatusId() {
                return status.getId();
            }

            @Override
            public long getUserId() {
                return status.getUser().getId();
            }

            @Override
            public int compareTo(StatusDeletionNotice another) {
                return 0;
            }
        });
        home.updateForce();
        Thread.sleep(500);
        assertEquals(0, home.getCount());
    }

    public void testOnMention() throws Exception {
        final Status status = mock.getReplyMock();
        CustomListAdapter<?> mentions = getActivity().getListAdapter(MainActivity.ADAPTER_MENTIONS);
        listener.onStatus(status);
        mentions.updateForce();
        Thread.sleep(500);
        assertEquals(1, mentions.getCount());
    }

    public void testOnRetweeted() throws Exception {
        final Status status = mock.getRetweetMock();
        listener.onStatus(status);
        CustomListAdapter<?> home = getActivity().getListAdapter(MainActivity.ADAPTER_HOME);
        home.updateForce();
        Thread.sleep(500);
        assertEquals(1, home.getCount());
    }

    public void testOnFavorited() throws Exception {
        final Status status = mock.getReplyMock();
        final User source = status.getUser();
        CustomListAdapter<?> history = getActivity().getListAdapter(MainActivity.ADAPTER_HISTORY);
        listener.onFavorite(source, user, status);
        history.updateForce();
        Thread.sleep(500);
        assertEquals(1, history.getCount());
        listener.onUnfavorite(source, user, status);
        history.updateForce();
        Thread.sleep(500);
        assertEquals(2, history.getCount());
    }

    public void testOnFollow() throws Exception {
        final User source = mock.getUserMock();
        CustomListAdapter<?> history = getActivity().getListAdapter(MainActivity.ADAPTER_HISTORY);
        listener.onFollow(source, user);
        history.updateForce();
        Thread.sleep(500);
        assertEquals(1, history.getCount());
    }

    public void testOnBlock() throws Exception {
        final User source = mock.getUserMock();
        CustomListAdapter<?> history = getActivity().getListAdapter(MainActivity.ADAPTER_HISTORY);
        listener.onBlock(source, user);
        listener.onUnblock(source, user);
        history.updateForce();
        Thread.sleep(500);
        assertEquals(2, history.getCount());
    }

    public void testOnDirectMessage() throws Exception {
        final DirectMessage message = mock.getDirectMessageMock();
        CustomListAdapter<?> messages = getActivity().getListAdapter(MainActivity.ADAPTER_MESSAGES);
        listener.onDirectMessage(message);
        messages.updateForce();
        Thread.sleep(500);
        assertEquals(1, messages.getCount());
        listener.onDeletionNotice(message.getId(), message.getSenderId());
        messages.updateForce();
        Thread.sleep(500);
        assertEquals(0, messages.getCount());
    }

    @Override
    protected void tearDown() throws Exception {
        getActivity().forceFinish();
    }
}
