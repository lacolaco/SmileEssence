package net.miz_hi.smileessence.permission;

public class PermissionFriend implements IPermission
{

	@Override
	public String getPermissionName()
	{
		return "Friend";
	}

	@Override
	public boolean canWarotaRT()
	{
		return true;
	}

	@Override
	public boolean canCopyTweet()
	{
		return true;
	}

	@Override
	public boolean canUnOffRetweet()
	{
		return true;
	}

}
