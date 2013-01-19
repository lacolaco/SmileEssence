package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.core.ViewModel;
import net.miz_hi.smileessence.viewmodel.StartActivityViewModel;
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
