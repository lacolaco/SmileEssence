package net.miz_hi.warotter.core;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public abstract class EventHandlerActivity extends Activity
{

	public List<ViewModel> viewModelList = new ArrayList<ViewModel>();

	public abstract ViewModel getViewModel();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewModel vm = getViewModel();
		viewModelList.add(vm);
		
	}
	
	public final void registerViewModel(ViewModel vm)
	{
		viewModelList.add(vm);
	}

	@Override
	public void onPostCreate(Bundle bundle)
	{
		super.onPostCreate(bundle);
		for(ViewModel vm : viewModelList)
		{
			vm.onActivityCreated(this);
		}
	}

	@Override
	public void onPostResume()
	{
		super.onPostResume();
		for(ViewModel vm : viewModelList)
		{
			vm.onActivityResumed(this);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		for(ViewModel vm : viewModelList)
		{
			vm.onActivityPaused(this);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		for(ViewModel vm : viewModelList)
		{
			vm.onActivityDestroy(this);
		}
	}
	
	public final void onEvent(String event)
	{
		for(ViewModel vm : viewModelList)
		{
			if(vm.onEvent(event, this))
			{
				break;
			}
		}
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		for(ViewModel vm : viewModelList)
		{
			vm.onActivityResult(this, reqCode, resultCode, data);
		}
	}
	
	public Messenger subscribeMessenger(Messenger messenger)
	{
		messenger.subscribe("toast", new EventSubscriber()
		{
			
			@Override
			public void onEventTriggered(String eventName, final Message message)
			{
				runOnUiThread(new Runnable()
				{
					public void run()
					{
						if(message instanceof ToastMessage)
						{
							Toast.makeText(EventHandlerActivity.this, ((ToastMessage)message).text, Toast.LENGTH_SHORT).show();
						}
					}
				});				
			}
		});
		
		return messenger;		
	}

}
