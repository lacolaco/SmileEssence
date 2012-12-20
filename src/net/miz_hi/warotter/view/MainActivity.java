package net.miz_hi.warotter.view;

import com.slidingmenu.lib.SlidingMenu;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventBindingActivity;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.util.EnumRequestCode;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import android.os.Bundle;

public class MainActivity extends EventBindingActivity
{

	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setAndBindRootView(R.layout.mainactivity_layout, viewModel);
		SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	}

	@Override
	public ViewModel getViewModel()
	{
		return new MainActivityViewModel();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		finishActivity(EnumRequestCode.MAIN.ordinal());
	}
	

}
