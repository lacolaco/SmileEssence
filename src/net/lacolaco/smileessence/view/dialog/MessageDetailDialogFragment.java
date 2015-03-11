/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2015 lacolaco.net
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.google.common.collect.Lists;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.data.DirectMessageCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.DeleteMessageTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.view.adapter.MessageListAdapter;
import net.lacolaco.smileessence.view.listener.ListItemClickListener;
import net.lacolaco.smileessence.viewmodel.MessageViewModel;
import twitter4j.DirectMessage;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MessageDetailDialogFragment extends DialogFragment implements View.OnClickListener
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

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnClickListener ---------------------

    @Override
    public void onClick(final View v)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account account = activity.getCurrentAccount();
        TwitterUtils.tryGetMessage(account, getMessageID(), new TwitterUtils.MessageCallback()
        {
            @Override
            public void success(DirectMessage message)
            {
                switch(v.getId())
                {
                    case R.id.button_status_detail_reply:
                    {
                        openSendMessageDialog(message);
                        break;
                    }
                    case R.id.button_status_detail_delete:
                    {
                        deleteMessage(account, message);
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
        MainActivity activity = (MainActivity) getActivity();
        final Account account = activity.getCurrentAccount();

        DirectMessage selectedMessage = DirectMessageCache.getInstance().get(getMessageID());
        if(selectedMessage == null)
        {
            Notificator.publish(getActivity(), R.string.notice_error_get_messages);
            return new DisposeDialog(getActivity());
        }
        View header = getTitleView(activity, account, selectedMessage);
        ListView listView = (ListView) header.findViewById(R.id.listview_status_detail_reply_to);
        final MessageListAdapter adapter = new MessageListAdapter(getActivity());
        listView.setAdapter(adapter);
        long replyToMessageId = -1;
        ArrayList<DirectMessage> allMessages = Lists.newArrayList(DirectMessageCache.getInstance().all());
        Collections.sort(allMessages, new Comparator<DirectMessage>()
        {
            @Override
            public int compare(DirectMessage lhs, DirectMessage rhs)
            {
                return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
            }
        });
        for(DirectMessage directMessage : allMessages)
        {
            if(selectedMessage.getId() == directMessage.getId())
            {
                continue;
            }
            if(directMessage.getCreatedAt().getTime() > selectedMessage.getCreatedAt().getTime())
            {
                continue;
            }
            if(directMessage.getSenderId() == selectedMessage.getRecipientId() && directMessage.getRecipientId() == selectedMessage.getSenderId())
            {
                replyToMessageId = directMessage.getId();
                break;
            }
        }

        if(replyToMessageId == -1)
        {
            listView.setVisibility(View.GONE);
        }
        else
        {
            TwitterUtils.tryGetMessage(account, replyToMessageId, new TwitterUtils.MessageCallback()
            {
                @Override
                public void success(DirectMessage message)
                {
                    adapter.addToTop(new MessageViewModel(message, account));
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

    // -------------------------- OTHER METHODS --------------------------

    public void deleteMessage(final Account account, final DirectMessage message)
    {
        ConfirmDialogFragment.show(getActivity(), getString(R.string.dialog_confirm_commands), new Runnable()
        {
            @Override
            public void run()
            {
                new DeleteMessageTask(new TwitterApi(account).getTwitter(), message.getId(), getActivity()).execute();
                dismiss();
            }
        });
    }

    public void openSendMessageDialog(DirectMessage message)
    {
        SendMessageDialogFragment dialogFragment = new SendMessageDialogFragment();
        dialogFragment.setScreenName(message.getSenderScreenName());
        DialogHelper.showDialog(getActivity(), dialogFragment);
    }

    private ArrayList<Command> getCommands(Activity activity, DirectMessage message, Account account)
    {
        ArrayList<Command> commands = new ArrayList<>();
        // URL
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
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_status_detail, null);
        View messageHeader = view.findViewById(R.id.layout_status_header);
        MessageViewModel statusViewModel = new MessageViewModel(message, account);
        messageHeader = statusViewModel.getView(activity, activity.getLayoutInflater(), messageHeader);
        messageHeader.setClickable(false);
        int background = ((ColorDrawable) messageHeader.getBackground()).getColor();
        view.setBackgroundColor(background);
        ImageButton reply = (ImageButton) view.findViewById(R.id.button_status_detail_reply);
        reply.setOnClickListener(this);
        ImageButton delete = (ImageButton) view.findViewById(R.id.button_status_detail_delete);
        delete.setVisibility(isDeletable(account, message) ? View.VISIBLE : View.GONE);
        delete.setOnClickListener(this);
        ImageButton menuButton = (ImageButton) view.findViewById(R.id.button_status_detail_menu);
        menuButton.setOnClickListener(this);
        LinearLayout commandsLayout = (LinearLayout) view.findViewById(R.id.linearlayout_status_detail_menu);
        commandsLayout.setClickable(true);
        // commands
        ArrayList<Command> commands = getCommands(activity, message, account);
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
        // status only parts
        view.findViewById(R.id.button_status_detail_retweet).setVisibility(View.GONE);
        view.findViewById(R.id.button_status_detail_favorite).setVisibility(View.GONE);
        view.findViewById(R.id.image_status_detail_fav_count).setVisibility(View.GONE);
        view.findViewById(R.id.image_status_detail_rt_count).setVisibility(View.GONE);
        return view;
    }

    private boolean isDeletable(Account account, DirectMessage message)
    {
        return message.getSenderId() == account.userID;
    }

    private void openMenu(MainActivity activity)
    {
        MessageMenuDialogFragment fragment = new MessageMenuDialogFragment();
        fragment.setMessageID(getMessageID());
        DialogHelper.showDialog(activity, fragment, "messageMenuDialog");
    }
}
