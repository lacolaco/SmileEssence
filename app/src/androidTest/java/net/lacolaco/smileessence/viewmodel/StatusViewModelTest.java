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

package net.lacolaco.smileessence.viewmodel;

import android.test.InstrumentationTestCase;

import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.util.TwitterMock;

public class StatusViewModelTest extends InstrumentationTestCase {

    TwitterMock mock;
    Account account;

    @Override
    public void setUp() throws Exception {
        mock = new TwitterMock(getInstrumentation().getContext());
        account = new Account(mock.getAccessToken(), mock.getAccessTokenSecret(), mock.getUserMock().getId(), mock.getUserMock().getScreenName());
    }

    public void testID() throws Exception {
        StatusViewModel status = new StatusViewModel(mock.getReplyMock(), account);
        assertNotSame(0L, status.getID());
    }

    public void testMention() throws Exception {
        StatusViewModel status = new StatusViewModel(mock.getReplyMock(), account);
        assertTrue(status.isMention(account.screenName));
    }

    public void testMyStatus() throws Exception {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), account);
        assertTrue(status.isMyStatus(account.userID));
    }
}
