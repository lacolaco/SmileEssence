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

import net.lacolaco.smileessence.util.TwitterMock;

public class UserViewModelTest extends InstrumentationTestCase {

    UserViewModel user;

    @Override
    public void setUp() throws Exception {
        user = new UserViewModel(new TwitterMock(getInstrumentation().getContext()).getUserMock());
    }

    public void testID() throws Exception {
        assertNotSame(0L, user.getID());
    }

    public void testScreenName() throws Exception {
        assertNotNull(user.getScreenName());
    }

    public void testName() throws Exception {
        assertNotNull(user.getName());
    }

    public void testDescription() throws Exception {
        assertNotNull(user.getDescription());
    }

    public void testLocation() throws Exception {
        assertNotNull(user.getLocation());
    }

    public void testURL() throws Exception {
        assertNotNull(user.getURL());
    }

    public void testIconURL() throws Exception {
        assertNotNull(user.getIconURL());
    }

    public void testBannerURL() throws Exception {
        assertNotNull(user.getBannerURL());
    }

    public void testStatusCount() throws Exception {
        assertNotSame(0, user.getStatusesCount());
    }

    public void testFriendCount() throws Exception {
        assertNotSame(0, user.getFriendsCount());
    }

    public void testFollowerCount() throws Exception {
        assertNotSame(0, user.getFollowersCount());
    }

    public void testFavoriteCount() throws Exception {
        assertNotSame(0, user.getFavoritesCount());
    }

    public void testProtected() throws Exception {
        assertFalse(user.isProtected());
    }

    public void testMarked() throws Exception {
        assertFalse(user.isVerified());
    }
}

