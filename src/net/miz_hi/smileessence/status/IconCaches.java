package net.miz_hi.smileessence.status;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncIconGetter;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

public class IconCaches
{

	private static ConcurrentHashMap<Long, Icon> iconCache = new ConcurrentHashMap<Long, Icon>(200);
	private static File cacheDir = Client.getApplication().getExternalCacheDir();
	private static Bitmap emptyIcon;

	public static void setIconBitmapToView(User user, ImageView viewIcon, StatusModel model)
	{
		String fileName = genIconName(user);
		File latestIconFile = Client.getApplicationFile(fileName);		
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

		if (iconCache.size() >= 200)
		{

			List<Map.Entry> entries = new ArrayList<Map.Entry>(iconCache.entrySet());
			Collections.sort(entries, new Comparator()
			{

				@Override
				public int compare(Object o1, Object o2)
				{
					Map.Entry e1 =(Map.Entry)o1;
					Map.Entry e2 =(Map.Entry)o2;
					return ((Icon)e1.getValue()).compareTo((Icon)e2.getValue());
				}
			});
			iconCache.remove(entries.get(0));
		}
	}

	public static Bitmap getEmptyIcon()
	{
		Options opt = new Options();
		opt.inPurgeable = true; // GC可能にする
		Bitmap bm = BitmapFactory.decodeResource(Client.getResource(), R.drawable.icon_reflesh, opt);
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
			return this.count > another.count ? -1 : (this.count == another.count ? 0 : 1);
		}

	}
}
