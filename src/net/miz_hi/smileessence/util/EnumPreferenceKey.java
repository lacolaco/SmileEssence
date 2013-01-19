package net.miz_hi.smileessence.util;

public enum EnumPreferenceKey
{
	LAST_USED_USER_ID(EnumValueType.LONG, "user_id"),
	THEME(EnumValueType.INTEGER, "theme");

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
