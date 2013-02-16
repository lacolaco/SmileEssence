package net.miz_hi.smileessence.listener;

import android.widget.ListView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.event.EnumEventType;
import net.miz_hi.smileessence.event.EventListAdapter;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.EventNoticer;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.LogHelper;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class WarotterUserStreamListener implements UserStreamListener
{
	private StatusListAdapter homeListAdapter;
	private StatusListAdapter mentionsListAdapter;
	private EventListAdapter eventListAdapter;

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

	public void setEventListAdapter(EventListAdapter adapter)
	{
		this.eventListAdapter = adapter;
	}

	@Override
	public void onDeletionNotice(final StatusDeletionNotice arg0)
	{
		LogHelper.print("on status detete");
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
		LogHelper.print("on status");
		final StatusModel model = StatusStore.put(status);
		new UiHandler()
		{
			@Override
			public void run()
			{
				if (model.isRetweet && model.isMine())
				{
					EventNoticer.receive(EventModel.createInstance(status.getUser(), EnumEventType.RETWEET, status.getRetweetedStatus()));
				}
				else if (model.isReply())
				{
					EventNoticer.receive(EventModel.createInstance(status.getUser(), EnumEventType.REPLY, status));
				}

				ListView homeListView = MainActivity.getInstance().getHomeListView();
				ListView mentionsListView = MainActivity.getInstance().getMentionsListView();

				if (model.isReply())
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

				if (homeListView.getFirstVisiblePosition() == 0 && homeListView.getChildAt(0) != null && homeListView.getChildAt(0).getTop() == 0)
				{
					homeListAdapter.setCanNotifyOnChange(true);
				}
				else
				{
					homeListAdapter.setCanNotifyOnChange(false);
				}
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
		
	}

	@Override
	public void onBlock(User arg0, User arg1)
	{
		if (arg1.getId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0, EnumEventType.BLOCK, null));
		}
	}

	@Override
	public void onDeletionNotice(long arg0, long arg1)
	{
	}

	@Override
	public void onDirectMessage(DirectMessage arg0)
	{
		if (arg0.getRecipientId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0.getSender(), EnumEventType.DIRECT_MESSAGE, null));
		}
	}

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2)
	{
		if(arg0.getId() == Client.getMainAccount().getUserId())
		{
			if(arg2.isRetweet())
			{
				StatusStore.putFavoritedStatus(arg2.getRetweetedStatus().getId());				
			}
			else
			{
				StatusStore.putFavoritedStatus(arg2.getId());
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
		else if (arg1.getId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0, EnumEventType.FAVORITE, arg2));
		}
	}

	@Override
	public void onFollow(User arg0, User arg1)
	{
		EventNoticer.receive(EventModel.createInstance(arg0, EnumEventType.FOLLOW, null));
	}

	@Override
	public void onFriendList(long[] arg0)
	{
	}

	@Override
	public void onUnblock(User arg0, User arg1)
	{
		if (arg1.getId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0, EnumEventType.UNBLOCK, null));
		}
	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2)
	{
		if (arg1.getId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0, EnumEventType.UNFAVORITE, arg2));
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

}
