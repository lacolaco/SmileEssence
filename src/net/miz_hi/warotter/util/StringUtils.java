package net.miz_hi.warotter.util;

import android.net.Uri;

public class StringUtils
{
	public static String parseUrlToFileName(String string)
	{
		return Uri.parse(string).getLastPathSegment();
	}
}
