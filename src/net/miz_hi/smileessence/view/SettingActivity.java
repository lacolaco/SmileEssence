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
				helper.setSeekBarMax(16);
				helper.setSeekBarStart(Client.getTextSize() - 8);
				helper.setLevelCorrect(8);
				helper.setText("デフォルト = 10");
				helper.setOnClickListener(new OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						Client.putPreferenceValue(EnumPreferenceKey.TEXT_SIZE, helper.getProgress() + 8);
						Client.loadPreferences();
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
		super.onDestroy();
	}


}
