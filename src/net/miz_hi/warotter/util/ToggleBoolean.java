package net.miz_hi.warotter.util;

public class ToggleBoolean
{
	private boolean flag;
	
	public ToggleBoolean()
	{
		this.flag = false;
	}
	
	public ToggleBoolean(boolean flag)
	{
		this.flag = flag;
	}
	
	public boolean get()
	{
		return flag;
	}
	
	public boolean toggle()
	{
		flag = !flag;
		return flag;
	}
	
	public boolean set(boolean newValue)
	{
		this.flag = newValue;
		return flag;
	}

}
