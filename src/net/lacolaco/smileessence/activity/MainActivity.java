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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.preference.PreferenceHelper;
import net.lacolaco.smileessence.property.PropertyHelper;
import net.lacolaco.smileessence.resource.ResourceHelper;

import java.io.IOException;

public class MainActivity extends Activity
{

    private ResourceHelper resourceHelper;
    private PreferenceHelper preferenceHelper;
    private PropertyHelper propertyHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try
        {
            setupHelpers();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onPause()
    {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onStop()
    {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private String getVersion()
    {
        return resourceHelper.getString(R.string.app_version);
    }

    private String getLastLaunchVersion()
    {
        return propertyHelper.getValue("app.version");
    }

    public boolean IsFirstLaunchByVersion()
    {
        return !getVersion().contentEquals(getLastLaunchVersion());
    }

    private void setupHelpers() throws IOException
    {
        resourceHelper = new ResourceHelper(this);
        preferenceHelper = new PreferenceHelper(this);
        propertyHelper = new PropertyHelper(this.getAssets(), "app.properties");
    }

    public ResourceHelper getResourceHelper()
    {
        return resourceHelper;
    }

    public PreferenceHelper getPreferenceHelper()
    {
        return preferenceHelper;
    }

    public PropertyHelper getPropertyHelper()
    {
        return propertyHelper;
    }
}
