package net.miz_hi.warotter.view;

import gueei.binding.app.BindingActivity;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventBindingActivity;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.viewmodel.StartActivityViewModel;
import android.os.Bundle;
import android.widget.Toast;

public class StartActivity extends EventBindingActivity
{	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.setAndBindRootView(R.layout.startactivity_layout, viewModel);        
    }

	@Override
	public ViewModel getViewModel()
	{
		return new StartActivityViewModel();
	}
}
