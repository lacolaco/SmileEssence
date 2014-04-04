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
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.PostState;

public class MainActivityMenuHelper
{

    public static void addItemsToMenu(MainActivity activity, Menu menu)
    {
        //Add button
        MenuItem addButton = menu.add(Menu.NONE, R.id.actionbar_add_page, Menu.NONE, activity.getString(R.string.actionbar_add_page));
        addButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        addButton.setIcon(R.drawable.ic_action_new);
        //Settings
        SubMenu settings = menu.addSubMenu(Menu.NONE, R.id.actionbar_settings, Menu.NONE, activity.getString(R.string.actionbar_settings));
        settings.setIcon(R.drawable.ic_action_settings);
        settings.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        settings.add(Menu.NONE, R.id.actionbar_setting, Menu.NONE, activity.getString(R.string.actionbar_setting));
        settings.add(Menu.NONE, R.id.actionbar_edit_templates, Menu.NONE, activity.getString(R.string.actionbar_edit_templates));
        settings.add(Menu.NONE, R.id.actionbar_edit_extraction, Menu.NONE, activity.getString(R.string.actionbar_edit_extraction));
        settings.add(Menu.NONE, R.id.actionbar_edit_commands, Menu.NONE, activity.getString(R.string.actionbar_edit_commands));
        //External services
        SubMenu services = menu.addSubMenu(Menu.NONE, R.id.actionbar_services, Menu.NONE, activity.getString(R.string.actionbar_services));
        services.setIcon(R.drawable.ic_action_web_site);
        services.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        services.add(Menu.NONE, R.id.actionbar_favstar, Menu.NONE, activity.getString(R.string.actionbar_favstar));
        services.add(Menu.NONE, R.id.actionbar_aclog, Menu.NONE, activity.getString(R.string.actionbar_aclog));
        services.add(Menu.NONE, R.id.actionbar_twilog, Menu.NONE, activity.getString(R.string.actionbar_twilog));
        //Report to author
        MenuItem report = menu.add(Menu.NONE, R.id.actionbar_report, Menu.NONE, activity.getString(R.string.actionbar_report));
        report.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        report.setIcon(R.drawable.ic_action_email);
    }

    public static boolean onItemSelected(MainActivity activity, MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.actionbar_add_page:
            {
                //TODO add page dialog
                break;
            }
            case R.id.actionbar_setting:
            {
                //TODO start setting activity
                break;
            }
            case R.id.actionbar_edit_templates:
            {
                //TODO start edit templates activity
                break;
            }
            case R.id.actionbar_edit_extraction:
            {
                //TODO start edit extraction activity
                break;
            }
            case R.id.actionbar_edit_commands:
            {
                //TODO start edit commands activity
                break;
            }
            case R.id.actionbar_favstar:
            {
                new CommandOpenURL(activity, TwitterUtils.getFavstarRecentURL(activity.getCurrentAccount().screenName)).execute();
                break;
            }
            case R.id.actionbar_aclog:
            {
                new CommandOpenURL(activity, TwitterUtils.getAclogTimelineURL(activity.getCurrentAccount().screenName)).execute();
                break;
            }
            case R.id.actionbar_twilog:
            {
                new CommandOpenURL(activity, TwitterUtils.getTwilogURL(activity.getCurrentAccount().screenName)).execute();
                break;
            }
            case R.id.actionbar_report:
            {
                PostState.newState().beginTransaction()
                         .setCursor(0)
                         .setText(activity.getString(R.string.text_message_to_author, activity.getVersion()))
                         .requestOpenPage(true)
                         .commit();
                break;
            }
        }
        return true;
    }

}
