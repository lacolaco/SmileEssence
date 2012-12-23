package net.miz_hi.warotter.util;

import android.content.Intent;

public interface ActivityCallback
{
	public abstract void run(int result, Intent data);
}
