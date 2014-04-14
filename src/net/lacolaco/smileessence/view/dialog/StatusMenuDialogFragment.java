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
import android.widget.ImageButton;
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenUserDetail;
import net.lacolaco.smileessence.command.status.*;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class StatusMenuDialogFragment extends MenuDialogFragment implements View.OnClickListener
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
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity)getActivity();
        Account account = activity.getCurrentAccount();
        Status status = TwitterUtils.tryGetStatus(account, getStatusID());
        List<Command> commands = getCommands(activity, status, account);
        filterCommands(commands);
        View body = activity.getLayoutInflater().inflate(R.layout.dialog_menu_list, null);
        ListView listView = (ListView)body.findViewById(R.id.listview_dialog_menu_list);
        CustomListAdapter<Command> adapter = new CustomListAdapter<>(activity, Command.class);
        listView.setAdapter(adapter);
        for(Command command : commands)
        {
            adapter.addToBottom(command);
        }
        adapter.update();
        listView.setOnItemClickListener(onItemClickListener);
        View header = getTitleView(activity, account, status);
        header.setClickable(false);

        return new AlertDialog.Builder(activity)
                .setCustomTitle(header)
                .setView(body)
                .setCancelable(true)
                .create();
    }

    private View getTitleView(MainActivity activity, Account account, Status status)
    {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_status_detail, null);
        View statusHeader = view.findViewById(R.id.layout_status_header);
        statusHeader = new StatusViewModel(status, account).getView(activity, activity.getLayoutInflater(), statusHeader);
        ImageButton message = (ImageButton)view.findViewById(R.id.button_status_detail_reply);
        message.setOnClickListener(this);
        ImageButton retweet = (ImageButton)view.findViewById(R.id.button_status_detail_retweet);
        retweet.setTag(status.getCurrentUserRetweetId());
        if(status.isRetweetedByMe())
        {
            retweet.setImageDrawable(getResources().getDrawable(R.drawable.icon_retweet_on));
        }
        retweet.setOnClickListener(this);
        ImageButton favorite = (ImageButton)view.findViewById(R.id.button_status_detail_favorite);
        favorite.setTag(status.isFavorited());
        if(status.isFavorited())
        {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.icon_favorite_on));
        }
        favorite.setOnClickListener(this);
        ImageButton delete = (ImageButton)view.findViewById(R.id.button_status_detail_delete);
        boolean deletable = false;
        if(!status.isRetweet())
        {
            deletable = status.getUser().getId() == account.userID;
        }
        else
        {
            deletable = status.getRetweetedStatus().getUser().getId() == account.userID;
        }
        delete.setVisibility(deletable ? View.VISIBLE : View.GONE);
        delete.setOnClickListener(this);
        return view;
    }

    public List<Command> getCommands(Activity activity, Status status, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new StatusCommandReply(activity, status));
        commands.add(new StatusCommandAddToReply(activity, status));
        commands.add(new StatusCommandReplyToAll(activity, status, account));
        commands.add(new StatusCommandFavorite(activity, status, account));
        commands.add(new StatusCommandRetweet(activity, status, account));
        commands.add(new StatusCommandDelete(activity, status, account));
        commands.add(new StatusCommandFavAndRT(activity, status, account));
        commands.add(new StatusCommandQuote(activity, status));
        commands.add(new StatusCommandShare(activity, status));
        commands.add(new StatusCommandOpenInBrowser(activity, status));
        commands.add(new StatusCommandClipboard(activity, status));
        commands.add(new StatusCommandTofuBuster(activity, status));
        commands.add(new StatusCommandNanigaja(activity, status, account));
        commands.add(new StatusCommandMakeAnonymous(activity, status, account));
        commands.add(new StatusCommandOpenChain(activity, status, account));
        for(String screenName : TwitterUtils.getScreenNames(status, null))
        {
            commands.add(new CommandOpenUserDetail(activity, screenName, account));
        }
        return commands;
    }

    @Override
    public void onClick(View v)
    {

    }
}
