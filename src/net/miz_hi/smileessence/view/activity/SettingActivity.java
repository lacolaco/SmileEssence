package net.miz_hi.smileessence.view.activity;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.dialog.SeekBarDialog;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.ViewConfiguration;

public class SettingActivity extends PreferenceActivity
{

	private static final String T4J_URL = "http://twitter4j.org/";
	private boolean footorFirstClick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	
		footorFirstClick = true;
		Preference textSize = findPreference(getResources().getString(R.string.key_setting_textSize));
		textSize.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				final SeekBarDialog helper = new SeekBarDialog(SettingActivity.this, "テキストサイズ");
				helper.setSeekBarMax(16);
				helper.setSeekBarStart(Client.getTextSize() - 8);
				helper.setLevelCorrect(8);
				helper.setText("デフォルト = 10");
				helper.setOnClickListener(new OnClickListener()
				{
					@Override
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
		
		Preference t4j = findPreference(getResources().getString(R.string.key_setting_t4j));
		t4j.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(T4J_URL)));
				return true;
			}
		});
		
		Preference app = findPreference(getResources().getString(R.string.prefrence_app));
		app.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Client.HOMEPAGE_URL)));
				return true;
			}
		});
		
		Preference deleteAccounts = findPreference(getResources().getString(R.string.key_setting_delete_accounts));
		deleteAccounts.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				ConfirmDialog helper = new ConfirmDialog(SettingActivity.this, "本当にリセットしますか？");
				helper.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch(which)
						{
							case DialogInterface.BUTTON_POSITIVE:
							{
								Notifier.toast("全ての認証情報をリセットします。再起動してください");
								AuthentificationDB.instance().deleteAll();
								finish();
								MainActivity.getInstance().finish();
								break;
							}
							default:
							{
								dialog.dismiss();
							}
						}

					}
				});
				helper.createYesNoAlert().show();
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