package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.event.HistoryListAdapter;
import net.miz_hi.smileessence.event.StatusEventModel;
import net.miz_hi.smileessence.event.StatusEventModel.EnumStatusEventType;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.event.UserEventModel.EnumUserEventType;
import net.miz_hi.smileessence.event.UserEventModel;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.RelationListPageFragment;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import android.widget.ListView;
import android.widget.Toast;

public class MyUserStreamListener implements UserStreamListener, ConnectionLifeCycleListener
{
	private StatusListAdapter homeListAdapter;
	private StatusListAdapter mentionsListAdapter;
	private HistoryListAdapter eventListAdapter;
	private StatusListAdapter relationListAdapter;
	
	private int exceptionCount;

	public MyUserStreamListener()
	{
	}

	public void setHomeListAdapter(StatusListAdapter adapter)
	{
		this.homeListAdapter = adapter;
	}

	public void setMentionsListAdapter(StatusListAdapter adapter)
	{
		this.mentionsListAdapter = adapter;
	}

	public void setEventListAdapter(HistoryListAdapter adapter)
	{
		this.eventListAdapter = adapter;
	}
	
	public void setRelationListAdapter(StatusListAdapter adapter)
	{
		this.relationListAdapter = adapter;
	}

	@Override
	public void onDeletionNotice(final StatusDeletionNotice arg0)
	{
		LogHelper.printD("on status detete");
		final StatusModel model = StatusStore.get(arg0.getStatusId());
		if (model == null)
		{
			return;
		}
		else
		{
			homeListAdapter.removeElement(model);
			homeListAdapter.notifyAdapter();
			mentionsListAdapter.removeElement(model);
			mentionsListAdapter.notifyAdapter();
			relationListAdapter.removeElement(model);
			relationListAdapter.notifyAdapter();
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
	public void onStatus(final Status status)
	{
		if(MainActivity.getInstance() == null || MainActivity.getInstance().isFinishing())
		{
			return;
		}
		final StatusModel model = StatusStore.put(status);

		if (model.isRetweet && model.isMine)
		{
			eventListAdapter.addFirst(new StatusEventModel(status.getUser(), EnumStatusEventType.RETWEET, status));
		}
		else if (model.isReply)
		{
			eventListAdapter.notice(new StatusEventModel(status.getUser(), EnumStatusEventType.REPLY, status));
		}

		if (model.isReply)
		{
			mentionsListAdapter.addFirst(model);
			mentionsListAdapter.notifyAdapter();
		}
		if(RelationListPageFragment.getChasingId() > -1 && model.inReplyToStatusId == RelationListPageFragment.getChasingId())
		{
			relationListAdapter.addFirst(model);
			relationListAdapter.notifyAdapter();
			RelationListPageFragment.setChasingId(model.statusId);
		}
		homeListAdapter.addFirst(model);				
		homeListAdapter.notifyAdapter();

	}

	@Override
	public void onTrackLimitationNotice(int arg0)
	{
	}

	@Override
	public void onException(Exception arg0)
	{
		if(exceptionCount++ > 3)
		{
			return;
		}
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Toast.makeText(MainActivity.getInstance(), "ê⁄ë±Ç™êÿÇÍÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();				
			}
		}.post();
		MainActivity.getInstance().connectUserStream();
	}

	@Override
	public void onBlock(final User sourceUser, User targetUser)
	{
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					eventListAdapter.addFirst(new UserEventModel(sourceUser, EnumUserEventType.BLOCK));
					eventListAdapter.notifyAdapter();
				}
			}.post();			
		}
	}

	@Override
	public void onDeletionNotice(long arg0, long arg1)
	{
	}

	@Override
	public void onDirectMessage(final DirectMessage message)
	{
		if (message.getRecipientId() == Client.getMainAccount().getUserId())
		{
			eventListAdapter.addFirst(new UserEventModel(message.getSender(), EnumUserEventType.DIRECT_MESSAGE));					
		}
	}

	@Override
	public void onFavorite(final User sourceUser, User targetUser, final Status targetStatus)
	{
		if(sourceUser.getId() == Client.getMainAccount().getUserId())
		{
			if(targetStatus.isRetweet())
			{
				StatusStore.putFavoritedStatus(targetStatus.getRetweetedStatus().getId());				
			}
			else
			{
				StatusStore.putFavoritedStatus(targetStatus.getId());
			}			

			MainActivity.getInstance().getHomeListAdapter().forceNotifyAdapter();
			MainActivity.getInstance().getMentionsListAdapter().forceNotifyAdapter();
		}
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			eventListAdapter.addFirst(new StatusEventModel(sourceUser, EnumStatusEventType.FAVORITE, targetStatus));
		}
	}

	@Override
	public void onFollow(final User sourceUser, User targetUser)
	{
		eventListAdapter.addFirst(new UserEventModel(sourceUser, EnumUserEventType.FOLLOW));	
	}

	@Override
	public void onFriendList(long[] arg0)
	{
	}

	@Override
	public void onUnblock(final User sourceUser, User targetUser)
	{
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			eventListAdapter.addFirst(new UserEventModel(sourceUser, EnumUserEventType.UNBLOCK));					
		}
	}

	@Override
	public void onUnfavorite(final User sourceUser, User targetUser, final Status targetStatus)
	{
		if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.NOTICE_UNFAV))
		{
			if (targetUser.getId() == Client.getMainAccount().getUserId())
			{
				eventListAdapter.addFirst(new StatusEventModel(sourceUser, EnumStatusEventType.UNFAVORITE, targetStatus));
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
		ToastManager.getInstance().toast("ê⁄ë±ÇµÇ‹ÇµÇΩ");
	}

	@Override
	public void onDisconnect()
	{
	}

}
