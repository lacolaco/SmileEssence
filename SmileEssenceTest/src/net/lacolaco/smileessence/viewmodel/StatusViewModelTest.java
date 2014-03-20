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

public class StatusViewModelTest extends InstrumentationTestCase
{

    TwitterMock mock;

    @Override
    public void setUp() throws Exception
    {
        mock = new TwitterMock(getInstrumentation().getContext());
    }

    public void testID() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotSame(0L, status.id);
    }

    public void testUser() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.user);
    }

    public void testText() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.text);
    }

    public void testCreatedAt() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.createdAt);
    }

    public void testSource() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.source);
    }

    public void testFavorited() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertFalse(status.favorited);
    }

    public void testIsRetweet() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getRetweetMock());
        assertNotNull(status.retweetedStatus);
    }

    public void testMentions() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.mentions);
    }

    public void testHashtags() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.hashtags);
    }

    public void testMedia() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.media);
    }

    public void testURLs() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.urls);
    }
}
