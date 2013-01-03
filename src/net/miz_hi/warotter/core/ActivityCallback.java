package net.miz_hi.warotter.core;

import android.content.Intent;

public interface ActivityCallback
{
	public abstract void run(int result, Intent data);
}
