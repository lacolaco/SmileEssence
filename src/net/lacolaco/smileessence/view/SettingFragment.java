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

package net.lacolaco.smileessence.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.SimpleDialogFragment;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SettingFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        EditTextPreference textSizePreference = (EditTextPreference) findPreference(R.string.key_setting_text_size);
        textSizePreference.setSummary(textSizePreference.getText());
        textSizePreference.setOnPreferenceChangeListener(this);
        ListPreference themePreference = (ListPreference) findPreference(R.string.key_setting_theme);
        themePreference.setSummary(themePreference.getEntry());
        ListPreference namestylePreference = (ListPreference) findPreference(R.string.key_setting_namestyle);
        namestylePreference.setSummary(namestylePreference.getEntry());
        EditTextPreference timelinesPreference = (EditTextPreference) findPreference(R.string.key_setting_timelines);
        timelinesPreference.setSummary(String.format(getString(R.string.setting_timelines_summary_format), timelinesPreference.getText()));
        timelinesPreference.setOnPreferenceChangeListener(this);
        Preference appInfoPreference = findPreference(R.string.key_setting_application_information);
        appInfoPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public Preference findPreference(int preferenceResID)
    {
        return findPreference(getString(preferenceResID));
    }

    private void setSummaryCurrentValue()
    {
        EditTextPreference textSizePreference = (EditTextPreference) findPreference(R.string.key_setting_text_size);
        textSizePreference.setSummary(textSizePreference.getText());
        ListPreference themePreference = (ListPreference) findPreference(R.string.key_setting_theme);
        themePreference.setSummary(themePreference.getEntry());
        ListPreference namestylePreference = (ListPreference) findPreference(R.string.key_setting_namestyle);
        namestylePreference.setSummary(namestylePreference.getEntry());
        EditTextPreference timelinesPreference = (EditTextPreference) findPreference(R.string.key_setting_timelines);
        timelinesPreference.setSummary(String.format(getString(R.string.setting_timelines_summary_format), timelinesPreference.getText()));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        setSummaryCurrentValue();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        String newValueStr = String.valueOf(newValue);
        if(preference.getKey().contentEquals(getString(R.string.key_setting_text_size)))
        {
            if(TextUtils.isDigitsOnly(newValueStr))
            {
                int newTextSize = Integer.parseInt(newValueStr);
                if(UserPreferenceHelper.TEXT_SIZE_MIN <= newTextSize && newTextSize <= UserPreferenceHelper.TEXT_SIZE_MAX)
                {
                    return true;
                }
                new Notificator(getActivity(), getString(R.string.error_setting_text_size_range)).publish();
            }
            else
            {
                new Notificator(getActivity(), getString(R.string.error_setting_text_size_not_number)).publish();
            }
            return false;
        }
        else if(preference.getKey().contentEquals(getString(R.string.key_setting_timelines)))
        {
            if(TextUtils.isDigitsOnly(newValueStr))
            {
                int newTextSize = Integer.parseInt(newValueStr);
                if(UserPreferenceHelper.TIMELINES_MIN <= newTextSize && newTextSize <= UserPreferenceHelper.TIMELINES_MAX)
                {
                    return true;
                }
                new Notificator(getActivity(), getString(R.string.error_setting_timelines_range)).publish();
            }
            else
            {
                new Notificator(getActivity(), getString(R.string.error_setting_timelines_not_number)).publish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        if(preference.getKey().contentEquals(getString(R.string.key_setting_application_information)))
        {
            SimpleDialogFragment informationDialog = SimpleDialogFragment.newInstance(
                    R.layout.dialog_app_info,
                    getString(R.string.dialog_title_about));
            new DialogHelper(getActivity(), informationDialog).show();
        }
        else if(preference.getKey().contentEquals(getString(R.string.key_setting_licenses)))
        {
            SimpleDialogFragment licensesDialog = SimpleDialogFragment.newInstance(
                    R.layout.dialog_licenses,
                    getString(R.string.dialog_title_licenses));
            new DialogHelper(getActivity(), licensesDialog).show();
        }
        else if(preference.getKey().contentEquals(getString(R.string.key_setting_delete_authentication)))
        {

        }
        return true;
    }
}
