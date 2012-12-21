package net.miz_hi.warotter.core;

import java.net.URI;
import java.util.HashMap;

import twitter4j.URLEntity;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.util.ActivityCallback;
import net.miz_hi.warotter.viewmodel.StartActivityViewModel;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;
import gueei.binding.app.BindingActivity;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;

public abstract class EventBindingActivity extends BindingActivity
{
	
	private static SparseArray<ActivityCallback> callback = new SparseArray<ActivityCallback>();
	
	public ViewModel viewModel;
	
	public abstract ViewModel getViewModel();
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        EventAggregator ea = EventAggregator.getInstance(this);
        viewModel = getViewModel();
        viewModel.setEventAggregator(getAggregator()); 
    }
	
	@Override
	public void onPostCreate(Bundle bundle)
	{
		super.onPostCreate(bundle);
		viewModel.onActivityCreated();
	}
	
	@Override
	public void onPostResume()
	{
		super.onPostResume();
		viewModel.onActivityResumed();
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		if(!(callback.indexOfKey(reqCode) < 0))
		{
			callback.get(reqCode).run(resultCode, data);
		}
	}
	
	private EventAggregator getAggregator()
	{
		EventAggregator ea = EventAggregator.getInstance(this);
		ea.subscribe("toast", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				if(arg1 instanceof ToastMessage)
				{
					ToastMessage message = (ToastMessage)arg1;
					Toast.makeText(EventBindingActivity.this, message.text, message.duration).show();				
				}
			}
		});
		ea.subscribe("startActivity", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				if(arg1 instanceof StartActivityMessage)
				{
					StartActivityMessage message = (StartActivityMessage)arg1;
					if(message.hasCallback())
					{
						callback.put(message.reqCode, message.callback);
						message.intent.setClass(EventBindingActivity.this, message.clazz);
						startActivityForResult(message.intent, message.reqCode);
					}
					else
					{
						message.intent.setClass(EventBindingActivity.this, message.clazz);
						startActivity(message.intent);		
					}
				}
			}
		});
		ea.subscribe("finish", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				finish();				
			}
		});
		ea.subscribe("runOnUiThread", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				if(arg1 instanceof Runnable)
				{
					runOnUiThread((Runnable) arg1);
				}				
			}
		});
		return ea;
	}

}
