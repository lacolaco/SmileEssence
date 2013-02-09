package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

public class StatusUtils
{

	public static boolean isMine(long id)
	{
		return isMine(StatusStore.get(id));
	}

	public static boolean isReply(Status st)
	{
		if (st == null)
		{
			return false;
		}
		for (UserMentionEntity ume : st.getUserMentionEntities())
		{
			if (ume.getScreenName().equals(Client.getMainAccount().getScreenName()))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isMine(StatusModel st)
	{
		return st.user.isMe();
	}

}
