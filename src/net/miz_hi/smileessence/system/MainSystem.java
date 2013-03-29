package net.miz_hi.smileessence.system;

import java.util.List;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncMentionsGetter;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.event.HistoryListAdapter;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.listener.MyUserStreamListener;
import net.miz_hi.smileessence.menu.MainMenu;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import net.miz_hi.smileessence.view.HistoryListPageFragment;
import net.miz_hi.smileessence.view.HomeListPageFragment;
import net.miz_hi.smileessence.view.ListPagerAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.MentionsListPageFragment;
import net.miz_hi.smileessence.view.RelationListPageFragment;
import twitter4j.Paging;
import twitter4j.TwitterStream;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

public class MainSystem
{
	
	private static MainSystem instance = new MainSystem();
	private MyUserStreamListener usListener;
	private TwitterStream twitterStream;
	public FragmentPagerAdapter pageAdapter;
	public StatusListAdapter homeListAdapter;
	public StatusListAdapter mentionsListAdapter;
	public StatusListAdapter relationListAdapter;
	public HistoryListAdapter historyListAdapter;
	
	
	public static MainSystem getInstance()
	{
		return instance;
	}
	
	public void start(FragmentActivity activity)
	{
		homeListAdapter = new StatusListAdapter(activity);
		mentionsListAdapter = new StatusListAdapter(activity);
		historyListAdapter = new HistoryListAdapter(activity);
		relationListAdapter = new StatusListAdapter(activity);
		
		MainMenu.init(activity);
		
		Fragment[] fragments = new Fragment[4];
		fragments[0] = Fragment.instantiate(activity, HomeListPageFragment.class.getName());
		fragments[1] = Fragment.instantiate(activity, MentionsListPageFragment.class.getName());
		fragments[2] = Fragment.instantiate(activity, HistoryListPageFragment.class.getName());
		fragments[3] = Fragment.instantiate(activity, RelationListPageFragment.class.getName());
		
		pageAdapter = new ListPagerAdapter(activity.getSupportFragmentManager(), fragments);
	}
	
	public void twitterSetup(Handler handler)
	{
		long lastUsedId = (Long) Client.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);

		for (Account account : AuthentificationDB.instance().findAll())
		{
			if (account.getUserId() == lastUsedId)
			{
				Client.setMainAccount(account);
				usListener = new MyUserStreamListener();
				usListener.setHomeListAdapter(homeListAdapter);
				usListener.setMentionsListAdapter(mentionsListAdapter);
				usListener.setEventListAdapter(historyListAdapter);
				usListener.setRelationListAdapter(relationListAdapter);
				twitterStream = TwitterManager.getTwitterStream(Client.getMainAccount());
				twitterStream.addListener(usListener);
				twitterStream.addConnectionLifeCycleListener(usListener);
				boolean canConnect = TwitterManager.canConnect();
				
				if(canConnect)
				{
					if(!connectUserStream())
					{
						handler.sendEmptyMessage(MainActivity.HANDLER_NOT_CONNECTION);
					}
					else
					{

						final Future<List<StatusModel>> resp_home = MyExecutor.submit(new AsyncTimelineGetter(account, null));					
						final Future<List<StatusModel>> resp_mentions = MyExecutor.submit(new AsyncMentionsGetter(account, new Paging(1)));
						try
						{
							List<StatusModel> oldTimeline = resp_home.get();
							homeListAdapter.addAll(oldTimeline);
							List<StatusModel> oldMentions = resp_mentions.get();
							mentionsListAdapter.addAll(oldMentions);
							homeListAdapter.forceNotifyAdapter();
							mentionsListAdapter.forceNotifyAdapter();
							historyListAdapter.forceNotifyAdapter();
							relationListAdapter.forceNotifyAdapter();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					handler.sendEmptyMessage(MainActivity.HANDLER_NOT_CONNECTION);
				}

				handler.sendEmptyMessage(MainActivity.HANDLER_SETUPED);
				return;
			}
		}
	}
	
	public void finish()
	{
		if (twitterStream != null)
		{
			twitterStream.shutdown();
			twitterStream = null;
		}
		IconCaches.clearCache();
		StatusStore.clearCache();
		UserStore.clearCache();
		MyExecutor.shutdown();
	}
	
	public boolean connectUserStream()
	{
		if(!TwitterManager.canConnect())
		{
			return false;
		}
		if(twitterStream != null)
		{
			twitterStream.shutdown();
			twitterStream.user();
		}
		return true;
	}
	
	public void refreshLists()
	{
		homeListAdapter.forceNotifyAdapter();
		mentionsListAdapter.forceNotifyAdapter();
		historyListAdapter.forceNotifyAdapter();
		pageAdapter.notifyDataSetChanged();
	}
}
