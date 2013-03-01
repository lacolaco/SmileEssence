package net.miz_hi.smileessence.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.SimpleAsyncTask;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.IconCaches.Icon;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.view.MainActivity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

public class AsyncIconGetter implements Callable<Bitmap>
{
	private UserModel user;

	public AsyncIconGetter(UserModel user)
	{
		this.user = user;
	}

	@Override
	public Bitmap call() 
	{
		try
		{
			URL url = new URL(user.iconUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Options opt = new Options();
			opt.inPurgeable = true; // GC‰Â”\‚É‚·‚é
			Bitmap bm = BitmapFactory.decodeStream(input, null, opt);
			File file = Client.getApplicationFile(IconCaches.genIconName(user));
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 90, fos);
			fos.close();
			Icon icon = new Icon(bm, IconCaches.genIconName(user));
			IconCaches.putIconToMap(user.userId, icon);
			LogHelper.printD("icon get from web");
			return icon.use();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return IconCaches.getEmptyIcon();
		}
	}
}
