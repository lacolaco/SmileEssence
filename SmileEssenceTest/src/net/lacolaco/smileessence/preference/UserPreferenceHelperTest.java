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

package net.lacolaco.smileessence.preference;

import android.test.AndroidTestCase;
import net.lacolaco.smileessence.R;

public class UserPreferenceHelperTest extends AndroidTestCase
{

    private UserPreferenceHelper pref;

    @Override
    public void setUp() throws Exception
    {
        pref = new UserPreferenceHelper(getContext());
    }

    public void testNotFound() throws Exception
    {
        assertEquals("", pref.getValue(0, ""));
    }

    public void testUseDefault() throws Exception
    {
        assertNotSame(0, pref.getValue(R.string.key_text_size, 10));
    }

    public void testPutAndGet() throws Exception
    {
        assertEquals(true, pref.putValue(R.string.key_test_preference, 10));
        assertEquals(10, pref.getValue(R.string.key_test_preference, 0));
    }
}
