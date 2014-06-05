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
import android.widget.ListView;
import net.lacolaco.smileessence.Application;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.viewmodel.EditableCheckBoxModel;

import java.util.ArrayList;
import java.util.List;

public class EditTabActivity extends Activity
{

    // ------------------------------ FIELDS ------------------------------

    private CustomListAdapter<EditableCheckBoxModel> adapter;

    // --------------------- GETTER / SETTER METHODS ---------------------

    private EditableCheckBoxModel[] getCheckBoxItems()
    {
        List<EditableCheckBoxModel> models = new ArrayList<>();

        EditableCheckBoxModel post = new EditableCheckBoxModel(getString(R.string.page_name_post));
        post.setChecked(true).setInputText(String.valueOf(0)).setFreezing(true);
        models.add(post);
        EditableCheckBoxModel home = new EditableCheckBoxModel(getString(R.string.page_name_home));
        home.setChecked(true).setInputText(String.valueOf(1)).setFreezing(true);
        models.add(home);
        EditableCheckBoxModel mentions = new EditableCheckBoxModel(getString(R.string.page_name_mentions));
        mentions.setChecked(true).setInputText(String.valueOf(2)).setFreezing(true);
        models.add(mentions);
        EditableCheckBoxModel messages = new EditableCheckBoxModel(getString(R.string.page_name_messages));
        messages.setChecked(getVisibility(R.string.key_page_messages_visibility))
                .setInputText(String.valueOf(getPosition(R.string.key_page_messages_position, 3)));
        models.add(messages);
        EditableCheckBoxModel history = new EditableCheckBoxModel(getString(R.string.page_name_history));
        history.setChecked(getVisibility(R.string.key_page_history_visibility))
               .setInputText(String.valueOf(getPosition(R.string.key_page_history_position, 4)));
        models.add(history);
        EditableCheckBoxModel search = new EditableCheckBoxModel(getString(R.string.page_name_search));
        search.setChecked(getVisibility(R.string.key_page_search_visibility))
              .setInputText(String.valueOf(getPosition(R.string.key_page_search_position, 5)));
        models.add(search);
        EditableCheckBoxModel list = new EditableCheckBoxModel(getString(R.string.page_name_list));
        list.setChecked(getVisibility(R.string.key_page_list_visibility))
            .setInputText(String.valueOf(getPosition(R.string.key_page_list_position, 6)));
        models.add(list);
        return models.toArray(new EditableCheckBoxModel[models.size()]);
    }

    private ListView getListView()
    {
        return (ListView) findViewById(R.id.listview_edit_list);
    }

    private MainActivity getMainActivity()
    {
        return (MainActivity) getParent();
    }

    private UserPreferenceHelper getPreference()
    {
        return new UserPreferenceHelper(this);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        UserPreferenceHelper userPreferenceHelper = getPreference();
        setTheme(Themes.getTheme(((Application) getApplication()).getThemeIndex()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_list);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initializeViews();
        Logger.debug("EditTabActivity:onCreate");
    }

    @Override
    protected void onDestroy()
    {
        EditableCheckBoxModel messages = (EditableCheckBoxModel) adapter.getItem(3);
        putVisibility(R.string.key_page_messages_visibility, messages.isChecked());
        //TODO        putPosition(R.string.key_page_messages_position, Integer.parseInt(messages.getInputText()));
        EditableCheckBoxModel history = (EditableCheckBoxModel) adapter.getItem(4);
        putVisibility(R.string.key_page_history_visibility, history.isChecked());
        EditableCheckBoxModel search = (EditableCheckBoxModel) adapter.getItem(5);
        putVisibility(R.string.key_page_search_visibility, search.isChecked());
        EditableCheckBoxModel list = (EditableCheckBoxModel) adapter.getItem(6);
        putVisibility(R.string.key_page_list_visibility, list.isChecked());
        new Notificator(this, R.string.notice_tab_editted).makeToast().show();
        super.onDestroy();
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

    private int getPosition(int key, int defaultValue)
    {
        return Integer.parseInt(getPreference().getValue(key, String.valueOf(defaultValue)));
    }

    private boolean getVisibility(int key)
    {
        return getPreference().getValue(key, true);
    }

    private void initializeViews()
    {
        ListView listView = getListView();
        adapter = new CustomListAdapter<>(this, EditableCheckBoxModel.class);
        listView.setAdapter(adapter);
        adapter.addToTop(getCheckBoxItems());
        adapter.update();
    }

    private void putPosition(int key, int value)
    {
        getPreference().putValue(key, String.valueOf(value));
    }

    private void putVisibility(int key, boolean value)
    {
        getPreference().putValue(key, value);
    }
}
