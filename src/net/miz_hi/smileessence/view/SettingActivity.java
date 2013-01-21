package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.dialog.SeekBarDialogHelper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		
		Preference textSize = findPreference(getResources().getString(R.string.key_setting_textSize));
		textSize.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			public boolean onPreferenceClick(Preference preference)
			{
				final SeekBarDialogHelper helper = new SeekBarDialogHelper(SettingActivity.this, "テキストサイズ");
				helper.setSeekBarMax(20);
				helper.setSeekBarStart((Integer)Client.getPreferenceValue(EnumPreferenceKey.TEXT_SIZE) - 10);
				helper.setText("デフォルト = 0");
				helper.setOnClickListener(new OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						Client.putPreferenceValue(EnumPreferenceKey.TEXT_SIZE, helper.getProgress() + 10);						
					}
				});
				helper.createSeekBarDialog().show();
				return true;
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}


}
