package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserMentionEntity;

public class StatusUtils
{

	public static boolean isReply(long id)
	{
		return isReply(StatusStore.get(id));
	}

	public static boolean isMine(long id)
	{
		return isMine(StatusStore.get(id));
	}
	
	public static boolean isReply(Status st)
	{
		for (UserMentionEntity ume : st.getUserMentionEntities())
		{
			if (ume.getScreenName().equals(Client.getMainAccount().getScreenName()))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isMine(Status st)
	{
		return st.getUser().getId() == Client.getMainAccount().getUserId();
	}
	
	public static boolean isMe(User user)
	{
		return user.getId() == Client.getMainAccount().getUserId();
	}
}
