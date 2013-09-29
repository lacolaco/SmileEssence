package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.system.PageControler;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.impl.ListFragment;
import net.miz_hi.smileessence.view.fragment.impl.PostFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class PageChangeListener implements OnPageChangeListener
{
	
	boolean isOpening = false;

	@Override
	public void onPageScrollStateChanged(int position)
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}

	@Override
	public void onPageSelected(final int position)
	{
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				if(position == PageControler.PAGE_POST)
				{
					PostFragment.singleton().load();
					isOpening = true;
				}
				else
				{
					if(isOpening)
					{
						PostFragment.singleton().save();
					}
					isOpening = false;

					Fragment fragment = PageControler.getInstance().getAdapter().getItem(position);

					if(fragment instanceof ListFragment)
					{
						if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.LIST_LOAD))
						{
							final ListFragment page = (ListFragment)fragment;
							if(!page.isInited())
							{
								new UiHandler()
								{

									@Override
									public void run()
									{
										page.refresh();
									}
								}.post();
							}
						}
					}
				}
			}
		});

	}

}
