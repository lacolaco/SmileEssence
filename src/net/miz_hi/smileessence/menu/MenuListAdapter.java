package net.miz_hi.smileessence.menu;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.util.CustomListAdapter;

public class MenuListAdapter extends CustomListAdapter<ICommand>
{
	
	public MenuListAdapter(Activity activity)
	{
		super(activity, 100);
		
	}

	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		if(convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.menuitem_white, null);
		}
		
		final ICommand item = (ICommand) getItem(position);
		
		TextView textView = (TextView) convertedView.findViewById(R.id.textView_menuItem);
		textView.setText(item.getName());
		convertedView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(item instanceof IConfirmable && Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
				{
					ConfirmDialog.show(getActivity(), "é¿çsÇµÇ‹Ç∑Ç©ÅH", new Runnable()
					{
						@Override
						public void run()
						{
							item.run();
						}
					});
				}
				else
				{
					item.run();
				}
			}
		});
		return convertedView;
	}

}
