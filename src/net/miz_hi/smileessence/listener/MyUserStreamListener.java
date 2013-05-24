package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.event.BlockEvent;
import net.miz_hi.smileessence.event.DirectMessageEvent;
import net.miz_hi.smileessence.event.FavoriteEvent;
import net.miz_hi.smileessence.event.FollowEvent;
import net.miz_hi.smileessence.event.HistoryListAdapter;
import net.miz_hi.smileessence.event.ReplyEvent;
import net.miz_hi.smileessence.event.RetweetEvent;
import net.miz_hi.smileessence.event.UnblockEvent;
import net.miz_hi.smileessence.event.UnfavoriteEvent;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusChecker;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.system.RelationSystem;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.activity.MainActivity;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class MyUserStreamListener implements UserStreamListener, ConnectionLifeCycleListener
{
	private StatusListAdapter homeListAdapter = MainSystem.getInstance().homeListAdapter;
	private StatusListAdapter mentionsListAdapter = MainSystem.getInstance().mentionsListAdapter;
	private HistoryListAdapter historyListAdapter = MainSystem.getInstance().historyListAdapter;
	
	private int exceptionCount;

	public MyUserStreamListener()
	{
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0)
	{
		LogHelper.d("on status detete");
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
			for(StatusListAdapter adapter : RelationSystem.getAdapters())
			{
				adapter.removeElement(model);
				adapter.notifyAdapter();
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
		if(MainActivity.getInstance() == null || MainActivity.getInstance().isFinishing())
		{
			return;
		}
		
		final StatusModel model = StatusStore.put(status);

		if (model.isRetweet && model.isMine)
		{
			historyListAdapter.addFirst(new RetweetEvent(model.retweeter, model));
		}
		else if (!model.isRetweet && model.isReply)
		{
			Notifier.buildEvent(new ReplyEvent(model.user, model)).raise();
		}
		
		if (model.isReply)
		{
			mentionsListAdapter.addFirst(model);
			mentionsListAdapter.notifyAdapter();
		}
		
		MainSystem.getInstance().homeListAdapter.addFirst(model);				
		MainSystem.getInstance().homeListAdapter.notifyAdapter();

		StatusChecker.check(model);
	}

	@Override
	public void onTrackLimitationNotice(int arg0)
	{
	}

	@Override
	public void onException(Exception arg0)
	{
		if(exceptionCount > 0)
		{
			exceptionCount = 1;
			arg0.printStackTrace();
			Notifier.alert("切断が切れました");	
		}
	}

	@Override
	public void onBlock(User sourceUser, User targetUser)
	{
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			historyListAdapter.addFirst(new BlockEvent(UserStore.put(sourceUser)));
			historyListAdapter.notifyAdapter();	
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
			historyListAdapter.addFirst(new DirectMessageEvent(UserStore.put(message.getSender())));
			historyListAdapter.notifyAdapter();	
		}
	}

	@Override
	public void onFavorite(User sourceUser, User targetUser, Status targetStatus)
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

			MainSystem.getInstance().homeListAdapter.notifyAdapter();
			MainSystem.getInstance().mentionsListAdapter.notifyAdapter();
		}
		if (targetUser.getId() == Client.getMainAccount().getUserId())
		{
			historyListAdapter.addFirst(new FavoriteEvent(UserStore.put(sourceUser), StatusStore.put(targetStatus)));
			historyListAdapter.notifyAdapter();	
		}
	}

	@Override
	public void onFollow(User sourceUser, User targetUser)
	{
		if(sourceUser.getId() != Client.getMainAccount().getUserId())
		{
			historyListAdapter.addFirst(new FollowEvent(UserStore.put(sourceUser)));	
			historyListAdapter.notifyAdapter();	
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
			historyListAdapter.addFirst(new UnblockEvent(UserStore.put(sourceUser)));
			historyListAdapter.notifyAdapter();	
		}
	}

	@Override
	public void onUnfavorite(User sourceUser, User targetUser, Status targetStatus)
	{
		if(sourceUser.getId() == Client.getMainAccount().getUserId())
		{
			if(targetStatus.isRetweet())
			{
				StatusStore.removeFavoritedStatus(targetStatus.getRetweetedStatus().getId());				
			}
			else
			{
				StatusStore.removeFavoritedStatus(targetStatus.getId());
			}			

			MainSystem.getInstance().homeListAdapter.notifyAdapter();
			MainSystem.getInstance().mentionsListAdapter.notifyAdapter();
		}
		
		if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.NOTICE_UNFAV))
		{
			if (targetUser.getId() == Client.getMainAccount().getUserId())
			{
				historyListAdapter.addFirst(new UnfavoriteEvent(UserStore.put(sourceUser), StatusStore.put(targetStatus)));
				historyListAdapter.notifyAdapter();	
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
		Notifier.info("接続しました");
		exceptionCount = 0;
	}

	@Override
	public void onDisconnect()
	{
	}

}
