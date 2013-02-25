package net.miz_hi.smileessence.permission;

public class PermissionBeginner implements IPermission
{

	@Override
	public String getPermissionName()
	{
		return "Beginner";
	}
	
	@Override
	public boolean canWarotaRT()
	{
		return false;
	}

	@Override
	public boolean canCopyTweet()
	{
		return false;
	}

	@Override
	public boolean canUnOffRetweet()
	{
		return false;
	}

}
