package net.miz_hi.smileessence.viewmodel;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncMentionsGetter;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.dialog.OptionMenuAdapter;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.listener.WarotterUserStreamListener;
import net.miz_hi.smileessence.status.IconCaches;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.util.ExtendedBoolean;
import twitter4j.Paging;
import twitter4j.TwitterStream;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivityViewModel extends ViewModel
{
	public StatusListAdapter homeListAdapter;
	public StatusListAdapter mentionsListAdapter;
	public ExtendedBoolean isHomeMode;
	public Handler handler;
	private static MainActivityViewModel instance;

	public static MainActivityViewModel singleton()
	{
		if(instance == null)
		{
			instance = new MainActivityViewModel();
		}
		return instance;
	}

	private MainActivityViewModel(){}
	
	public MainActivityViewModel initialize(EventHandlerActivity activity)
	{
		homeListAdapter = new StatusListAdapter(activity);
		mentionsListAdapter = new StatusListAdapter(activity);
		handler = new Handler();
		new AsyncTimelineGetter(Client.getMainAccount()).execute(new Paging(1));
		new AsyncMentionsGetter(Client.getMainAccount()).execute(new Paging(1));
				
		WarotterUserStreamListener usListener = new WarotterUserStreamListener();
		usListener.setHomeListAdapter(homeListAdapter);
		usListener.setMentionsListAdapter(mentionsListAdapter);
		TwitterStream twitterStream = Client.getTwitterStream(Client.getMainAccount(), true);
		twitterStream.addListener(usListener);
		twitterStream.user();
		toast("ê⁄ë±ÇµÇ‹ÇµÇΩ");
		return this;
	}

	@Override
	public void onActivityCreated(EventHandlerActivity activity)
	{
		isHomeMode = new ExtendedBoolean(true);
		((ImageView)activity.findViewById(R.id.imageView_timeline)).setImageResource(R.drawable.icon_mentions_w);
		TextView viewTitle = ((TextView)activity.findViewById(R.id.textView_title));
		viewTitle.setText("Home");
		viewTitle.setTextSize((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE) + 3);
		ListView homeListView = (ListView)activity.findViewById(R.id.listView_home);
		ListView mentionsListView = (ListView)activity.findViewById(R.id.listView_mentions);
		homeListView.setAdapter(homeListAdapter);
		mentionsListView.setAdapter(mentionsListAdapter);		
		homeListView.setOnScrollListener(new TimelineScrollListener(homeListAdapter));
		mentionsListView.setOnScrollListener(new TimelineScrollListener(mentionsListAdapter));
		homeListView.setVisibility(View.VISIBLE);
		mentionsListView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onActivityDestroy(EventHandlerActivity activity)
	{
		Client.getTwitterStream(Client.getMainAccount(), false).shutdown();
		IconCaches.clearCache();
		StatusStore.clearCache();
	}
	
	private void toggleTimeline(EventHandlerActivity activity)
	{
		boolean isHome = isHomeMode.toggle();	
		ListView homeListView = (ListView)activity.findViewById(R.id.listView_home);
		ListView mentionsListView = (ListView)activity.findViewById(R.id.listView_mentions);
		homeListView.setVisibility(isHome ? View.VISIBLE : View.INVISIBLE);
		mentionsListView.setVisibility(isHome ? View.INVISIBLE : View.VISIBLE);
		String title = isHome ? "Home" : "Mentions";
		TextView viewTitle = (TextView)activity.findViewById(R.id.textView_title);
		viewTitle.setText(title);
		viewTitle.refreshDrawableState();
		int res = isHomeMode.get() ? R.drawable.icon_mentions_w : R.drawable.icon_home_w;
		((ImageView)activity.findViewById(R.id.imageView_timeline)).setImageResource(res);
	}
	
	private void openMenuDialog(EventHandlerActivity activity)
	{
		
	}

	@Override
	public boolean onEvent(String eventName, EventHandlerActivity activity)
	{
		if(eventName.equals("timeline"))
		{
			toggleTimeline(activity);
			return true;
		}
		else if(eventName.equals("menu"))
		{
			new OptionMenuAdapter(activity, "ÉÅÉjÉÖÅ[").createMenuDialog(true).show();
			return true;
		}
		return false;		
	}
}
