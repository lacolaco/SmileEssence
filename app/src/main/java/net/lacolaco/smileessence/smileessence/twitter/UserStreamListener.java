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

package net.lacolaco.smileessence.twitter;

import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.*;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.view.adapter.MessageListAdapter;
import net.lacolaco.smileessence.view.adapter.StatusListAdapter;
import net.lacolaco.smileessence.viewmodel.EnumEvent;
import net.lacolaco.smileessence.viewmodel.EventViewModel;
import net.lacolaco.smileessence.viewmodel.MessageViewModel;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.*;

public class UserStreamListener implements twitter4j.UserStreamListener, ConnectionLifeCycleListener
{

    // ------------------------------ FIELDS ------------------------------

    private final MainActivity activity;

    // --------------------------- CONSTRUCTORS ---------------------------

    public UserStreamListener(MainActivity activity)
    {
        this.activity = activity;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    private long getMyID()
    {
        return activity.getCurrentAccount().userID;
    }

    private int getPagerCount()
    {
        return activity.getPagerAdapter().getCount();
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface ConnectionLifeCycleListener ---------------------

    @Override
    public void onConnect()
    {
        activity.setStreaming(true);
        new Notificator(activity, R.string.notice_stream_connect).publish();
    }

    @Override
    public void onDisconnect()
    {
        activity.setStreaming(false);
        new Notificator(activity, R.string.notice_stream_disconnect).publish();
    }

    @Override
    public void onCleanUp()
    {
    }

    // --------------------- Interface StatusListener ---------------------

    @Override
    public void onStatus(Status status)
    {
        StatusCache.getInstance().put(status);
        if(isIgnoredStatus(status))
        {
            return;
        }
        StatusViewModel viewModel = new StatusViewModel(status, activity.getCurrentAccount());
        addToHome(viewModel);
        if(status.isRetweet())
        {
            if(viewModel.isRetweetOfMe())
            {
                addToHistory(new EventViewModel(EnumEvent.RETWEETED, status.getUser(), status));
            }
        }
        else if(viewModel.isMention())
        {
            addToMentions(viewModel);
            EventViewModel mentioned = new EventViewModel(EnumEvent.MENTIONED, status.getUser(), status);
            Notificator.publish(activity, mentioned.getFormattedString(activity));
        }
        StatusFilter.filter(activity, viewModel);
        FavoriteCache.getInstance().put(status);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice)
    {
        for(CustomListAdapter<?> adapter : activity.getListAdapters())
        {
            if(adapter != null && adapter instanceof StatusListAdapter)
            {
                StatusListAdapter statusListAdapter = (StatusListAdapter) adapter;
                statusListAdapter.removeByStatusID(statusDeletionNotice.getStatusId());
                statusListAdapter.updateForce();
            }
        }
        StatusCache.getInstance().remove(statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses)
    {
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId)
    {
    }

    @Override
    public void onStallWarning(StallWarning warning)
    {
    }

    // --------------------- Interface StreamListener ---------------------

    @Override
    public void onException(Exception ex)
    {
        net.lacolaco.smileessence.logging.Logger.error(ex.toString());
    }

    // --------------------- Interface UserStreamListener ---------------------

    @Override
    public void onDeletionNotice(long directMessageId, long userId)
    {
        MessageListAdapter messages = (MessageListAdapter) activity.getListAdapter(MainActivity.ADAPTER_MESSAGES);
        messages.removeByMessageID(directMessageId);
        messages.updateForce();
        DirectMessageCache.getInstance().remove(directMessageId);
    }

    @Override
    public void onFriendList(long[] friendIds)
    {
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus)
    {
        StatusCache.getInstance().put(favoritedStatus);
        if(isMe(target))
        {
            addToHistory(new EventViewModel(EnumEvent.FAVORITED, source, favoritedStatus));
        }
        if(isMe(source))
        {
            FavoriteCache.getInstance().put(favoritedStatus, true);
            activity.getListAdapter(MainActivity.ADAPTER_HOME).update();
            activity.getListAdapter(MainActivity.ADAPTER_MENTIONS).update();
        }
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus)
    {
        StatusCache.getInstance().put(unfavoritedStatus);
        boolean unfavNoticeEnabled = activity.getUserPreferenceHelper().getValue(R.string.key_setting_notify_on_unfavorited, true);
        if(isMe(target) && unfavNoticeEnabled)
        {
            addToHistory(new EventViewModel(EnumEvent.UNFAVORITED, source, unfavoritedStatus));
        }
        if(isMe(source))
        {
            FavoriteCache.getInstance().put(unfavoritedStatus, false);
            activity.getListAdapter(MainActivity.ADAPTER_HOME).update();
            activity.getListAdapter(MainActivity.ADAPTER_MENTIONS).update();
        }
    }

    @Override
    public void onFollow(User source, User followedUser)
    {
        UserCache.getInstance().put(source);
        UserCache.getInstance().put(followedUser);
        if(isMe(followedUser))
        {
            addToHistory(new EventViewModel(EnumEvent.FOLLOWED, source));
        }
    }

    @Override
    public void onUnfollow(User source, User unfollowedUser)
    {
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage)
    {
        DirectMessageCache.getInstance().put(directMessage);
        if(isMe(directMessage.getRecipient()))
        {
            addToHistory(new EventViewModel(EnumEvent.RECEIVE_MESSAGE, directMessage.getSender()));
        }
        MessageViewModel message = new MessageViewModel(directMessage, activity.getCurrentAccount());
        addToMessages(message);
    }

    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list)
    {
    }

    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list)
    {
    }

    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list)
    {
        UserListCache.getInstance().put(list.getFullName());
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list)
    {
        UserListCache.getInstance().remove(list.getFullName());
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list)
    {
        UserListCache.getInstance().put(list.getFullName());
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list)
    {
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list)
    {
        UserListCache.getInstance().remove(list.getFullName());
    }

