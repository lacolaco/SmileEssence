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
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotSame(0L, status.getID());
    }

    public void testUserID() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotSame(0L, status.getUserID());
    }

    public void testScreenName() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getScreenName());
    }

    public void testName() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getName());
    }

    public void testIconURL() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getIconURL());
    }

    public void testText() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getText());
    }

    public void testCreatedAt() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getCreatedAt());
    }

    public void testSource() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getSource());
    }

    public void testFavorited() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertFalse(status.isFavorited());
    }

    public void testProtected() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertFalse(status.isProtected());
    }

    public void testIsRetweet() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getRetweetMock(), );
        assertNotNull(status.getRetweetedStatus());
    }

    public void testMentions() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getMentions());
    }

    public void testHashtags() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getHashtags());
    }

    public void testMedia() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getMedia());
    }

    public void testURLs() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getURLs());
    }

    public void testSymbol() throws Exception
    {
        StatusViewModel status = new StatusViewModel(mock.getStatusMock(), );
        assertNotNull(status.getSymbols());
    }
}
