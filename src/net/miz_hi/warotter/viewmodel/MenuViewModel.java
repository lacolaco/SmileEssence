package net.miz_hi.warotter.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.core.StartActivityMessage;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.AuthentificationDB;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.view.StartActivity;

public class MenuViewModel extends ViewModel
{
	public StringObservable title = new StringObservable("メニュー");
	public ArrayListObservable<MenuItemViewModel> items = new ArrayListObservable<MenuItemViewModel>(MenuItemViewModel.class);
	public ObjectObservable clickedItem = new ObjectObservable();
	
	public MenuViewModel(Activity activity)
	{
		super(activity);
		initialize();
	}

	private void initialize()
	{
		items.add(new MenuItemViewModel(activity, "設定").setCommand(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				
			}
		}));
		items.add(new MenuItemViewModel(activity, "認証情報のリセット").setCommand(new Runnable()
		{
			
			@Override
			public void run()
			{
				AuthentificationDB.instance().delete(Warotter.getMainAccount());
				Warotter.setMainAccount(null);
				toast("全ての認証情報をリセットしました。再起動してください");
				MainActivityViewModel.instance().handler.postDelayed(new Runnable()
				{
					
					@Override
					public void run()
					{
						activity.finish();						
					}
				}, 2000);
			}
		}));
	}
	
	public Command onItemClicked = new Command()
	{
		
		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			if(clickedItem != null && clickedItem.get() instanceof MenuItemViewModel)
			{
				((MenuItemViewModel)clickedItem.get()).run();
			}
			
		}
	};
}
