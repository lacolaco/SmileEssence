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
        assertNotSame(0L, status.getID());
    }

    public void testUserID() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotSame(0L, status.getUserID());
    }

    public void testScreenName() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getScreenName());
    }

    public void testName() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getName());
    }

    public void testIconURL() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getIconURL());
    }

    public void testText() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getText());
    }

    public void testCreatedAt() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getCreatedAt());
    }

    public void testSource() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getSource());
    }

    public void testFavorited() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertFalse(status.isFavorited());
    }

    public void testProtected() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertFalse(status.isProtected());
    }

    public void testIsRetweet() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getRetweetMock());
        assertNotNull(status.getRetweetedStatus());
    }

    public void testMentions() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getMentions());
    }

    public void testHashtags() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getHashtags());
    }

    public void testMedia() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getMedia());
    }

    public void testURLs() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getURLs());
    }

    public void testSymbol() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock());
        assertNotNull(status.getSymbols());
    }
}
