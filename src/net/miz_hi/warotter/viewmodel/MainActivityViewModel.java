package net.miz_hi.warotter.viewmodel;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import android.os.Handler;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Statuses;

public class MainActivityViewModel extends ViewModel
{
	public StringObservable title = new StringObservable("Home");
	public IntegerObservable naviSrc = new IntegerObservable(R.drawable.navi_right);
	public final ArrayListObservable listTimeline = new ArrayListObservable(StatusViewModel.class);
	
	public MainActivityViewModel()
	{
		
	}
	
	private List<StatusViewModel> getPublic()
	{
		List statuses = new ArrayList<StatusViewModel>();
		try
		{
			ResponseList<Status> list = Warotter.getTwitter().getHomeTimeline();
			for(Status st : list)
			{
				Statuses.put(st);
				statuses.add(new StatusViewModel(st.getId()));
			}
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return statuses;
	}

	@Override
	public void onActivityCreated()
	{

	}

	@Override
	public void onActivityResumed()
	{
		Handler handler = new Handler();
		handler.post(new Runnable()
		{
			
			@Override
			public void run()
			{
				listTimeline.addAll(getPublic());
				
			}
		});
	}
}
