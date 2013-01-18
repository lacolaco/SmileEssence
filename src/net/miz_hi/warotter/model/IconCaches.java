package net.miz_hi.warotter.model;

import gueei.binding.Observable;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.async.AsyncIconGetter;
import net.miz_hi.warotter.util.StringUtils;
import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

public class IconCaches
{

	private static HashMap<Long, Icon> iconCache = new HashMap<Long, Icon>();
	private static File cacheDir = Warotter.getApplication().getExternalCacheDir();
	private static Bitmap emptyIcon;

	public static void setIconBitmapToView(User user, ImageView viewIcon, StatusModel model)
	{
		String fileName = genIconName(user);
		File latestIconFile = Warotter.getApplicationFile(fileName);		
		File[] caches = cacheDir.listFiles();
		/*
		 * URLからアイコンが更新されているかどうかの確認
		 */
		boolean needsCacheUpdate = true;
		if (iconCache.containsKey(user.getId()))
		{
			needsCacheUpdate = !iconCache.get(user.getId()).fileName.equals(fileName);
		}
		else
		{
			needsCacheUpdate = !latestIconFile.exists();
		}
		/*
		 * 更新が不要
		 */
		if (!needsCacheUpdate)
		{
			/*
			 * from メモリキャッシュ
			 */
			if (iconCache.containsKey(user.getId()))
			{
				Icon icon = iconCache.get(user.getId());
				model.icon = icon;
				if(viewIcon != null)
				{
					viewIcon.setImageBitmap(icon.use());
				}
			}
			/*
			 * from ファイルキャッシュ
			 */
			else
			{
				Options opt = new Options();
				opt.inPurgeable = true; // GC可能にする
				Bitmap bm = BitmapFactory.decodeFile(latestIconFile.getPath(), opt);
				Icon icon = new Icon(bm, fileName);
				putIconToMap(user, icon);
				model.icon = icon;
				if(viewIcon != null)
				{
					viewIcon.setImageBitmap(icon.use());
				}
			}
		}
		else
		{
			AsyncIconGetter.addTask(new AsyncIconGetter(user, viewIcon, model));
		}
	}

	public static String genIconName(User user)
	{
		return String.format("%1$s_%2$s", user.getId(), StringUtils.parseUrlToFileName(user.getProfileImageURL()));
	}

	public static void putIconToMap(User user, Icon icon)
	{
		if (iconCache.containsKey(user.getId()))
		{
			iconCache.remove(user.getId());
		}

		iconCache.put(user.getId(), icon);

		if (iconCache.size() > 200)
		{
			LinkedList<Icon> list = new LinkedList<IconCaches.Icon>(iconCache.values());
			Collections.sort(list);
			list.removeLast();
		}
	}
	
	public static Bitmap getEmptyIcon()
	{
		Options opt = new Options();
		opt.inPurgeable = true; // GC可能にする
		Bitmap bm = BitmapFactory.decodeResource(Warotter.getResource(), R.drawable.icon_reflesh, opt);
		return bm;		
	}

	public static class Icon implements Comparable<Icon>
	{
		private Bitmap bitmap;
		public String fileName;
		public int count = 0;

		public Icon(Bitmap bitmap, String fileName)
		{
			this.bitmap = bitmap;
			this.fileName = fileName;
		}

		public Bitmap use()
		{
			count++;
			return bitmap;
		}

		@Override
		public int compareTo(Icon another)
		{
			return this.count > another.count ? 1 : (this.count == another.count ? 0 : -1);
		}

	}
}
