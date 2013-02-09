package net.miz_hi.smileessence.util;

public class ExtendedBoolean
{
	private boolean flag;
	private boolean initialized = false;

	public ExtendedBoolean()
	{
		this.flag = false;
	}

	public ExtendedBoolean(boolean flag)
	{
		this.flag = flag;
		initialized = true;
	}

	public boolean get()
	{
		return flag;
	}

	public boolean toggle()
	{
		flag = !flag;
		initialized = true;
		return flag;
	}

	public boolean set(boolean newValue)
	{
		this.flag = newValue;
		initialized = true;
		return flag;
	}

	public boolean isInitialized()
	{
		return initialized;
	}

}
