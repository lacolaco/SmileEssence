package net.miz_hi.warotter.core;

import android.content.Intent;
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
		if (!(callback.indexOfKey(reqCode) < 0))
		{
			callback.get(reqCode).run(resultCode, data);
		}
	}

	protected EventAggregator getAggregator()
	{
		EventAggregator ea = EventAggregator.getInstance(this);
		ea.subscribe("toast", new EventSubscriber()
		{

			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				if (arg1 instanceof ToastMessage)
				{
					final ToastMessage message = (ToastMessage) arg1;
					runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							Toast.makeText(EventBindingActivity.this, message.text, message.duration).show();
						}
					});

				}
			}
		});
		ea.subscribe("startActivity", new EventSubscriber()
		{

			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				if (arg1 instanceof StartActivityMessage)
				{
					final StartActivityMessage message = (StartActivityMessage) arg1;
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{

							if (message.hasCallback())
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
					});
				}
			}
		});
		ea.subscribe("finish", new EventSubscriber()
		{

			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						finish();
					}
				});
			}
		});
		ea.subscribe("runOnUiThread", new EventSubscriber()
		{

			@Override
			public void onEventTriggered(String arg0, Object arg1, Bundle arg2)
			{
				if (arg1 instanceof Runnable)
				{
					runOnUiThread((Runnable) arg1);
				}
			}
		});
		return ea;
	}

}
