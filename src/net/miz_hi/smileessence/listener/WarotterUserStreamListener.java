package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.event.HistoryListAdapter;
import net.miz_hi.smileessence.event.StatusEventModel;
import net.miz_hi.smileessence.event.StatusEventModel.EnumStatusEventType;
import net.miz_hi.smileessence.event.UserEventModel.EnumUserEventType;
import net.miz_hi.smileessence.event.UserEventModel;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.MainActivity;
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

public class WarotterUserStreamListener implements UserStreamListener, ConnectionLifeCycleListener
{
	private StatusListAdapter homeListAdapter;
	private StatusListAdapter mentionsListAdapter;
	private HistoryListAdapter eventListAdapter;
	
	private int exceptionCount;

	public WarotterUserStreamListener()
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
			new UiHandler()
			{

				@Override
				public void run()
				{
					homeListAdapter.removeElement(model);
					homeListAdapter.notifyAdapter();
					mentionsListAdapter.removeElement(model);
					mentionsListAdapter.notifyAdapter();
				}
			}.post();
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
		new UiHandler()
		{
			@Override
			public void run()
			{
				if (model.isRetweet && model.isMine)
				{
					eventListAdapter.addFirst(new StatusEventModel(status.getUser(), EnumStatusEventType.RETWEET, status));
				}
				else if (model.isReply)
				{
					eventListAdapter.addFirst(new StatusEventModel(status.getUser(), EnumStatusEventType.REPLY, status));
				}

				ListView homeListView = MainActivity.getInstance().getHomeListView();
				ListView mentionsListView = MainActivity.getInstance().getMentionsListView();

				if (model.isReply)
				{
					mentionsListAdapter.addFirst(model);
					if (mentionsListView.getFirstVisiblePosition() == 0 && mentionsListView.getChildAt(0) != null && mentionsListView.getChildAt(0).getTop() == 0)
					{
						mentionsListAdapter.setCanNotifyOnChange(true);
					}
					else
					{
						mentionsListAdapter.setCanNotifyOnChange(false);
					}
					mentionsListAdapter.notifyAdapter();
				}
				homeListAdapter.addFirst(model);				
				homeListAdapter.notifyAdapter();
				
			}
		}.post();
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
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					eventListAdapter.addFirst(new UserEventModel(message.getSender(), EnumUserEventType.DIRECT_MESSAGE));					
				}
			}.post();
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
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					MainActivity.getInstance().getHomeListView().invalidateViews();
					MainActivity.getInstance().getMentionsListView().invalidateViews();
				}
			}.post();
		}
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					eventListAdapter.addFirst(new StatusEventModel(sourceUser, EnumStatusEventType.FAVORITE, targetStatus));
				}
			}.post();
		}
	}

	@Override
	public void onFollow(final User sourceUser, User targetUser)
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				eventListAdapter.addFirst(new UserEventModel(sourceUser, EnumUserEventType.FOLLOW));	
			}
		}.post();
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
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					eventListAdapter.addFirst(new UserEventModel(sourceUser, EnumUserEventType.UNBLOCK));					
				}
			}.post();
		}
	}

	@Override
	public void onUnfavorite(final User sourceUser, User targetUser, final Status targetStatus)
	{
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			new UiHandler()
			{
				
				@Override
				public void run()
				{
					eventListAdapter.addFirst(new StatusEventModel(sourceUser, EnumStatusEventType.UNFAVORITE, targetStatus));
				}
			}.post();
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
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Toast.makeText(MainActivity.getInstance(), "ê⁄ë±ÇµÇ‹ÇµÇΩ", Toast.LENGTH_SHORT).show();				
			}
		}.post();

	}

	@Override
	public void onDisconnect()
	{
	}

}
