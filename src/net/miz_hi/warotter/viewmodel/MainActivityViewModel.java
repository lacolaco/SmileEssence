package net.miz_hi.warotter.viewmodel;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.observables.StringObservable;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.ThemeHelper;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.core.WarotterUserStreamListener;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.util.PostMentionsGetter;
import net.miz_hi.warotter.util.PostTimelineGetter;
import twitter4j.Paging;
import twitter4j.TwitterStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class MainActivityViewModel extends ViewModel implements Runnable
{
	public StringObservable title = new StringObservable();
	public IntegerObservable timelineButtonRes = new IntegerObservable();
	public IntegerObservable postButtonRes = new IntegerObservable();
	public IntegerObservable menuButtonRes = new IntegerObservable();
	public ObjectObservable clickedItem = new ObjectObservable();
	public BooleanObservable isScrollTop = new BooleanObservable(false);
	public Observable<View> footerView = new Observable<View>(View.class);
	public IntegerObservable listBackground = new IntegerObservable();
	public IntegerObservable barBackground = new IntegerObservable();
	public ArrayListObservable<StatusViewModel> homeTimeline = new ArrayListObservable<StatusViewModel>(StatusViewModel.class);
	public ArrayListObservable<StatusViewModel> mentionsTimeline = new ArrayListObservable<StatusViewModel>(StatusViewModel.class);
	public ConcurrentLinkedQueue<Long> preLoadStatusQueue = new ConcurrentLinkedQueue<Long>();
	public Thread queueWatcher;
	public Handler handler;
	public BooleanObservable isHomeMode = new BooleanObservable(true);
	private static MainActivityViewModel instance;
	private boolean isAlive = true;
	private MenuViewModel menuViewModel;

	public static MainActivityViewModel initialize(Activity activity)
	{
		return instance = new MainActivityViewModel(activity);
	}
	
	public static MainActivityViewModel instance()
	{
		return instance;
	}

	private MainActivityViewModel(Activity activity)
	{
		super(activity);
		layoutInitialize();
		handler = new Handler();
		new PostTimelineGetter(this).execute(new Paging(1));
		new PostMentionsGetter(this).execute(new Paging(1));
	}
	
	private void layoutInitialize()
	{
		int theme = Warotter.getTheme();
		title.set("Home");
		listBackground.set(ThemeHelper.getListBgColor(theme));
		barBackground.set(ThemeHelper.getBarBgColor(theme));
		timelineButtonRes.set(ThemeHelper.getMentionsButton(theme));
		postButtonRes.set(ThemeHelper.getPostButton(theme));
		menuButtonRes.set(ThemeHelper.getMenuButton(theme));
	}

	@Override
	public void onActivityCreated()
	{
		TwitterStream twitterStream = Warotter.getTwitterStream(Warotter.getMainAccount(), true);
		twitterStream.addListener(new WarotterUserStreamListener());
		twitterStream.user();
		queueWatcher = new Thread(this, "test");
		queueWatcher.setDaemon(true);
		queueWatcher.start();
		toast("ê⁄ë±ÇµÇ‹ÇµÇΩ");
		menuViewModel = new MenuViewModel(activity);
		menuViewModel.setEventAggregator(eventAggregator);
	}
	
	@Override
	public void onDispose()
	{
		isAlive = false;
		Warotter.getTwitterStream(Warotter.getMainAccount(), false).shutdown();
	}

	public Command onItemClicked = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			if (clickedItem.get() instanceof StatusViewModel)
			{
				StatusViewModel svm = (StatusViewModel)clickedItem.get();
				toast(svm.screenName.get());
			}
		}
	};

	public Command tweet = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			eventAggregator.publish("toggle", null, null);
		}
	};

	public Command mentions = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			activity.runOnUiThread(new Runnable()
			{
				
				@Override
				public void run()
				{
					isHomeMode.toggle();
					title.set(isHomeMode.get() ? "Home" : "Mentions" );
					int theme = Warotter.getTheme();
					timelineButtonRes.set(isHomeMode.get() ? ThemeHelper.getMentionsButton(theme) : ThemeHelper.getHomeButton(theme));
				}
			});
		}
	};

	public Command menu = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			activity.runOnUiThread(new Runnable()
			{
				
				@Override
				public void run()
				{
					Dialog dialog = new Dialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					View view = Binder.bindView(dialog.getContext(), 
							Binder.inflateView(activity, R.layout.dialog_menu_layout, null, true), menuViewModel);
					dialog.setContentView(view);
					LayoutParams lp = dialog.getWindow().getAttributes();
					DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
					lp.width = (int) (metrics.widthPixels * 0.9);
					lp.height = (int) (metrics.heightPixels * 0.9);
					dialog.show();
				}
			});
		}
	};

	@Override
	public void run()
	{
		while (isAlive)
		{			
			try
			{
				if(isScrollTop.get() && preLoadStatusQueue.peek() != null)
				{
					activity.runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							try
							{
								StatusViewModel svm = StatusViewModel.createInstance(activity, preLoadStatusQueue.poll());
								if(svm != null)
								{
									homeTimeline.add(0,svm);
									if(svm.isRelpy)
									{
										mentionsTimeline.add(0, svm);
									}
								}
							}
							catch(Throwable t)
							{
								
							}
						}							
					});					
				}
				Thread.sleep(400);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
