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

package net.lacolaco.smileessence.activity;

import android.app.Fragment;
import android.preference.Preference;
import android.test.ActivityInstrumentationTestCase2;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.view.SettingFragment;

public class SettingActivityTest extends ActivityInstrumentationTestCase2<SettingActivity>
{

    private Fragment fragment;

    public SettingActivityTest()
    {
        super(SettingActivity.class);
    }

    @Override
    public void setUp() throws Exception
    {
        fragment = getActivity().getFragmentManager().findFragmentById(R.id.fragment_setting);
    }

    public void testGetValues() throws Exception
    {
        assertNotNull(fragment);
        assertTrue(fragment instanceof SettingFragment);
    }

    public void testGetPreference() throws Exception
    {
        SettingFragment settingFragment = (SettingFragment)fragment;
        Preference textSize = settingFragment.findPreference(R.string.key_setting_text_size);
        assertNotNull(textSize);
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(getActivity());
        assertEquals("10", textSize.getSummary());
        assertEquals("Dark", settingFragment.findPreference(R.string.key_setting_theme).getSummary());
        assertEquals("ScreenName / Name", settingFragment.findPreference(R.string.key_setting_namestyle).getSummary());
        assertEquals(String.format(getActivity().getString(R.string.setting_timelines_summary_format), 20), settingFragment.findPreference(R.string.key_setting_timelines).getSummary());
    }
}
