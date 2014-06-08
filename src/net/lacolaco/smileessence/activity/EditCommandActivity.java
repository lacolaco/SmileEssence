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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import net.lacolaco.smileessence.Application;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.status.StatusCommand;
import net.lacolaco.smileessence.command.user.UserCommand;
import net.lacolaco.smileessence.data.CommandSettingCache;
import net.lacolaco.smileessence.entity.CommandSetting;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.viewmodel.CheckBoxModel;

import java.util.ArrayList;
import java.util.List;

public class EditCommandActivity extends Activity
{

    // ------------------------------ FIELDS ------------------------------

    private CustomListAdapter<CheckBoxModel> adapter;
    private List<Command> editedCommands;

    // --------------------- GETTER / SETTER METHODS ---------------------

    private CheckBoxModel[] getCheckBoxItems()
    {
        editedCommands = new ArrayList<>();
        List<CheckBoxModel> checkBoxModels = new ArrayList<>();
        List<CommandSetting> commandSettings = CommandSetting.getAll();
        List<Command> commands = Command.getAllCommands(this);
        for(Command command : commands)
        {
            if(command.getKey() < 0)
            {
                continue;
            }
            String text;
            if(command instanceof StatusCommand)
            {
                text = String.format("Tweet : %s", command.getText());
            }
            else if(command instanceof UserCommand)
            {
                text = String.format("User : %s", command.getText());
            }
            else
            {
                continue;
            }
            editedCommands.add(command);
            CommandSetting setting = null;
            for(CommandSetting s : commandSettings)
            {
                if(command.getKey() == s.commandKey)
                {
                    setting = s;
                }
            }
            if(setting == null)
            {
                setting = new CommandSetting(command.getKey(), true);
                setting.save();
            }
            CheckBoxModel checkBoxModel = new CheckBoxModel(text, setting.visibility);
            checkBoxModels.add(checkBoxModel);
        }
        return checkBoxModels.toArray(new CheckBoxModel[checkBoxModels.size()]);
    }

    private ListView getListView()
    {
        return (ListView) findViewById(R.id.listview_edit_list);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        UserPreferenceHelper userPreferenceHelper = new UserPreferenceHelper(this);
        setTheme(Themes.getTheme(((Application) getApplication()).getThemeIndex()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_list);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initializeViews();
        Logger.debug("EditCommandActivity:onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem allOn = menu.add(Menu.NONE, R.id.menu_edit_command_all_on, Menu.NONE, "");
        allOn.setTitle(R.string.actionbar_edit_command_all_on);
        allOn.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        for(int i = 0; i < adapter.getCount(); i++)
        {
            CheckBoxModel checkBoxModel = (CheckBoxModel) adapter.getItem(i);
            Command command = editedCommands.get(i);
            CommandSetting commandSetting = CommandSetting.selectByKey(command.getKey());
            commandSetting.visibility = checkBoxModel.isChecked();
            commandSetting.save();
            CommandSettingCache.getInstance().put(commandSetting);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_edit_command_all_on:
            {
                enableAll();
                break;
            }
            case android.R.id.home:
            {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }
        return true;
    }

    private void enableAll()
    {
        adapter.setNotifiable(false);
        for(int i = 0; i < adapter.getCount(); i++)
        {
            CheckBoxModel item = (CheckBoxModel) adapter.getItem(i);
            item.setChecked(true);
        }
        adapter.setNotifiable(true);
        adapter.notifyDataSetChanged();
        updateListView();
    }

    private void initializeViews()
    {
        ListView listView = getListView();
        adapter = new CustomListAdapter<>(this, CheckBoxModel.class);
        listView.setAdapter(adapter);
        adapter.addToTop(getCheckBoxItems());
        adapter.update();
    }

    private void updateListView()
    {
        getListView().requestLayout();
    }
}
