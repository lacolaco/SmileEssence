package net.miz_hi.warotter.viewmodel;

import java.util.concurrent.ConcurrentLinkedQueue;
import twitter4j.Paging;
import twitter4j.TwitterStream;

import android.graphics.Color;
import android.view.View;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionChangedEventArg.Action;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.core.ThemeHelper;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.core.WarotterUserStreamListener;
import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.util.PostMentionsGetter;
import net.miz_hi.warotter.util.PostTimelineGetter;

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
	public BooleanObservable isHomeMode = new BooleanObservable(true);
	private static MainActivityViewModel instance;
	private boolean isAlive = true;

	public static MainActivityViewModel getSingleton()
	{
		if (instance == null)
		{
			instance = new MainActivityViewModel();
		}
		return instance;
	}

	private MainActivityViewModel()
	{
		layoutInitialize();
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
		eventAggregator.publish("toast", new ToastMessage("ê⁄ë±ÇµÇ‹ÇµÇΩ"), null);
	}

	@Override
	public void onActivityResumed()
	{
	}
	
	@Override
	public void onDispose()
	{
		Warotter.getTwitterStream(Warotter.getMainAccount(), false).shutdown();
		isAlive = false;
	}

	public Command onItemClicked = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			if (clickedItem.get() instanceof StatusViewModel)
			{
				eventAggregator.publish("toast", new ToastMessage(((StatusViewModel) clickedItem.get()).screenName.get()), null);
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
			eventAggregator.publish("runOnUiThread", new Runnable()
			{
				
				@Override
				public void run()
				{
					isHomeMode.toggle();
					title.set(isHomeMode.get() ? "Home" : "Mentions" );
					int theme = Warotter.getTheme();
					timelineButtonRes.set(isHomeMode.get() ? ThemeHelper.getMentionsButton(theme) : ThemeHelper.getHomeButton(theme));
				}
			}, null);
		}
	};

	public Command menu = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			eventAggregator.publish("toast", new ToastMessage("menu"), null);
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
					eventAggregator.publish("runOnUiThread", new Runnable()
					{

						@Override
						public void run()
						{
							try
							{
								StatusViewModel svm = StatusViewModel.createInstance(preLoadStatusQueue.poll());
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
					}, null);					
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
