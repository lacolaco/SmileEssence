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

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.lacolaco.smileessence.Application;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.util.Themes;

import java.util.ArrayList;
import java.util.List;

public class LicenseActivity extends Activity
{

    // --------------------- GETTER / SETTER METHODS ---------------------

    private List<String> getFileNames()
    {
        List<String> apacheFiles = new ArrayList<>();
        apacheFiles.add(getString(R.string.library_name_twitter4j));
        apacheFiles.add(getString(R.string.library_name_crouton));
        apacheFiles.add(getString(R.string.library_name_pull_to_refresh));
        apacheFiles.add(getString(R.string.library_name_volley));
        apacheFiles.add(getString(R.string.library_name_activeandroid));
        apacheFiles.add(getString(R.string.library_name_guava));
        apacheFiles.add(getString(R.string.library_name_twitter_text));
        return apacheFiles;
    }

    private String getMarkerString()
    {
        return "-";
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setTheme(Themes.getTheme(((Application) getApplication()).getThemeIndex()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_license);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setFiles();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
            {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }
        return true;
    }

    private void setFiles()
    {
        LinearLayout files = (LinearLayout) findViewById(R.id.linear_license_files);
        List<String> apacheFiles = getFileNames();
        String marker = getMarkerString();
        for(String apacheFile : apacheFiles)
        {
            TextView name = new TextView(this);
            name.setText(String.format("%s %s", marker, apacheFile));
            files.addView(name);
        }
    }
}