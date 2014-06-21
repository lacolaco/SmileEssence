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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.*;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TweetBuilder;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.DeleteStatusTask;
import net.lacolaco.smileessence.twitter.task.FavoriteTask;
import net.lacolaco.smileessence.twitter.task.RetweetTask;
import net.lacolaco.smileessence.twitter.task.UnfavoriteTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.view.adapter.PostState;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.*;

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

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnClickListener ---------------------

    @Override
    public void onClick(final View v)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account account = activity.getCurrentAccount();
        TwitterUtils.tryGetStatus(account, getStatusID(), new TwitterUtils.StatusCallback()
        {
            @Override
            public void onCallback(Status status)
            {
                switch(v.getId())
                {
                    case R.id.button_status_detail_reply:
                    {
                        replyToStatus(activity, status);
                        break;
                    }
                    case R.id.button_status_detail_retweet:
                    {
                        final Long retweetID = (Long) v.getTag();
                        toggleRetweet(activity, account, status, retweetID);
                        break;
                    }
                    case R.id.button_status_detail_favorite:
                    {
                        Boolean isFavorited = (Boolean) v.getTag();
                        toggleFavorite(activity, account, status, isFavorited);
                        break;
                    }
                    case R.id.button_status_detail_delete:
                    {
                        deleteStatus(activity, account, status);
                        break;
                    }
                    default:
                    {
                        dismiss();
                    }
                }
            }
        });
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

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).setView(body).create();
        TwitterUtils.tryGetStatus(account, getStatusID(), new TwitterUtils.StatusCallback()
        {
            @Override
            public void onCallback(Status status)
            {
                List<Command> commands = getCommands(activity, status, account);
                filterCommands(commands);
                for(Command command : commands)
                {
                    adapter.addToBottom(command);
                }
                adapter.update();
                View header = getTitleView(activity, account, status);
                header.setClickable(false);
                alertDialog.setCustomTitle(header);
            }
        });
        return alertDialog;
    }

    // -------------------------- OTHER METHODS --------------------------

    public void addBottomCommands(Activity activity, Status status, Account account, ArrayList<Command> commands)
    {
        commands.add(new CommandSaveAsTemplate(activity, TwitterUtils.getOriginalStatusText(status)));
        for(String screenName : TwitterUtils.getScreenNames(status, null))
        {
            commands.add(new CommandOpenUserDetail(activity, screenName, account));
        }
        for(Command command : getHashtagCommands(activity, status))
        {
            commands.add(command);
        }
    }

    public boolean addMainCommands(Activity activity, Status status, Account account, ArrayList<Command> commands)
    {
        return commands.addAll(Command.getStatusCommands(activity, status, account));
    }

    public void addTopCommands(Activity activity, Status status, ArrayList<Command> commands)
    {
        for(Command command : getURLCommands(activity, status))
        {
            commands.add(command);
        }
    }

    public List<Command> getCommands(Activity activity, Status status, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        addTopCommands(activity, status, commands);
        addMainCommands(activity, status, account, commands);
        addBottomCommands(activity, status, account, commands);
        return commands;
    }

    private void confirm(MainActivity activity, Runnable onYes)
    {
        ConfirmDialogFragment.show(activity, getString(R.string.dialog_confirm_commands), onYes);
    }

    private void deleteStatus(final MainActivity activity, final Account account, final Status status)
    {
        confirm(activity, new Runnable()
        {
            @Override
            public void run()
            {
                new DeleteStatusTask(TwitterApi.getTwitter(account), TwitterUtils.getOriginalStatus(status).getId(), activity).execute();
                dismiss();
            }
        });
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

    private MediaEntity[] getMediaEntities(Status status)
    {
        if(status.getExtendedMediaEntities().length == 0)
        {
            return status.getMediaEntities();
        }
        else
        {
            return status.getExtendedMediaEntities();
        }
    }

    private View getTitleView(MainActivity activity, Account account, Status status)
    {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_status_detail, null);
        View statusHeader = view.findViewById(R.id.layout_status_header);
        StatusViewModel statusViewModel = new StatusViewModel(status, account);
        statusHeader = statusViewModel.getView(activity, activity.getLayoutInflater(), statusHeader);
        statusHeader.setClickable(false);
        int background = ((ColorDrawable) statusHeader.getBackground()).getColor();
        view.setBackgroundColor(background);
        ImageButton message = (ImageButton) view.findViewById(R.id.button_status_detail_reply);
        message.setOnClickListener(this);
        ImageButton retweet = (ImageButton) view.findViewById(R.id.button_status_detail_retweet);
        if(isNotRetweetable(account, status))
        {
            retweet.setVisibility(View.GONE);
        }
        else if(isRetweetDeletable(account, status))
        {
            retweet.setImageDrawable(getResources().getDrawable(R.drawable.icon_retweet_on));
            retweet.setTag(status.getId());
        }
        else
        {
            retweet.setTag(-1L);
        }
        retweet.setOnClickListener(this);
        ImageButton favorite = (ImageButton) view.findViewById(R.id.button_status_detail_favorite);
        favorite.setTag(statusViewModel.isFavorited());
        if(statusViewModel.isFavorited())
        {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.icon_favorite_on));
        }
        favorite.setOnClickListener(this);
        ImageButton delete = (ImageButton) view.findViewById(R.id.button_status_detail_delete);
        boolean deletable = isDeletable(account, status);
        delete.setVisibility(deletable ? View.VISIBLE : View.GONE);
        delete.setOnClickListener(this);
        return view;
    }

    private ArrayList<Command> getURLCommands(Activity activity, Status status)
    {
        ArrayList<Command> commands = new ArrayList<>();
        if(status.getURLEntities() != null)
        {
            for(URLEntity urlEntity : status.getURLEntities())
            {
                commands.add(new CommandOpenURL(activity, urlEntity.getExpandedURL()));
            }
        }
        for(MediaEntity mediaEntity : getMediaEntities(status))
        {
            commands.add(new CommandOpenURL(activity, mediaEntity.getMediaURL()));
        }
        return commands;
    }

    private boolean isDeletable(Account account, Status status)
    {
        boolean deletable = false;
        if(!status.isRetweet())
        {
            deletable = status.getUser().getId() == account.userID;
        }
        else
        {
            deletable = status.getRetweetedStatus().getUser().getId() == account.userID;
        }
        return deletable;
    }

    private boolean isNotRetweetable(Account account, Status status)
    {
        User user = TwitterUtils.getOriginalStatus(status).getUser();
        return status.getUser().isProtected() || status.getUser().getId() == account.userID;
    }

    private boolean isRetweetDeletable(Account account, Status status)
    {
        return status.isRetweet() && status.getUser().getId() == account.userID;
    }

    private void replyToStatus(MainActivity activity, Status status)
    {
        Status originalStatus = TwitterUtils.getOriginalStatus(status);
        TweetBuilder builder = new TweetBuilder().addScreenName(originalStatus.getUser().getScreenName());
        String text = builder.buildText();
        PostState.newState().beginTransaction()
                 .setText(text)
                 .setInReplyToStatusID(originalStatus.getId())
                 .moveCursor(text.length())
                 .commitWithOpen(activity);
    }

    private void toggleFavorite(MainActivity activity, Account account, Status status, Boolean isFavorited)
    {
        long statusID = status.isRetweet() ? status.getRetweetedStatus().getId() : status.getId();
        if(isFavorited)
        {
            new UnfavoriteTask(TwitterApi.getTwitter(account), statusID, activity).execute();
        }
        else
        {
            new FavoriteTask(TwitterApi.getTwitter(account), statusID, activity).execute();
        }
        dismiss();
    }

    private void toggleRetweet(final MainActivity activity, final Account account, final Status status, final Long retweetID)
    {
        confirm(activity, new Runnable()
        {
            @Override
            public void run()
            {
                if(retweetID != -1L)
                {
                    new DeleteStatusTask(TwitterApi.getTwitter(account), retweetID, activity).execute();
                }
                else
                {
                    new RetweetTask(TwitterApi.getTwitter(account), TwitterUtils.getOriginalStatus(status).getId(), activity).execute();
                }
                dismiss();
            }
        });
    }
}
