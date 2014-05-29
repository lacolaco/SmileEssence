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

import junit.framework.Assert;
import junit.framework.TestCase;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;

public class TwitterUtilsTest extends TestCase
{

    public void testLength() throws Exception
    {
        String text = "test";
        Assert.assertEquals(text.length(), TwitterUtils.getFixedTextLength(text));
        text = "test.com";
        assertNotSame(text.length(), TwitterUtils.getFixedTextLength(text));
    }

    public void testURL() throws Exception
    {
        String screenName = "laco0416";

        assertEquals("https://twitter.com/laco0416", TwitterUtils.getUserHomeURL(screenName));
        assertEquals("http://favstar.fm/users/laco0416/recent", TwitterUtils.getFavstarRecentURL(screenName));
        assertEquals("http://aclog.koba789.com/laco0416/timeline", TwitterUtils.getAclogTimelineURL(screenName));
        assertEquals("http://twilog.org/laco0416", TwitterUtils.getTwilogURL(screenName));
    }
}
