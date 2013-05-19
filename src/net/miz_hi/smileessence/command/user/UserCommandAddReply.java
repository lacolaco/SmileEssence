package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.system.PostSystem;

public class UserCommandAddReply extends UserCommand
{

	public UserCommandAddReply(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "リプライ先に追加";
	}

	@Override
	public void workOnUiThread()
	{
		PostSystem.addReply(userName);
		Notifier.info(userName + "をリプライ先に追加しました");
	}
}
