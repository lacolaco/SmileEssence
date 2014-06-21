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

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.*;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.PostState;

public class MainActivityMenuHelper
{

    // -------------------------- STATIC METHODS --------------------------

    public static void addItemsToMenu(MainActivity activity, Menu menu)
    {
        //Post button
        MenuItem postButton = menu.add(Menu.NONE, R.id.actionbar_post, Menu.NONE, activity.getString(R.string.actionbar_post));
        postButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        postButton.setIcon(R.drawable.icon_edit);
        //Search button
        //        MenuItem searchButton = menu.add(Menu.NONE, R.id.actionbar_search, Menu.NONE, activity.getString(R.string.actionbar_search));
        //        searchButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //        searchButton.setIcon(R.drawable.icon_search_white);
        //Settings
        SubMenu settings = menu.addSubMenu(Menu.NONE, R.id.actionbar_settings, Menu.NONE, activity.getString(R.string.actionbar_settings));
        settings.setIcon(R.drawable.icon_settings);
        settings.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        settings.add(Menu.NONE, R.id.actionbar_setting, Menu.NONE, activity.getString(R.string.actionbar_setting));
        settings.add(Menu.NONE, R.id.actionbar_edit_templates, Menu.NONE, activity.getString(R.string.actionbar_edit_templates));
        settings.add(Menu.NONE, R.id.actionbar_edit_extraction, Menu.NONE, activity.getString(R.string.actionbar_edit_extraction));
        settings.add(Menu.NONE, R.id.actionbar_edit_commands, Menu.NONE, activity.getString(R.string.actionbar_edit_commands));
        settings.add(Menu.NONE, R.id.actionbar_edit_tabs, Menu.NONE, activity.getString(R.string.actionbar_edit_tabs));
        //External services
        SubMenu services = menu.addSubMenu(Menu.NONE, R.id.actionbar_services, Menu.NONE, activity.getString(R.string.actionbar_services));
        services.setIcon(R.drawable.icon_website);
        services.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        services.add(Menu.NONE, R.id.actionbar_favstar, Menu.NONE, activity.getString(R.string.actionbar_favstar));
        services.add(Menu.NONE, R.id.actionbar_aclog, Menu.NONE, activity.getString(R.string.actionbar_aclog));
        services.add(Menu.NONE, R.id.actionbar_twilog, Menu.NONE, activity.getString(R.string.actionbar_twilog));
        //Report to author
        MenuItem report = menu.add(Menu.NONE, R.id.actionbar_report, Menu.NONE, activity.getString(R.string.actionbar_report));
        report.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        report.setIcon(R.drawable.icon_email);
    }

    public static boolean onItemSelected(MainActivity activity, MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.actionbar_post:
            {
                openPostPage(activity);
                return true;
            }
            case R.id.actionbar_search:
            {
                openSearchPage(activity);
                return true;
            }
            case R.id.actionbar_setting:
            {
                Intent intent = new Intent(activity, SettingActivity.class);
                activity.startActivity(intent);
                return true;
            }
            case R.id.actionbar_edit_templates:
            {
                Intent intent = new Intent(activity, EditTemplateActivity.class);
                activity.startActivity(intent);
                return true;
            }
            case R.id.actionbar_edit_extraction:
            {
                Intent intent = new Intent(activity, EditExtractionActivity.class);
                activity.startActivity(intent);
                return true;
            }
            case R.id.actionbar_edit_commands:
            {
                Intent intent = new Intent(activity, EditCommandActivity.class);
                activity.startActivity(intent);
                return true;
            }
            case R.id.actionbar_edit_tabs:
            {
                Intent intent = new Intent(activity, EditTabActivity.class);
                activity.startActivity(intent);
                return true;
            }
            case R.id.actionbar_favstar:
            {
                new CommandOpenURL(activity, TwitterUtils.getFavstarRecentURL(activity.getCurrentAccount().screenName)).execute();
                return true;
            }
            case R.id.actionbar_aclog:
            {
                new CommandOpenURL(activity, TwitterUtils.getAclogTimelineURL(activity.getCurrentAccount().screenName)).execute();
                return true;
            }
            case R.id.actionbar_twilog:
            {
                new CommandOpenURL(activity, TwitterUtils.getTwilogURL(activity.getCurrentAccount().screenName)).execute();
                return true;
            }
            case R.id.actionbar_report:
            {
                PostState.getState().beginTransaction()
                         .appendText(activity.getString(R.string.text_message_to_author, activity.getVersion()))
                         .commitWithOpen(activity);
                return true;
            }
            default:
            {
                return false;
            }
        }
    }

    private static void openPostPage(MainActivity activity)
    {
        activity.openPostPage();
    }

    private static void openSearchPage(MainActivity activity)
    {
        activity.openSearchPage();
    }

}
