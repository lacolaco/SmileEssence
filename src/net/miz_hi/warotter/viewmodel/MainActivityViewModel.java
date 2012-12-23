package net.miz_hi.warotter.viewmodel;

import java.util.concurrent.ConcurrentLinkedQueue;
import twitter4j.Paging;
import twitter4j.TwitterStream;

import android.graphics.Color;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.core.WarotterUserStreamListener;
import net.miz_hi.warotter.util.PostTimelineGetter;

public class MainActivityViewModel extends ViewModel implements Runnable
{
	public StringObservable title = new StringObservable("Home");
	public ObjectObservable clickedItem = new ObjectObservable();
	public BooleanObservable isScrollTop = new BooleanObservable(false);
	public Observable<View> footerView = new Observable<View>(View.class);
	public IntegerObservable listBackground = new IntegerObservable(Color.WHITE);
	public IntegerObservable titleBackground = new IntegerObservable(Color.BLACK);
	public IntegerObservable infoBackground = new IntegerObservable(Color.BLACK);
	public ArrayListObservable<StatusViewModel> listTimeline;
	public ArrayListObservable<StatusViewModel> homeTimeline = new ArrayListObservable<StatusViewModel>(StatusViewModel.class);
	public ArrayListObservable<StatusViewModel> mentionsTimeline = new ArrayListObservable<StatusViewModel>(StatusViewModel.class);
	public ConcurrentLinkedQueue<Long> preLoadStatusQueue = new ConcurrentLinkedQueue<Long>();
	public Thread queueWatcher;
	private static MainActivityViewModel instance;

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
		listTimeline = homeTimeline;
	}

	@Override
	public void onActivityCreated()
	{
		new PostTimelineGetter(this).execute(new Paging(1));
		TwitterStream twitterStream = Warotter.getTwitterStream(true);
		twitterStream.addListener(new WarotterUserStreamListener());
		twitterStream.user();
		queueWatcher = new Thread(this, "test");
		queueWatcher.start();
		eventAggregator.publish("toast", new ToastMessage("Ú‘±‚µ‚Ü‚µ‚½"), null);
	}

	@Override
	public void onActivityResumed()
	{
	}
	
	@Override
	public void onDispose()
	{
		queueWatcher.stop();
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
			eventAggregator.publish("toast", new ToastMessage("top:" + isScrollTop.get()), null);
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
		while (true)
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
							StatusViewModel svm = StatusViewModel.createInstance(preLoadStatusQueue.poll());
							if(svm != null)
							{
								if(listTimeline.size() >= 1000)
								{
									listTimeline.remove(listTimeline.size());
								}
								listTimeline.add(0,svm);
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
