package net.miz_hi.smileessence.system;

import java.util.List;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncMentionsGetter;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.data.page.Page;
import net.miz_hi.smileessence.data.page.Pages;
import net.miz_hi.smileessence.dialog.OneButtonDialog;
import net.miz_hi.smileessence.event.HistoryListAdapter;
import net.miz_hi.smileessence.listener.MyUserStreamListener;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.NetworkUtils;
import net.miz_hi.smileessence.view.IRemainable;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.ExtractFragment;
import net.miz_hi.smileessence.view.fragment.RelationFragment;
import twitter4j.Paging;
import twitter4j.TwitterStream;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MainSystem
{
	
	private static MainSystem instance = new MainSystem();
	private MyUserStreamListener usListener;
	private TwitterStream twitterStream;
	public StatusListAdapter homeListAdapter;
	public StatusListAdapter mentionsListAdapter;
	public HistoryListAdapter historyListAdapter;
	public StatusListAdapter extractListAdapter;
	public AuthorizeHelper authHelper;
	public Uri tempFilePath;	
	
	public static MainSystem getInstance()
	{
		return instance;
	}
	
	public void initialize(Activity activity)
	{
		homeListAdapter = new StatusListAdapter(activity);
		mentionsListAdapter = new StatusListAdapter(activity);
		historyListAdapter = new HistoryListAdapter(activity);
		extractListAdapter = new StatusListAdapter(activity);
	}
	
	public void setup(Activity activity)
	{
		if (Client.hasAuthedAccount())
		{
			long lastUsedId = (Long) Client.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);
			
			for (Account account : AuthentificationDB.instance().findAll())
			{
				if (account.getUserId() == lastUsedId)
				{
					Client.setMainAccount(account);
					break;
				}
			}
			if(Client.getMainAccount() == null)
			{
				Client.setMainAccount(AuthentificationDB.instance().findAll().get(0));
			}
			
			loadRemainablePages();
			
			usListener = new MyUserStreamListener();

			twitterStream = TwitterManager.getTwitterStream(Client.getMainAccount());
			twitterStream.addListener(usListener);
			twitterStream.addConnectionLifeCycleListener(usListener);
			if(connectUserStream(activity))
			{
				final Future<List<StatusModel>> resp_home = MyExecutor.submit(new AsyncTimelineGetter(Client.getMainAccount(), null));					
				final Future<List<StatusModel>> resp_mentions = MyExecutor.submit(new AsyncMentionsGetter(Client.getMainAccount(), new Paging(1)));

				MyExecutor.execute(new Runnable()
				{

					@Override
					public void run()
					{
						try
						{
							homeListAdapter.addAll(resp_home.get());
							mentionsListAdapter.addAll(resp_mentions.get());
							homeListAdapter.forceNotifyAdapter();
							mentionsListAdapter.forceNotifyAdapter();
							historyListAdapter.forceNotifyAdapter();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							Notifier.alert("タイムラインの取得に失敗しました");
						}
					}
				});
			}
			else
			{
				Notifier.alert("接続出来ません");
			}
		}
		else
		{
			authHelper = new AuthorizeHelper(activity, Consumers.getDedault());
			//NOT AUTHOLIZED
			OneButtonDialog.show(activity, "認証してください", "認証ページヘ", new Runnable()
			{
				
				@Override
				public void run()
				{
					authHelper.oauthSend();
				}
			});
			
		}
	}
	
	public void onDestroyed()
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
	
	private void loadRemainablePages()
	{
		Pages.update();
		for(Page page : Pages.getPages())
		{
			String className = page.getClassName();
			if(className.equals(RelationFragment.class.getSimpleName()))
			{
				RelationFragment fragment = RelationFragment.newInstance(-1);
				fragment.load(page.getData());
			}
			else if(className.equals(ExtractFragment.class.getSimpleName()))
			{
				ExtractFragment fragment = ExtractFragment.singleton();
				fragment.load(page.getData());
			}
		}
	}
	
	public void saveRamainablePages()
	{
		Pages.clear();
		Pages.startTransaction();
		for(Object element : MainActivity.getInstance().getFragmentAdapter().getList())
		{
			if(element instanceof IRemainable)
			{
				Pages.addPage(new Page(element.getClass().getSimpleName(), ((IRemainable) element).save()));
			}
		}
		Pages.stopTransaction();
	}
	
	public void receivePicture(Activity activity, Intent data, int reqCode)
	{
		try
		{
			Uri uri;
			if(reqCode == EnumRequestCode.PICTURE.ordinal())
			{
				uri = data.getData();
			}
			else
			{
				uri = MainSystem.getInstance().tempFilePath;
			}
			Cursor c = activity.getContentResolver().query(uri, null, null, null, null);
			c.moveToFirst();
			String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
			PostSystem.setPicturePath(path).openPostPage();
			Notifier.info("画像をセットしました");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Notifier.alert("失敗しました");
		}
	}

	public boolean connectUserStream(Activity activity)
	{
		if(!NetworkUtils.canConnect(activity))
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

	public void authorize(Activity activity, Uri data)
	{
		Account account = authHelper.oauthRecieve(data);
		setup(activity);
	}
}
