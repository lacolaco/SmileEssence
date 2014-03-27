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

import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.StatusCache;
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

    private final MainActivity activity;

    public UserStreamListener(MainActivity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onConnect()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDisconnect()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCleanUp()
    {
    }

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
        if(activity.getCurrentAccount().userID == target.getId())
        {
            EventViewModel event = new EventViewModel(EnumEvent.FAVORITED, source, favoritedStatus);
            CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
            new Notificator(activity, event.getFormattedString(activity)).publish();
            history.addToTop(event);
        }
    }

    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus)
    {
        if(activity.getCurrentAccount().userID == target.getId())
        {
            EventViewModel event = new EventViewModel(EnumEvent.UNFAVORITED, source, unfavoritedStatus);
            CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
            new Notificator(activity, event.getFormattedString(activity)).publish();
            history.addToTop(event);
        }
    }

    @Override
    public void onFollow(User source, User followedUser)
    {
        if(activity.getCurrentAccount().userID == followedUser.getId())
        {
            EventViewModel event = new EventViewModel(EnumEvent.FOLLOWED, source);
            CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
            new Notificator(activity, event.getFormattedString(activity)).publish();
            history.addToTop(event);
        }
    }

    @Override
    public void onUnfollow(User source, User unfollowedUser)
    {
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage)
    {
        EventViewModel event = new EventViewModel(EnumEvent.RECEIVE_MESSAGE, directMessage.getSender());
        CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
        new Notificator(activity, event.getFormattedString(activity)).publish();
        history.addToTop(event);
        MessageViewModel message = new MessageViewModel(directMessage);
        CustomListAdapter<?> messages = activity.getListAdapter(MainActivity.PAGE_MESSAGES);
        messages.addToTop(message);
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
    }

    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list)
    {
    }

    @Override
    public void onUserListCreation(User listOwner, UserList list)
    {
    }

    @Override
    public void onUserListUpdate(User listOwner, UserList list)
    {
    }

    @Override
    public void onUserListDeletion(User listOwner, UserList list)
    {
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
            EventViewModel event = new EventViewModel(EnumEvent.BLOCKED, source);
            CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
            new Notificator(activity, event.getFormattedString(activity)).publish();
            history.addToTop(event);
        }
    }

    @Override
    public void onUnblock(User source, User unblockedUser)
    {
        if(activity.getCurrentAccount().userID == unblockedUser.getId())
        {
            EventViewModel event = new EventViewModel(EnumEvent.UNBLOCKED, source);
            CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
            new Notificator(activity, event.getFormattedString(activity)).publish();
            history.addToTop(event);
        }
    }

    @Override
    public void onStatus(Status status)
    {
        StatusCache.getInstance().put(status);
        if(status.isRetweet())
        {
            StatusCache.getInstance().put(status.getRetweetedStatus());
        }
        CustomListAdapter<?> home = activity.getListAdapter(MainActivity.PAGE_HOME);
        StatusViewModel viewModel = new StatusViewModel(status);
        home.addToTop(viewModel);
        if(status.isRetweet())
        {
            if(viewModel.isRetweetOfMe(activity.getCurrentAccount().userID))
            {
                viewModel.setRetweetOfMe(true);
                CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
                EventViewModel retweeted = new EventViewModel(EnumEvent.RETWEETED, status.getUser(), status);
                new Notificator(activity, retweeted.getFormattedString(activity)).publish();
                history.addToTop(retweeted);
            }
        }
        else if(viewModel.isMention(activity.getCurrentAccount().screenName))
        {
            viewModel.setMention(true);
            CustomListAdapter<?> mentions = activity.getListAdapter(MainActivity.PAGE_MENTIONS);
            mentions.addToTop(viewModel);
            CustomListAdapter<?> history = activity.getListAdapter(MainActivity.PAGE_HISTORY);
            EventViewModel mentioned = new EventViewModel(EnumEvent.MENTIONED, status.getUser(), status);
            new Notificator(activity, mentioned.getFormattedString(activity)).publish();
            history.addToTop(mentioned);
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice)
    {
        for(int i = 0; i < activity.getPageCount(); i++)
        {
            CustomListAdapter adapter = activity.getListAdapter(i);
            if(adapter != null && adapter instanceof StatusListAdapter)
            {
                StatusListAdapter statusListAdapter = (StatusListAdapter) adapter;
                statusListAdapter.removeByStatusID(statusDeletionNotice.getStatusId());
            }
        }
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

    @Override
    public void onException(Exception ex)
    {
    }
}
