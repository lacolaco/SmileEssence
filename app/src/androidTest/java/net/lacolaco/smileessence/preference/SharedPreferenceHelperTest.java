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

import android.test.InstrumentationTestCase;

public class SharedPreferenceHelperTest extends InstrumentationTestCase {

    SharedPreferenceHelper helper;

    @Override
    public void setUp() throws Exception {
        //can't create on test context.
        helper = new SharedPreferenceHelper(getInstrumentation().getTargetContext(), "TestPreference");
        assertTrue(helper.putValue("test.sample", "test"));
        assertTrue(helper.putValue("test.empty", ""));
    }

    public void testGetProperty() throws Exception {
        String sample = helper.getValue("test.sample", "");
        assertEquals("test", sample);
    }

    public void testSetProperty() throws Exception {
        assertTrue(helper.putValue("test.sample", "test1"));
        assertEquals("test1", helper.getValue("test.sample", ""));
    }

    public void testGetEmptyValue() throws Exception {
        String empty = helper.getValue("test.empty", "");
        assertEquals("", empty);
    }

    public void testNotExists() throws Exception {
        String notExists = helper.getValue("test.null", "");
        assertEquals("", notExists);
    }
}