    @Override
    public void onUserProfileUpdate(User updatedUser)
    {
    }

    @Override
    public void onBlock(User source, User blockedUser)
    {
        if(isMe(blockedUser))
        {
            addToHistory(new EventViewModel(EnumEvent.BLOCKED, source));
        }
    }

    @Override
    public void onUnblock(User source, User unblockedUser)
    {
        if(isMe(unblockedUser))
        {
            addToHistory(new EventViewModel(EnumEvent.UNBLOCKED, source));
        }
    }

    private void addToHistory(EventViewModel mentioned)
    {
        CustomListAdapter<?> history = activity.getListAdapter(MainActivity.ADAPTER_HISTORY);
        Notificator.publish(activity, mentioned.getFormattedString(activity));
        history.addToTop(mentioned);
        history.update();
    }

    private void addToHome(StatusViewModel viewModel)
    {
        StatusListAdapter home = (StatusListAdapter) activity.getListAdapter(MainActivity.ADAPTER_HOME);
        home.addToTop(viewModel);
        home.update();
    }

    private void addToMentions(StatusViewModel viewModel)
    {
        StatusListAdapter mentions = (StatusListAdapter) activity.getListAdapter(MainActivity.ADAPTER_MENTIONS);
        mentions.addToTop(viewModel);
        mentions.update();
    }

    private void addToMessages(MessageViewModel message)
    {
        MessageListAdapter messages = (MessageListAdapter) activity.getListAdapter(MainActivity.ADAPTER_MESSAGES);
        messages.addToTop(message);
        messages.update();
    }

    private boolean isIgnoredStatus(Status status)
    {
        return status.isRetweet() && StatusCache.getInstance().isIgnored(status.getRetweetedStatus().getId());
    }

    private boolean isMe(User user)
    {
        return user.getId() == getMyID();
    }
}
