package net.miz_hi.warotter.viewmodel;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.observables.StringObservable;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.async.PostMentionsGetter;
import net.miz_hi.warotter.async.PostTimelineGetter;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.core.ThemeHelper;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.listener.TimelineScrollListener;
import net.miz_hi.warotter.listener.WarotterUserStreamListener;
import net.miz_hi.warotter.model.StatusListAdapter;
import net.miz_hi.warotter.model.StatusModel;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.util.ExtendedBoolean;
import twitter4j.Paging;
import twitter4j.TwitterStream;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
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
	private MenuViewModel menuViewModel;

	public static MainActivityViewModel initialize(EventHandlerActivity activity)
	{
		return instance = new MainActivityViewModel(activity);
	}

	public static MainActivityViewModel instance()
	{
		return instance;
	}

	private MainActivityViewModel(EventHandlerActivity activity)
	{
		homeListAdapter = new StatusListAdapter(activity);
		mentionsListAdapter = new StatusListAdapter(activity);
		handler = new Handler();
		new PostTimelineGetter(homeListAdapter).execute(new Paging(1));
		new PostMentionsGetter(mentionsListAdapter).execute(new Paging(1));
	}

	@Override
	public void onActivityCreated(EventHandlerActivity activity)
	{
		isHomeMode = new ExtendedBoolean(true);
		((ImageView)activity.findViewById(R.id.imageView_timeline)).setImageResource(R.drawable.icon_mentions_w);
		((TextView)activity.findViewById(R.id.textView_title)).setText("Home");
		ListView homeListView = (ListView)activity.findViewById(R.id.listView_home);
		ListView mentionsListView = (ListView)activity.findViewById(R.id.listView_mentions);
		homeListView.setAdapter(homeListAdapter);
		mentionsListView.setAdapter(mentionsListAdapter);		
		homeListView.setOnScrollListener(new TimelineScrollListener(homeListAdapter));
		mentionsListView.setOnScrollListener(new TimelineScrollListener(mentionsListAdapter));
		homeListView.setVisibility(View.VISIBLE);
		mentionsListView.setVisibility(View.INVISIBLE);
		
		WarotterUserStreamListener usListener = new WarotterUserStreamListener();
		usListener.setHomeListAdapter(homeListAdapter);
		usListener.setMentionsListAdapter(mentionsListAdapter);
		TwitterStream twitterStream = Warotter.getTwitterStream(Warotter.getMainAccount(), true);
		twitterStream.addListener(usListener);
		twitterStream.user();
		toast("ê⁄ë±ÇµÇ‹ÇµÇΩ");
		menuViewModel = new MenuViewModel();
		menuViewModel.setMessenger(messenger);
	}

	@Override
	public void onActivityDestroy(EventHandlerActivity activity)
	{
		Warotter.getTwitterStream(Warotter.getMainAccount(), false).shutdown();
	}

	public Command menu = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
//			activity.runOnUiThread(new Runnable()
//			{
//
//				@Override
//				public void run()
//				{
//					Dialog dialog = new Dialog(activity);
//					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//					View view = Binder.bindView(dialog.getContext(), Binder.inflateView(activity, R.layout.dialog_menu_layout, null, true), menuViewModel);
//					dialog.setContentView(view);
//					LayoutParams lp = dialog.getWindow().getAttributes();
//					DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
//					lp.width = (int) (metrics.widthPixels * 0.9);
//					lp.height = (int) (metrics.heightPixels * 0.9);
//					dialog.show();
//				}
//			});
		}
	};

	@Override
	public boolean onEvent(String eventName, EventHandlerActivity activity)
	{
		if(eventName.equals("tweet"))
		{
			messenger.raise("toggle", null);
			return true;
		}
		else if(eventName.equals("timeline"))
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
			return true;
		}
		else if(eventName.equals("menu"))
		{
//			Dialog dialog = new Dialog(activity);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			View view = Binder.bindView(dialog.getContext(), Binder.inflateView(activity, R.layout.dialog_menu_layout, null, true), menuViewModel);
//			dialog.setContentView(view);
//			LayoutParams lp = dialog.getWindow().getAttributes();
//			DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
//			lp.width = (int) (metrics.widthPixels * 0.9);
//			lp.height = (int) (metrics.heightPixels * 0.9);
//			dialog.show();
			return true;
		}
		return false;		
	}
}
