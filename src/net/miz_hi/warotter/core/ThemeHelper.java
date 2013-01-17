package net.miz_hi.warotter.core;

import net.miz_hi.warotter.R;
import android.graphics.Color;

public class ThemeHelper
{
	public static final int WHITE = 0;
	public static final int BLACK = 1;

	public static int getHomeButton(int theme)
	{
		switch (theme)
		{
			case BLACK:
				return R.drawable.icon_home_b;
			case WHITE:
				return R.drawable.icon_home_w;
		}
		return -1;
	}

	public static int getPostButton(int theme)
	{
		switch (theme)
		{
			case BLACK:
				return R.drawable.icon_tweet_b;
			case WHITE:
				return R.drawable.icon_tweet_w;
		}
		return -1;
	}

	public static int getMenuButton(int theme)
	{
		switch (theme)
		{
			case BLACK:
				return R.drawable.icon_menu_b;
			case WHITE:
				return R.drawable.icon_menu_w;
		}
		return -1;
	}

	public static int getMentionsButton(int theme)
	{
		switch (theme)
		{
			case BLACK:
				return R.drawable.icon_mentions_b;
			case WHITE:
				return R.drawable.icon_mentions_w;
		}
		return -1;
	}

	public static int getListBgColor(int theme)
	{
		switch (theme)
		{
			case WHITE:
				return Color.WHITE;
			case BLACK:
				return Color.BLACK;
		}
		return -1;
	}

	public static int getBarBgColor(int theme)
	{
		switch (theme)
		{
			case WHITE:
				return Color.BLACK;
			case BLACK:
				return Color.WHITE;
		}
		return -1;
	}
}
