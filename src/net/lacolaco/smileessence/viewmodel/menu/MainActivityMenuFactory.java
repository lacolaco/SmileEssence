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

package net.lacolaco.smileessence.viewmodel.menu;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.resource.ResourceHelper;

public class MainActivityMenuFactory
{

    private final ResourceHelper resourceHelper;

    public MainActivityMenuFactory(ResourceHelper resourceHelper)
    {
        this.resourceHelper = resourceHelper;
    }

    public void addItemsToMenu(Menu menu)
    {
        //Add button
        MenuItem addButton = menu.add(Menu.NONE, R.id.actionbar_add, Menu.NONE, resourceHelper.getString(R.string.actionbar_add));
        addButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        addButton.setIcon(R.drawable.ic_action_new);
        //Setting button
        MenuItem setting = menu.add(Menu.NONE, R.id.actionbar_setting, Menu.NONE, resourceHelper.getString(R.string.actionbar_setting));
        setting.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        setting.setIcon(R.drawable.ic_action_settings);
        //External services
        SubMenu services = menu.addSubMenu(Menu.NONE, R.id.actionbar_services, Menu.NONE, resourceHelper.getString(R.string.actionbar_services));
        services.setHeaderIcon(R.drawable.ic_action_web_site);
        services.add(Menu.NONE, R.id.actionbar_favstar, Menu.NONE, resourceHelper.getString(R.string.actionbar_favstar));
        services.add(Menu.NONE, R.id.actionbar_aclog, Menu.NONE, resourceHelper.getString(R.string.actionbar_aclog));
        services.add(Menu.NONE, R.id.actionbar_twilog, Menu.NONE, resourceHelper.getString(R.string.actionbar_twilog));
        //Report to author
        MenuItem report = menu.add(Menu.NONE, R.id.actionbar_report, Menu.NONE, resourceHelper.getString(R.string.actionbar_report));
        report.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        report.setIcon(R.drawable.ic_action_email);
    }
}
