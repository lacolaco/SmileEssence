/*
 * SmileEssence Lite
 * Copyright 2014 laco0416
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License
 */

package net.lacolaco.smileessence.viewmodel;

import android.test.InstrumentationTestCase;
import net.lacolaco.smileessence.util.TwitterMock;

public class UserViewModelTest extends InstrumentationTestCase
{

    UserViewModel user;

    @Override
    public void setUp() throws Exception
    {
        user = new UserViewModel(new TwitterMock(getInstrumentation().getContext()).getUserMock());
    }

    public void testID() throws Exception
    {
        assertNotSame(0L, user.getID());
    }

    public void testScreenName() throws Exception
    {
        assertNotNull(user.getScreenName());
    }

    public void testName() throws Exception
    {
        assertNotNull(user.getName());
    }

    public void testDescription() throws Exception
    {
        assertNotNull(user.getDescription());
    }

    public void testLocation() throws Exception
    {
        assertNotNull(user.getLocation());
    }

    public void testURL() throws Exception
    {
        assertNotNull(user.getURL());
    }

    public void testIconURL() throws Exception
    {
        assertNotNull(user.getIconURL());
    }

    public void testBannerURL() throws Exception
    {
        assertNotNull(user.getBannerURL());
    }

    public void testStatusCount() throws Exception
    {
        assertNotSame(0, user.getStatusesCount());
    }

    public void testFriendCount() throws Exception
    {
        assertNotSame(0, user.getFriendsCount());
    }

    public void testFollowerCount() throws Exception
    {
        assertNotSame(0, user.getFollowersCount());
    }

    public void testFavoriteCount() throws Exception
    {
        assertNotSame(0, user.getFavoritesCount());
    }

    public void testProtected() throws Exception
    {
        assertFalse(user.isProtected());
    }

    public void testMarked() throws Exception
    {
        assertFalse(user.isVerified());
    }
}

