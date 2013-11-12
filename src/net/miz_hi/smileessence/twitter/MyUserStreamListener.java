package net.miz_hi.smileessence.twitter;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.cache.TweetCache;
import net.miz_hi.smileessence.extraction.ExtractManager;
import net.miz_hi.smileessence.model.status.ResponseConverter;
import net.miz_hi.smileessence.model.status.event.EventModel;
import net.miz_hi.smileessence.model.status.event.impl.*;
import net.miz_hi.smileessence.model.status.tweet.EnumTweetType;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.model.statuslist.StatusList;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.talkchase.TalkManager;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.activity.MainActivity;
import twitter4j.*;

public class MyUserStreamListener implements UserStreamListener, ConnectionLifeCycleListener
{

    private int exceptionCount;

    public MyUserStreamListener()
    {
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice arg0)
    {
        LogHelper.d("on status delete");
        final TweetModel model = TweetCache.get(arg0.getStatusId());
        if (model != null)
        {
            for (StatusList list : StatusListManager.getTweetLists())
            {
                list.remove(model);
                StatusListManager.getAdapter(list).forceNotifyAdapter();
            }
        }
    }

    @Override
    public void onScrubGeo(long arg0, long arg1)
    {
    }

    @Override
    public void onStallWarning(StallWarning arg0)
    {
    }

    @Override
    public void onStatus(Status status)
    {
        if (MainActivity.getInstance() == null || MainActivity.getInstance().isFinishing())
        {
            return;
        }
        TweetModel model = ResponseConverter.convert(status);

        if (model.type == EnumTweetType.RETWEET && model.user.isMe())
        {
            EventModel event = new RetweetEvent(model.retweeter, model);
            StatusList history = StatusListManager.getHistoryTimeline();
            history.addToTop(event);
            history.apply();
            event.raise();
        }
        else if (model.type == EnumTweetType.REPLY)
        {
            StatusList mentions = StatusListManager.getMentionsTimeline();
            mentions.addToTop(model);
            mentions.apply();
            Notificator.buildEvent(new ReplyEvent(model.user, model)).raise();
        }
        if (Client.<Boolean>getPreferenceValue(EnumPreferenceKey.SHOW_READ_RETWEET) || TweetCache.isNotRead(model.statusId))
        {
            StatusList home = StatusListManager.getHomeTimeline();
            home.addToTop(model);
            home.apply();
        }

        TalkManager.check(model);
        ExtractManager.check(model);
    }

    @Override
    public void onTrackLimitationNotice(int arg0)
    {
    }

    @Override
    public void onException(Exception arg0)
    {
        if (exceptionCount > 0)
        {
            exceptionCount = 1;
            arg0.printStackTrace();
            Notificator.alert("切断されました");
        }
    }

    @Override
    public void onBlock(User sourceUser, User targetUser)
    {
        if (targetUser.getId() == Client.getMainAccount().getUserId())
        {
            EventModel event = new BlockEvent(ResponseConverter.<UserModel>convert(sourceUser));
            StatusList history = StatusListManager.getHistoryTimeline();
            history.addToTop(event);
            history.apply();
            event.raise();
        }
    }

    @Override
    public void onDeletionNotice(long arg0, long arg1)
    {
    }

    @Override
    public void onDirectMessage(DirectMessage message)
    {
        if (message.getRecipientId() == Client.getMainAccount().getUserId())
        {
            EventModel event = new DirectMessageEvent(ResponseConverter.<UserModel>convert(message.getSender()));
            StatusList history = StatusListManager.getHistoryTimeline();
            history.addToTop(event);
            history.apply();
            event.raise();
        }
    }

    @Override
    public void onFavorite(User sourceUser, User targetUser, Status targetStatus)
    {
        if (sourceUser.getId() == Client.getMainAccount().getUserId())
        {
            if (targetStatus.isRetweet())
            {
                TweetCache.putFavoritedStatus(targetStatus.getRetweetedStatus().getId());
            }
            else
            {
                TweetCache.putFavoritedStatus(targetStatus.getId());
            }
            StatusList home = StatusListManager.getHomeTimeline();
            StatusList mentions = StatusListManager.getMentionsTimeline();
            home.apply();
            mentions.apply();
        }
        if (targetUser.getId() == Client.getMainAccount().getUserId())
        {
            EventModel event = new FavoriteEvent(ResponseConverter.<UserModel>convert(sourceUser), ResponseConverter.<TweetModel>convert(targetStatus));
            StatusList history = StatusListManager.getHistoryTimeline();
            history.addToTop(event);
            history.apply();
            event.raise();
        }
    }

    @Override
    public void onFollow(User sourceUser, User targetUser)
    {
        if (sourceUser.getId() != Client.getMainAccount().getUserId())
        {
            EventModel event = new FollowEvent(ResponseConverter.<UserModel>convert(sourceUser));
            StatusList history = StatusListManager.getHistoryTimeline();
            history.addToTop(event);
            history.apply();
            event.raise();
        }
    }

    @Override
    public void onFriendList(long[] arg0)
    {
    }

    @Override
    public void onUnblock(User sourceUser, User targetUser)
    {
        if (targetUser.getId() == Client.getMainAccount().getUserId())
        {
            EventModel event = new UnblockEvent(ResponseConverter.<UserModel>convert(sourceUser));
            StatusList history = StatusListManager.getHistoryTimeline();
            history.addToTop(event);
            history.apply();
            event.raise();
        }
    }

    @Override
    public void onUnfavorite(User sourceUser, User targetUser, Status targetStatus)
    {
        if (sourceUser.getId() == Client.getMainAccount().getUserId())
        {
            if (targetStatus.isRetweet())
            {
                TweetCache.removeFavoritedStatus(targetStatus.getRetweetedStatus().getId());
            }
            else
            {
                TweetCache.removeFavoritedStatus(targetStatus.getId());
            }
            StatusList home = StatusListManager.getHomeTimeline();
            StatusList mentions = StatusListManager.getMentionsTimeline();
            home.apply();
            mentions.apply();
        }

        if (Client.<Boolean>getPreferenceValue(EnumPreferenceKey.NOTICE_UNFAV))
        {
            if (targetUser.getId() == Client.getMainAccount().getUserId())
            {
                EventModel event = new UnfavoriteEvent(ResponseConverter.<UserModel>convert(sourceUser), ResponseConverter.<TweetModel>convert(targetStatus));
                StatusList history = StatusListManager.getHistoryTimeline();
                history.addToTop(event);
                history.apply();
                event.raise();
            }
        }
    }

    @Override
    public void onUserListCreation(User arg0, UserList arg1)
    {
    }

    @Override
    public void onUserListDeletion(User arg0, UserList arg1)
    {
    }

    @Override
    public void onUserListMemberAddition(User arg0, User arg1, UserList arg2)
    {
    }

    @Override
    public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2)
    {
    }

    @Override
    public void onUserListSubscription(User arg0, User arg1, UserList arg2)
    {
    }

    @Override
    public void onUserListUnsubscription(User arg0, User arg1, UserList arg2)
    {
    }

    @Override
    public void onUserListUpdate(User arg0, UserList arg1)
    {
    }

    @Override
    public void onUserProfileUpdate(User arg0)
    {
    }

    @Override
    public void onCleanUp()
    {
    }

    @Override
    public void onConnect()
    {
        Notificator.info("接続しました");
        exceptionCount = 0;
    }

    @Override
    public void onDisconnect()
    {
    }

}
