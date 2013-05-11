package net.miz_hi.smileessence.preference;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;

public enum EnumPreferenceKey
{
	LAST_USED_USER_ID(EnumValueType.LONG, "user_id", -1L),
	VISIBLE_FOORER(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_footer), true),
	TEXT_SIZE(EnumValueType.INTEGER, Client.getResource().getString(R.string.key_setting_textSize), 10),
	AFTER_SUBMIT(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_aftersubmit), true),
	NOTICE_UNFAV(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_notice_unfav), false),
	OPEN_IME(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_open_ime), true),
	CONFIRM_DIALOG(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_confirm_dialog),true),
	READ_MORSE(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_morse), true);
	;

	private final EnumValueType type;
	private final String key;
	private final Object defaultValue;

	private EnumPreferenceKey(EnumValueType type, String key, Object defaultValue)
	{
		this.type = type;
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey()
	{
		return this.key;
	}

	public EnumValueType getType()
	{
		return this.type;
	}
	
	public Object getDefaultValue()
	{
		return this.defaultValue;
	}

	public enum EnumValueType
	{
		BOOLEAN,
		INTEGER,
		LONG,
		FLOAT,
		STRING
	}
}
