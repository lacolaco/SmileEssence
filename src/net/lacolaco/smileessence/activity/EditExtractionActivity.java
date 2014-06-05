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
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import net.lacolaco.smileessence.Application;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.ExtractionWord;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.EditTextDialogFragment;

import java.util.List;

public class EditExtractionActivity extends Activity implements AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener
{

    // ------------------------------ FIELDS ------------------------------

    private CustomListAdapter<ExtractionWord> adapter;

    // --------------------- GETTER / SETTER METHODS ---------------------

    private ExtractionWord[] getExtractionWords()
    {
        List<ExtractionWord> all = ExtractionWord.getAll();
        return all.toArray(new ExtractionWord[all.size()]);
    }

    private ListView getListView()
    {
        return (ListView) findViewById(R.id.listview_edit_list);
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface Callback ---------------------

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        menu.clear();
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.edit_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_edit_list_delete:
            {
                deleteSelectedItems();
            }
        }
        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
    }

    // --------------------- Interface MultiChoiceModeListener ---------------------

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
    {
    }

    // --------------------- Interface OnItemClickListener ---------------------

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        openEditExtractionWordDialog(position);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(Themes.getTheme(((Application) getApplication()).getThemeIndex()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_list);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initializeViews();
        Logger.debug("EditExtractionWordActivity:onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem add = menu.add(Menu.NONE, R.id.menu_edit_list_add, Menu.NONE, "");
        add.setIcon(android.R.drawable.ic_menu_add);
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_edit_list_add:
            {
                addNewExtractionWord();
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

    // -------------------------- OTHER METHODS --------------------------

    public void deleteSelectedItems()
    {
        SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
        adapter.setNotifiable(false);
        for(int i = adapter.getCount() - 1; i > -1; i--)
        {
            if(checkedItems.get(i))
            {
                ExtractionWord extractionWord = adapter.removeItem(i);
                extractionWord.delete();
            }
        }
        adapter.setNotifiable(true);
        adapter.notifyDataSetChanged();
        updateListView();
    }

    public void openEditExtractionWordDialog(int position)
    {
        final ExtractionWord extractionWord = (ExtractionWord) adapter.getItem(position);
        EditTextDialogFragment dialogFragment = new EditTextDialogFragment()
        {
            @Override
            public void onTextInput(String text)
            {
                if(TextUtils.isEmpty(text.trim()))
                {
                    return;
                }
                extractionWord.text = text;
                extractionWord.save();
                adapter.notifyDataSetChanged();
                updateListView();
            }
        };
        dialogFragment.setParams(getString(R.string.dialog_title_edit), extractionWord.text);
        DialogHelper.showDialog(this, dialogFragment);
    }

    private void addNewExtractionWord()
    {
        final ExtractionWord extractionWord = new ExtractionWord();
        EditTextDialogFragment dialogFragment = new EditTextDialogFragment()
        {
            @Override
            public void onTextInput(String text)
            {
                if(TextUtils.isEmpty(text.trim()))
                {
                    return;
                }
                extractionWord.text = text;
                extractionWord.save();
                adapter.addToBottom(extractionWord);
                adapter.notifyDataSetChanged();
                updateListView();
            }
        };
        dialogFragment.setParams(getString(R.string.dialog_title_add), "");
        DialogHelper.showDialog(this, dialogFragment);
    }

    private void initializeViews()
    {
        ListView listView = getListView();
        adapter = new CustomListAdapter<>(this, ExtractionWord.class);
        listView.setAdapter(adapter);
        adapter.addToTop(getExtractionWords());
        adapter.update();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(this);
        listView.setMultiChoiceModeListener(this);
    }

    private void updateListView()
    {
        getListView().requestLayout();
    }
}
