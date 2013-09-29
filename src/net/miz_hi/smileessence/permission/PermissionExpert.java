package net.miz_hi.smileessence.permission;

public class PermissionExpert implements IPermission
{

	@Override
	public String getPermissionName()
	{
		return "Expert";
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
