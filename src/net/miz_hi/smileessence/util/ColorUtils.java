package net.miz_hi.smileessence.util;

import android.graphics.Color;

public class ColorUtils
{

	public static int setAlpha(int color, int alpha)
	{
		return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
	}
}
