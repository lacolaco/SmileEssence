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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandSearchOnTwitter;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;

public class UserMenuDialogFragment extends MenuDialogFragment
{

    // ------------------------------ FIELDS ------------------------------

    private static final String KEY_USER_ID = "userID";

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getUserID()
    {
        return getArguments().getLong(KEY_USER_ID);
    }

    public void setUserID(long userID)
    {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userID);
        setArguments(args);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account account = activity.getCurrentAccount();
        View body = activity.getLayoutInflater().inflate(R.layout.dialog_menu_list, null);
        ListView listView = (ListView) body.findViewById(R.id.listview_dialog_menu_list);
        final CustomListAdapter<Command> adapter = new CustomListAdapter<>(activity, Command.class);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        TwitterUtils.tryGetUser(account, getUserID(), new TwitterUtils.UserCallback()
        {
            @Override
            public void success(User user)
            {
                List<Command> commands = getCommands(activity, user, account);
                Command.filter(commands);
                for(Command command : commands)
                {
                    adapter.addToBottom(command);
                }
                adapter.update();
            }

            @Override
            public void error()
            {
                dismiss();
            }
        });

        return new AlertDialog.Builder(activity)
                .setView(body)
                .setCancelable(true)
                .create();
    }

    @Override
    protected void onItemClick(AdapterView<?> adapterView, int i)
    {
        super.onItemClick(adapterView, i);
        dismiss();
    }

    // -------------------------- OTHER METHODS --------------------------

    public boolean addBottomCommands(Activity activity, User user, ArrayList<Command> commands)
    {
        return commands.add(new CommandSearchOnTwitter(activity, user.getScreenName()));
    }

    public boolean addMainCommands(Activity activity, User user, Account account, ArrayList<Command> commands)
    {
        return commands.addAll(Command.getUserCommands(activity, user, account));
    }

    public List<Command> getCommands(Activity activity, User user, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        addMainCommands(activity, user, account, commands);
        addBottomCommands(activity, user, commands);
        return commands;
    }
}
