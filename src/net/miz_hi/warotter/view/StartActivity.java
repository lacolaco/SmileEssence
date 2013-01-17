package net.miz_hi.warotter.view;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.viewmodel.StartActivityViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends EventHandlerActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.startactivity_layout);
		
		Button buttonAuth = (Button)findViewById(R.id.button_Auth);
		buttonAuth.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				onEvent("authorize");
			}
		});
	}

	@Override
	public ViewModel getViewModel()
	{
		return new StartActivityViewModel();
	}
}
