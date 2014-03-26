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

import android.test.InstrumentationTestCase;
import net.lacolaco.smileessence.util.TwitterMock;

public class TwitterApiTest extends InstrumentationTestCase
{

    private static final String CONSUMER_KEY = "SIt6h4O6qmBB2URSKsF1Q";
    private static final String CONSUMER_SECRET = "Uil1dyrqiodLLqXIB6B0rVwVxFfFCxTf8ggAcszWc";
    private static final String SCREEN_NAME = "laco0416";
    private static final long USER_ID = 498602690;
    private TwitterApi api;

    @Override
    public void setUp() throws Exception
    {
        TwitterMock mock = new TwitterMock(getInstrumentation().getContext());
        api = new TwitterApi(mock.getAccessToken(), mock.getAccessTokenSecret());
    }

    public void testReadProperties() throws Exception
    {
        assertEquals(CONSUMER_KEY, api.getTwitter().getConfiguration().getOAuthConsumerKey());
        assertEquals(CONSUMER_SECRET, api.getTwitter().getConfiguration().getOAuthConsumerSecret());
    }

    public void testAuthenticate() throws Exception
    {
        assertEquals(SCREEN_NAME, api.getTwitter().getScreenName());
    }

    public void testAccessApi() throws Exception
    {
        assertNotNull(api.getTwitter().users().showUser(USER_ID));
    }
}
