package net.miz_hi.smileessence.listener;

import java.util.Date;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.event.EnumEventType;
import net.miz_hi.smileessence.event.EventListAdapter;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.EventNoticer;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.status.StatusUtils;
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
		Status status = StatusStore.get(arg0.getStatusId());
		if(status == null)
		{
			return;
		}
		else
		{
			final StatusModel model = StatusModel.createInstance(status);
			homeListAdapter.getActivity().runOnUiThread(new Runnable()
			{
				public void run()
				{
					homeListAdapter.remove(model);
				}
			});
			mentionsListAdapter.getActivity().runOnUiThread(new Runnable()
			{
				public void run()
				{
					mentionsListAdapter.remove(model);
				}
			});
			StatusStore.remove(status.getId());
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
	public void onStatus(final Status arg0)
	{
		StatusStore.put(arg0);
		final StatusModel model = StatusModel.createInstance(arg0);
		boolean isRetweet = arg0.isRetweet();
		boolean isReply = StatusUtils.isReply(isRetweet ? arg0.getRetweetedStatus() : arg0);
		if (isRetweet)
		{
			StatusStore.put(arg0.getRetweetedStatus());
		}
		if(isReply)
		{
			mentionsListAdapter.getActivity().runOnUiThread(new Runnable()
			{
				public void run()
				{
					mentionsListAdapter.addFirst(model);
				}
			});
		}
		homeListAdapter.getActivity().runOnUiThread(new Runnable()
		{
			public void run()
			{
				homeListAdapter.addFirst(model);
			}
		});
		
		//noticing
		if(isRetweet && StatusUtils.isMine(arg0.getRetweetedStatus()))
		{
			EventNoticer.receive(EventModel.createInstance(arg0.getUser(), EnumEventType.RETWEET, arg0.getRetweetedStatus()));
		}
		else if(isReply)
		{
			EventNoticer.receive(EventModel.createInstance(arg0.getUser(), EnumEventType.REPLY, arg0));
		}
	}

	@Override
	public void onTrackLimitationNotice(int arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(Exception arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onBlock(User arg0, User arg1)
	{
		if(arg1.getId() == Client.getMainAccount().getUserId())
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
		if(arg0.getRecipientId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0.getSender(), EnumEventType.DIRECT_MESSAGE, null));
		}
	}

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2)
	{
		if(arg1.getId() == Client.getMainAccount().getUserId())
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
		if(arg1.getId() == Client.getMainAccount().getUserId())
		{
			EventNoticer.receive(EventModel.createInstance(arg0, EnumEventType.UNBLOCK, null));
		}
	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2)
	{
		if(arg1.getId() == Client.getMainAccount().getUserId())
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
