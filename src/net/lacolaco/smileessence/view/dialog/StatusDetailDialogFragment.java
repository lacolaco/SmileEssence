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
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TweetBuilder;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.DeleteStatusTask;
import net.lacolaco.smileessence.twitter.task.FavoriteTask;
import net.lacolaco.smileessence.twitter.task.RetweetTask;
import net.lacolaco.smileessence.twitter.task.UnfavoriteTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.PostState;
import net.lacolaco.smileessence.view.adapter.StatusListAdapter;
import net.lacolaco.smileessence.view.listener.ListItemClickListener;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;

import java.util.ArrayList;

public class StatusDetailDialogFragment extends DialogFragment implements View.OnClickListener
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
            public void success(Status status)
            {
                switch(v.getId())
                {
                    case R.id.button_status_detail_reply:
                    {
                        replyToStatus(activity, account, status);
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
                    case R.id.button_status_detail_menu:
                    {
                        openMenu(activity);
                        break;
                    }
                    default:
                    {
                        dismiss();
                    }
                }
            }

            @Override
            public void error()
            {

            }
        });
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account account = activity.getCurrentAccount();

        Status status = StatusCache.getInstance().get(getStatusID());
        View header = getTitleView(activity, account, status);
        ListView listView = (ListView) header.findViewById(R.id.listview_status_detail_reply_to);
        final StatusListAdapter adapter = new StatusListAdapter(getActivity());
        listView.setAdapter(adapter);
        long inReplyToStatusId = TwitterUtils.getOriginalStatus(status).getInReplyToStatusId();
        if(inReplyToStatusId == -1)
        {
            listView.setVisibility(View.GONE);
        }
        else
        {
            TwitterUtils.tryGetStatus(account, inReplyToStatusId, new TwitterUtils.StatusCallback()
            {
                @Override
                public void success(Status status)
                {
                    adapter.addToTop(new StatusViewModel(status, account));
                    adapter.updateForce();
                }

                @Override
                public void error()
                {

                }
            });
        }
        return new AlertDialog.Builder(getActivity()).setView(header).create();
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

    private View getTitleView(MainActivity activity, Account account, Status status)
    {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_status_detail, null);
        View statusHeader = view.findViewById(R.id.layout_status_header);
        StatusViewModel statusViewModel = new StatusViewModel(status, account);
        statusHeader = statusViewModel.getView(activity, activity.getLayoutInflater(), statusHeader);
        statusHeader.setClickable(false);
        int background = ((ColorDrawable) statusHeader.getBackground()).getColor();
        view.setBackgroundColor(background);
        ImageView favCountIcon = (ImageView) view.findViewById(R.id.image_status_detail_fav_count);
        ImageView rtCountIcon = (ImageView) view.findViewById(R.id.image_status_detail_rt_count);
        TextView favCountText = (TextView) view.findViewById(R.id.textview_status_detail_fav_count);
        TextView rtCountText = (TextView) view.findViewById(R.id.textview_status_detail_rt_count);
        int favoriteCount = TwitterUtils.getOriginalStatus(status).getFavoriteCount();
        if(favoriteCount == 0)
        {
            favCountIcon.setVisibility(View.GONE);
            favCountText.setVisibility(View.GONE);
        }
        else
        {
            favCountText.setText(Integer.toString(favoriteCount));
        }
        int retweetCount = TwitterUtils.getOriginalStatus(status).getRetweetCount();
        if(retweetCount == 0)
        {
            rtCountIcon.setVisibility(View.GONE);
            rtCountText.setVisibility(View.GONE);
        }
        else
        {
            rtCountText.setText(Integer.toString(retweetCount));
        }
        ImageButton menu = (ImageButton) view.findViewById(R.id.button_status_detail_menu);
        ImageButton message = (ImageButton) view.findViewById(R.id.button_status_detail_reply);
        ImageButton retweet = (ImageButton) view.findViewById(R.id.button_status_detail_retweet);
        ImageButton favorite = (ImageButton) view.findViewById(R.id.button_status_detail_favorite);
        ImageButton delete = (ImageButton) view.findViewById(R.id.button_status_detail_delete);
        menu.setOnClickListener(this);
        message.setOnClickListener(this);
        retweet.setOnClickListener(this);
        favorite.setOnClickListener(this);
        delete.setOnClickListener(this);
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
        favorite.setTag(statusViewModel.isFavorited());
        if(statusViewModel.isFavorited())
        {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.icon_favorite_on));
        }
        boolean deletable = isDeletable(account, status);
        delete.setVisibility(deletable ? View.VISIBLE : View.GONE);
        LinearLayout commandsLayout = (LinearLayout) view.findViewById(R.id.linearlayout_status_detail_menu);
        commandsLayout.setClickable(true);
        ArrayList<Command> commands = getCommands(activity, status, account);
        Command.filter(commands);
        for(final Command command : commands)
        {
            View commandView = command.getView(activity, activity.getLayoutInflater(), null);
            commandView.setBackgroundColor(getResources().getColor(R.color.transparent));
            commandView.setOnClickListener(new ListItemClickListener(activity, new Runnable()
            {
                @Override
                public void run()
                {
                    command.execute();
                    dismiss();
                }
            }));
            commandsLayout.addView(commandView);
        }
        return view;
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

    private ArrayList<Command> getCommands(Activity activity, Status status, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        // URL
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
        boolean deletable;
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
        return user.isProtected() || user.getId() == account.userID;
    }

    private boolean isRetweetDeletable(Account account, Status status)
    {
        return status.isRetweet() && status.getUser().getId() == account.userID;
    }

    private void openMenu(MainActivity activity)
    {
        StatusMenuDialogFragment fragment = new StatusMenuDialogFragment();
        fragment.setStatusID(getStatusID());
        DialogHelper.showDialog(activity, fragment, "statusMenuDialog");
    }

    private void replyToStatus(MainActivity activity, Account account, Status status)
    {
        Status originalStatus = TwitterUtils.getOriginalStatus(status);
        TweetBuilder builder = new TweetBuilder();
        if(account.userID == originalStatus.getUser().getId())
        {
            builder.addScreenName(account.screenName);
        }
        builder.addScreenNames(TwitterUtils.getScreenNames(originalStatus, account.screenName));

        String text = builder.buildText();
        int selStart = originalStatus.getUser().getScreenName().length() + 2; // "@" and " "

        PostState.newState().beginTransaction()
                 .insertText(0, text)
                 .setInReplyToStatusID(originalStatus.getId())
                 .setSelection(selStart, text.length())
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
