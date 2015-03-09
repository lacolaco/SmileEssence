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
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.*;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.util.ArrayList;
import java.util.List;

public class StatusMenuDialogFragment extends MenuDialogFragment
{

    // ------------------------------ FIELDS ------------------------------

    private static final String KEY_STATUS_ID = "statusID";

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getStatusID()
    {
        return getArguments().getLong(KEY_STATUS_ID);
    }

    public void setStatusID(long statusID)
    {
        Bundle args = new Bundle();
        args.putLong(KEY_STATUS_ID, statusID);
        setArguments(args);
    }

    // ------------------------ OVERRIDE METHODS ------------------------


    @Override
    protected void executeCommand(Command command)
    {
        if(command.execute())
        {
            dismiss();
            DialogHelper.close(getActivity(), StatusViewModel.STATUS_DIALOG);
        }
    }

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

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).setView(body).create();
        TwitterUtils.tryGetStatus(account, getStatusID(), new TwitterUtils.StatusCallback()
        {
            @Override
            public void success(Status status)
            {
                List<Command> commands = getCommands(activity, status, account);
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
        return alertDialog;
    }

    // -------------------------- OTHER METHODS --------------------------

    public void addBottomCommands(Activity activity, Status status, Account account, ArrayList<Command> commands)
    {
        commands.add(new CommandSaveAsTemplate(activity, TwitterUtils.getOriginalStatusText(status)));
        //User
        for(String screenName : TwitterUtils.getScreenNames(status, null))
        {
            commands.add(new CommandOpenUserDetail(activity, screenName, account));
        }
        for(Command command : getHashtagCommands(activity, status))
        {
            commands.add(command);
        }
        // Media
        if(status.getURLEntities() != null)
        {
            for(URLEntity urlEntity : status.getURLEntities())
            {
                commands.add(new CommandOpenURL(activity, urlEntity.getExpandedURL()));
            }
        }
        for(MediaEntity mediaEntity : status.getExtendedMediaEntities().length == 0 ? status.getMediaEntities() : status.getExtendedMediaEntities())
        {
            commands.add(new CommandOpenURL(activity, mediaEntity.getMediaURL()));
        }
    }

    public boolean addMainCommands(Activity activity, Status status, Account account, ArrayList<Command> commands)
    {
        return commands.addAll(Command.getStatusCommands(activity, status, account));
    }

    public List<Command> getCommands(Activity activity, Status status, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        addMainCommands(activity, status, account, commands);
        addBottomCommands(activity, status, account, commands);
        return commands;
    }

    private ArrayList<Command> getHashtagCommands(Activity activity, Status status)
    {
        ArrayList<Command> commands = new ArrayList<>();
        if(status.getHashtagEntities() != null)
        {
            for(HashtagEntity hashtagEntity : status.getHashtagEntities())
            {
                commands.add(new CommandOpenHashtagDialog(activity, hashtagEntity));
            }
        }
        return commands;
    }
}
