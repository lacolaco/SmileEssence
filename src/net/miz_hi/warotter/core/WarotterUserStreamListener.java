package net.miz_hi.warotter.core;

import net.miz_hi.warotter.model.StatusStore;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import net.miz_hi.warotter.viewmodel.StatusViewModel;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class WarotterUserStreamListener implements UserStreamListener
{
	private MainActivityViewModel mainViewModel;

	public WarotterUserStreamListener()
	{
		this.mainViewModel = MainActivityViewModel.getSingleton();
	}

	@Override
	public void onDeletionNotice(final StatusDeletionNotice arg0)
	{
		mainViewModel.eventAggregator.publish("runOnUiThread", new Runnable()
		{

			@Override
			public void run()
			{
				StatusStore.remove(arg0.getStatusId());
			}
		}, null);

	}

	@Override
	public void onScrubGeo(long arg0, long arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStallWarning(StallWarning arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatus(final Status arg0)
	{
		mainViewModel.eventAggregator.publish("runOnUiThread", new Runnable()
		{

			@Override
			public void run()
			{
				StatusStore.put(arg0);
				if(arg0.isRetweet())
				{
					StatusStore.put(arg0.getRetweetedStatus());
				}
				mainViewModel.preLoadStatusQueue.add(arg0.getId());
			}
		}, null);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeletionNotice(long arg0, long arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDirectMessage(DirectMessage arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onFollow(User arg0, User arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onFriendList(long[] arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnblock(User arg0, User arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListCreation(User arg0, UserList arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListDeletion(User arg0, UserList arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListMemberAddition(User arg0, User arg1, UserList arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListSubscription(User arg0, User arg1, UserList arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListUnsubscription(User arg0, User arg1, UserList arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListUpdate(User arg0, UserList arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserProfileUpdate(User arg0)
	{
		// TODO Auto-generated method stub

	}

}
