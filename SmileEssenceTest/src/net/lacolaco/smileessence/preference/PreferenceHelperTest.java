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

package net.lacolaco.smileessence.preference;

import android.test.AndroidTestCase;
import net.lacolaco.smileessence.R;

public class PreferenceHelperTest extends AndroidTestCase
{

    private Pref pref;

    @Override
    public void setUp() throws Exception
    {
        pref = new Pref(getContext());
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
