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
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.command.CommandOpenUserDetail;
import net.lacolaco.smileessence.command.message.MessageCommandDelete;
import net.lacolaco.smileessence.command.message.MessageCommandReply;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.viewmodel.MessageViewModel;
import twitter4j.DirectMessage;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;

import java.util.ArrayList;
import java.util.List;

public class MessageMenuDialogFragment extends MenuDialogFragment
{

    // ------------------------------ FIELDS ------------------------------

    private static final String KEY_MESSAGE_ID = "messageID";

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getMessageID()
    {
        return getArguments().getLong(KEY_MESSAGE_ID);
    }

    public void setMessageID(long messageID)
    {
        Bundle args = new Bundle();
        args.putLong(KEY_MESSAGE_ID, messageID);
        setArguments(args);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity) getActivity();
        Account account = activity.getCurrentAccount();
        DirectMessage message = TwitterUtils.tryGetMessage(account, getMessageID());
        List<Command> commands = getCommands(activity, message, account);
        filterCommands(commands);
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
        View header = getTitleView(activity, account, message);
        header.setClickable(false);

        return new AlertDialog.Builder(activity)
                .setCustomTitle(header)
                .setView(body)
                .setCancelable(true)
                .create();
    }

    // -------------------------- OTHER METHODS --------------------------

    public List<Command> getCommands(Activity activity, DirectMessage message, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new MessageCommandReply(activity, message));
        commands.add(new MessageCommandDelete(activity, message, account));
        commands.addAll(getURLCommands(activity, message));
        commands.addAll(getScreenNameCommands(activity, message, account));
        return commands;
    }

    public List<Command> getScreenNameCommands(Activity activity, DirectMessage message, Account account)
    {
        List<Command> commands = new ArrayList<>();
        for(String screenName : TwitterUtils.getScreenNames(message, null))
        {
            commands.add(new CommandOpenUserDetail(activity, screenName, account));
        }
        return commands;
    }

    public List<Command> getURLCommands(Activity activity, DirectMessage message)
    {
        List<Command> commands = new ArrayList<>();
        if(message.getURLEntities() != null)
        {
            for(URLEntity urlEntity : message.getURLEntities())
            {
                commands.add(new CommandOpenURL(activity, urlEntity.getExpandedURL()));
            }
        }
        for(MediaEntity mediaEntity : getMediaEntities(message))
        {
            commands.add(new CommandOpenURL(activity, mediaEntity.getMediaURL()));
        }
        return commands;
    }

    private MediaEntity[] getMediaEntities(DirectMessage message)
    {
        if(message.getExtendedMediaEntities().length == 0)
        {
            // direct message's media is contained also in url entities.
            return new MediaEntity[0];
        }
        else
        {
            return message.getExtendedMediaEntities();
        }
    }

    private View getTitleView(MainActivity activity, Account account, DirectMessage message)
    {
        return new MessageViewModel(message, account).getView(activity, activity.getLayoutInflater(), null);
    }
}
