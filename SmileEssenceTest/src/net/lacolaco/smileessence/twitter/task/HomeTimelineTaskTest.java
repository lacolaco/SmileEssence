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

package net.lacolaco.smileessence.twitter.task;

import android.test.InstrumentationTestCase;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.util.TwitterMock;
import twitter4j.Paging;
import twitter4j.Twitter;

public class HomeTimelineTaskTest extends InstrumentationTestCase
{

    Twitter twitter;

    @Override
    public void setUp() throws Exception
    {
        TwitterMock mock = new TwitterMock(getInstrumentation().getContext());
        twitter = new TwitterApi(mock.getAccessToken(), mock.getAccessTokenSecret()).getTwitter();
    }

    public void testGetDefaultHome() throws Exception
    {
        HomeTimelineTask task = new HomeTimelineTask(twitter);
        task.execute();
        assertNotNull(task.get());
    }

    public void testGetCountedHome() throws Exception
    {
        Paging paging = new Paging(1).count(50);
        HomeTimelineTask task = new HomeTimelineTask(twitter, paging);
        task.execute();
        assertEquals(50, task.get().length);
    }
}
