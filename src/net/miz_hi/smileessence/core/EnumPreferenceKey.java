package net.miz_hi.smileessence.core;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;

public enum EnumPreferenceKey
{
	LAST_USED_USER_ID(EnumValueType.LONG, "user_id"),
	TEXT_SIZE(EnumValueType.INTEGER, Client.getResource().getString(R.string.key_setting_textSize)),
	AFTER_SUBMIT(EnumValueType.BOOLEAN, Client.getResource().getString(R.string.key_setting_aftersubmit)),

	;

	private final EnumValueType type;
	private final String key;

	private EnumPreferenceKey(EnumValueType type, String key)
	{
		this.type = type;
		this.key = key;
	}

	public String getKey()
	{
		return this.key;
	}

	public EnumValueType getType()
	{
		return this.type;
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
