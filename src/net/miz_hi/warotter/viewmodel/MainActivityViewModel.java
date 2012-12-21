package net.miz_hi.warotter.viewmodel;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;

import android.R.color;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.cursor.CursorField;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.core.WarotterUserStreamListener;
import net.miz_hi.warotter.model.Statuses;

public class MainActivityViewModel extends ViewModel
{
	public StringObservable title = new StringObservable("Home");
	public ObjectObservable clickedItem = new ObjectObservable();
	public IntegerObservable listBackground = new IntegerObservable(Color.WHITE);
	public IntegerObservable titleBackground = new IntegerObservable(Color.BLACK);
	public IntegerObservable infoBackground = new IntegerObservable(Color.BLACK);
	public final ArrayListObservable listTimeline = new ArrayListObservable(StatusViewModel.class);
	
	public MainActivityViewModel()
	{
	}

	@Override
	public void onActivityCreated()
	{

	}

	@Override
	public void onActivityResumed()
	{
		TwitterStream twitterStream = Warotter.getTwitterStream(true);
		twitterStream.addListener(new WarotterUserStreamListener(this, listTimeline));
		twitterStream.user();
	}
	
	public Command onItemClicked = new Command()
	{
		
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			if(clickedItem.get() instanceof StatusViewModel)
			{
				eventAggregator.publish("toast", new ToastMessage(((StatusViewModel)clickedItem.get()).screenName.get()), null);
			}
		}
	};
	
	public Command tweet = new  Command()
	{
		
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			eventAggregator.publish("toast", new ToastMessage("tweet"), null);			
		}
	};
	
	public Command mentions = new  Command()
	{
		
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			eventAggregator.publish("toast", new ToastMessage("mentions"), null);			
		}
	};
	
	public Command menu = new  Command()
	{
		
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			eventAggregator.publish("toast", new ToastMessage("menu"), null);			
		}
	};
	
	
}
