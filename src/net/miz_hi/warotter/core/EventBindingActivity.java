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
		super.onActivityResult(reqCode, resultCode, data);
	}

	protected EventAggregator getAggregator()
	{
		EventAggregator ea = EventAggregator.getInstance(this);
		return ea;
	}

}
