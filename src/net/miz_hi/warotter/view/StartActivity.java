package net.miz_hi.warotter.view;

import twitter4j.auth.AccessToken;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventBindingActivity;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.model.AuthentificationDB;
import net.miz_hi.warotter.model.Consumers;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.model.Consumers.Consumer;
import net.miz_hi.warotter.viewmodel.StartActivityViewModel;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
		return new StartActivityViewModel(this);
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		super.onActivityResult(reqCode, resultCode, data);
		viewModel.onActivityResult(reqCode, resultCode, data);
	}
}
