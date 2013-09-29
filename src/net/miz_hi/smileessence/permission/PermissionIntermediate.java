package net.miz_hi.smileessence.permission;

public class PermissionIntermediate implements IPermission
{

	@Override
	public String getPermissionName()
	{
		return "Intermediate";
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
		return false;
	}

}
