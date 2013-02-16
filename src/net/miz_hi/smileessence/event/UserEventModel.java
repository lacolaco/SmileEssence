package net.miz_hi.smileessence.event;

import java.util.Date;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import twitter4j.Status;
import twitter4j.User;

public class UserEventModel extends EventModel
{
	public EnumUserEventType type;

	public UserEventModel(User source, EnumUserEventType type)
	{
		super(source);
		this.type = type;
	}
	
	public static enum EnumUserEventType
	{
		BLOCK("にブロックされた"),
		UNBLOCK("にブロック解除された"),
		DIRECT_MESSAGE("からDMを受け取った"),
		FOLLOW("にフォローされた"), ;

		private final String text;

		private EnumUserEventType(String text)
		{
			this.text = text;
		}

		public String getText()
		{
			return text;
		}
	}
}
