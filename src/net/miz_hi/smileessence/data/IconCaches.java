package net.miz_hi.smileessence.data;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncIconGetter;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.util.CountUpInteger;
import net.miz_hi.smileessence.util.StringUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

public class IconCaches
{

	private static ConcurrentHashMap<Long, Icon> iconCache = new ConcurrentHashMap<Long, Icon>(100);
	private static File cacheDir = Client.getApplication().getExternalCacheDir();
	private static Bitmap emptyIcon;
	private static CountUpInteger counter = new CountUpInteger(5);

	public static Icon getIcon(long id)
	{
		return iconCache.get(id);
	}

	public static void setIconBitmapToView(final UserModel user, final ImageView viewIcon)
	{
		String fileName = genIconName(user);
		File latestIconFile = Client.getApplicationFile(fileName);
		File[] caches = cacheDir.listFiles();
		/*
		 * URLからアイコンが更新されているかどうかの確認
		 */
		boolean needsCacheUpdate = true;
		if (iconCache.containsKey(user.userId))
		{
			needsCacheUpdate = !iconCache.get(user.userId).fileName.equals(fileName);
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
			if (iconCache.containsKey(user.userId))
			{
				final Icon icon = iconCache.get(user.userId);
				if (viewIcon != null)
				{
					new UiHandler()
					{

						@Override
						public void run()
						{
							viewIcon.setImageBitmap(icon.use());
						}
					}.post();
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
				final Icon icon = new Icon(bm, fileName);
				putIconToMap(user.userId, icon);
				if (viewIcon != null)
				{
					new UiHandler()
					{

						@Override
						public void run()
						{
							viewIcon.setImageBitmap(icon.use());
						}
					}.post();
				}
			}
		}
		else
		{
			new UiHandler()
			{

				@Override
				public void run()
				{
					new AsyncIconGetter(user, viewIcon).addToQueue();
				}
			}.post();
		}
	}

	public static String genIconName(UserModel user)
	{
		return String.format("%1$s_%2$s", user.userId, StringUtils.parseUrlToFileName(user.iconUrl));
	}

	public static void clearCache()
	{
		iconCache.clear();
	}

	public static void putIconToMap(long id, Icon icon)
	{

		if (iconCache.size() > 99 && counter.isOver())
		{

			LinkedList<Map.Entry> entries = new LinkedList<Map.Entry>(iconCache.entrySet());
			Collections.sort(entries, new Comparator()
			{

				@Override
				public int compare(Object o1, Object o2)
				{
					Map.Entry e1 = (Map.Entry) o1;
					Map.Entry e2 = (Map.Entry) o2;
					return ((Icon) e1.getValue()).compareTo((Icon) e2.getValue());
				}
			});
			while (iconCache.size() > 98)
			{
				iconCache.remove(entries.pollFirst().getKey());
			}
			counter.reset();
		}

		iconCache.put(id, icon);
		counter.countUp();
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
