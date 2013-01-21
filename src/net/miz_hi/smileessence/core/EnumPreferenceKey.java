package net.miz_hi.smileessence.core;

public enum EnumPreferenceKey
{
	LAST_USED_USER_ID(EnumValueType.LONG, "user_id"),
	TEXT_SIZE(EnumValueType.INTEGER, "text_size"),
	
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
