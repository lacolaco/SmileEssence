package net.miz_hi.smileessence.event;

public enum EnumEventType
{
	REPLY("からの返信"),
	RETWEET("にリツイートされた"),
	BLOCK("にブロックされた"),
	UNBLOCK("にブロック解除された"),
	DIRECT_MESSAGE("からDMを受け取った"),
	FAVORITE("にふぁぼられた"),
	UNFAVORITE("にあんふぁぼされた"),
	FOLLOW("にフォローされた"), ;

	private final String text;

	private EnumEventType(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}
}
