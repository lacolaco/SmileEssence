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
import net.lacolaco.smileessence.data.FavoriteCache;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.data.UserListCache;
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
    }

    private boolean isIgnoredStatus(Status status)
    {
        return status.isRetweet() && StatusCache.getInstance().isIgnored(status.getRetweetedStatus().getId());
    }

    private void addToHome(StatusViewModel viewModel)
    {
        StatusListAdapter home = (StatusListAdapter) activity.getListAdapter(MainActivity.PAGE_HOME);
        home.addToTop(viewModel);
        home.update();
    }

    private void addToHistory(EventViewModel mentioned)
    {
        CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
        Notificator.publish(activity, mentioned.getFormattedString(activity));
        history.addToTop(mentioned);
        history.update();
    }

    private void addToMentions(StatusViewModel viewModel)
    {
        StatusListAdapter mentions = (StatusListAdapter) activity.getListAdapter(MainActivity.PAGE_MENTIONS);
        mentions.addToTop(viewModel);
        mentions.update();
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice)
    {
        for(int i = 0; i < getPagerCount(); i++)
        {
            CustomListAdapter adapter = activity.getListAdapter(i);
            if(adapter != null && adapter instanceof StatusListAdapter)
            {
                StatusListAdapter statusListAdapter = (StatusListAdapter) adapter;
                statusListAdapter.removeByStatusID(statusDeletionNotice.getStatusId());
            }
        }
    }

    private int getPagerCount()
    {
        return activity.getPagerAdapter().getCount();
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
        MessageListAdapter messages = (MessageListAdapter) activity.getListAdapter(MainActivity.PAGE_MESSAGES);
        messages.removeByStatusID(directMessageId);
    }

    @Override
    public void onFriendList(long[] friendIds)
    {
    }

    @Override
    public void onFavorite(User source, User target, Status favoritedStatus)
    {
        long myID = activity.getCurrentAccount().userID;
        if(myID == target.getId())
        {
            addToHistory(new EventViewModel(EnumEvent.FAVORITED, source, favoritedStatus));
        }
        else if(myID == source.getId())
        {
            FavoriteCache.getInstance().put(favoritedStatus, true);
            activity.getListAdapter(MainActivity.PAGE_HOME).update();
            activity.getListAdapter(MainActivity.PAGE_MENTIONS).update();
        }
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus)
    {
        long myID = activity.getCurrentAccount().userID;
        if(myID == target.getId())
        {
            addToHistory(new EventViewModel(EnumEvent.UNFAVORITED, source, unfavoritedStatus));
        }
        else if(myID == source.getId())
        {
            FavoriteCache.getInstance().put(unfavoritedStatus, false);
            activity.getListAdapter(MainActivity.PAGE_HOME).update();
            activity.getListAdapter(MainActivity.PAGE_MENTIONS).update();
        }
    }

    @Override
    public void onFollow(User source, User followedUser)
    {
        if(activity.getCurrentAccount().userID == followedUser.getId())
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
        addToHistory(new EventViewModel(EnumEvent.RECEIVE_MESSAGE, directMessage.getSender()));
        MessageViewModel message = new MessageViewModel(directMessage, activity.getCurrentAccount());
        CustomListAdapter<?> messages = activity.getListAdapter(MainActivity.PAGE_MESSAGES);
        messages.addToTop(message);
        messages.update();
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
        if(activity.getCurrentAccount().userID == blockedUser.getId())
        {
            addToHistory(new EventViewModel(EnumEvent.BLOCKED, source));
        }
    }

    @Override
    public void onUnblock(User source, User unblockedUser)
    {
        if(activity.getCurrentAccount().userID == unblockedUser.getId())
        {
            addToHistory(new EventViewModel(EnumEvent.UNBLOCKED, source));
        }
    }
}
