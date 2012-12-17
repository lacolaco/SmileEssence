package net.miz_hi.warotter.util;

public enum EnumPreferenceKey
{
	AUTHORIZED(EnumValueType.BOOLEAN, "is_authorized"),
	TOKEN(EnumValueType.STRING, "token"),
	TOKEN_SECRET(EnumValueType.STRING, "token_secret"),
	SCREEN_NAME(EnumValueType.STRING, "screen_name"),
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
