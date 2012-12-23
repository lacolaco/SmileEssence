package net.miz_hi.warotter.model;

import gueei.binding.Observable;

import java.io.File;
import java.util.HashMap;

import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.util.AsyncIconGetter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import twitter4j.User;

public class IconCaches
{
	private static HashMap<Long, Bitmap> iconCacheMap = new HashMap<Long, Bitmap>();
	private static HashMap<Long, String> iconNameMap = new HashMap<Long, String>();

	public static void setIconBitmap(User user, Observable<Bitmap> observable)
	{
		String fileName = genIconFileName(user);
		File currentIcon = Warotter.getApplicationFile(fileName);
		File cacheDir = Warotter.getApplication().getExternalCacheDir();
		File[] caches = cacheDir.listFiles();
		/*
		 * URLからアイコンが更新されているかどうかの確認
		 */
		boolean needsCacheUpdate = true;
		if (iconNameMap.containsKey(user.getId()))
		{
			if (iconNameMap.get(user.getId()).equals(fileName))
			{
				needsCacheUpdate = false;
			}
		}
		else
		{

			for (File file : caches)
			{
				if (file.getName().equals(fileName))
				{
					needsCacheUpdate = false;
					break;
				}
			}
		}
		iconNameMap.put(user.getId(), fileName);

		if (!needsCacheUpdate)
		{
			/*
			 * キャッシュ済
			 */
			if (iconCacheMap.containsKey(user.getId()))
			{
				observable.set(iconCacheMap.get(user.getId()));
			}
			else
			{
				/*
				 * ダウンロード済
				 */
				if (currentIcon.exists())
				{
					Bitmap bm = BitmapFactory.decodeFile(currentIcon.getPath());
					iconCacheMap.put(user.getId(), bm);
					observable.set(bm);
				}
				else
				{
					for (File file : caches)
					{
						if (file.getName().startsWith(user.getScreenName()))
						{
							file.delete();
						}
					}
					AsyncIconGetter async = new AsyncIconGetter(currentIcon, observable);
					async.execute(user);
				}
			}
		}
		else
		{
			iconCacheMap.remove(user.getId());
			for (File file : caches)
			{
				if (file.getName().startsWith(user.getScreenName()))
				{
					file.delete();
				}
			}
			AsyncIconGetter async = new AsyncIconGetter(currentIcon, observable);
			async.execute(user);
		}
	}

	public static void putIconToMap(User user, Bitmap bitmap)
	{
		if (iconCacheMap.containsKey(user.getId()))
		{
			iconCacheMap.remove(user.getId());
		}
		iconCacheMap.put(user.getId(), bitmap);
	}

	private static String genIconFileName(User user)
	{
		Uri uri = Uri.parse(user.getProfileImageURL());
		String s = String.format("%1$s_%2$s", user.getScreenName(), uri.getLastPathSegment());
		return s;
	}
}
