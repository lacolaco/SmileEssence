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

package net.lacolaco.smileessence.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddNewPageDialog extends MenuDialogFragment
{

// ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity) getActivity();
        Account account = activity.getCurrentAccount();
        List<Command> commands = getCommands(activity);
        View body = activity.getLayoutInflater().inflate(R.layout.dialog_menu_list, null);
        ListView listView = (ListView) body.findViewById(R.id.listview_dialog_menu_list);
        CustomListAdapter<Command> adapter = new CustomListAdapter<>(activity, Command.class);
        listView.setAdapter(adapter);
        for(Command command : commands)
        {
            adapter.addToBottom(command);
        }
        adapter.update();
        listView.setOnItemClickListener(onItemClickListener);

        return new AlertDialog.Builder(activity).setView(body).setTitle(R.string.dialog_title_add_new_page).setCancelable(true).create();
    }

    public List<Command> getCommands(final MainActivity activity)
    {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new Command(-1, activity)
        {
            @Override
            public boolean execute()
            {
                openCreateSearchPageDialog(activity);
                return true;
            }

            @Override
            public String getText()
            {
                return activity.getString(R.string.command_create_search_page_dialog);
            }

            @Override
            public boolean isEnabled()
            {
                return true;
            }
        });
        return commands;
    }

    private void openCreateSearchPageDialog(final MainActivity activity)
    {
        EditTextDialogFragment dialogFragment = new EditTextDialogFragment()
        {
            @Override
            public void onTextInput(String text)
            {
                if(TextUtils.isEmpty(text))
                {
                    return;
                }
                activity.addSearchPage(text, true);
            }
        };
        dialogFragment.setParams(getString(R.string.dialog_create_search_page), "");
        DialogHelper.showDialog(activity, dialogFragment);
    }
}
