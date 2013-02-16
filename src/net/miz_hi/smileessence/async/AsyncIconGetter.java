package net.miz_hi.smileessence.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.SimpleAsyncTask;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.IconCaches.Icon;
import net.miz_hi.smileessence.data.UserModel;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

public class AsyncIconGetter extends SimpleAsyncTask<Bitmap>
{
	private UserModel user;
	private ImageView viewIcon;
	private long tag;

	public AsyncIconGetter(UserModel user, ImageView viewIcon)
	{
		this.user = user;
		this.viewIcon = viewIcon;
		this.tag = user.userId;
	}

	@Override
	protected Bitmap doInBackground(Object... params)
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
			return bm;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return IconCaches.getEmptyIcon();
		}
	}

	@Override
	protected void onPostExecute(Bitmap result)
	{
		Icon icon = new Icon(result, IconCaches.genIconName(user));
		IconCaches.putIconToMap(user.userId, icon);
		if (viewIcon != null && viewIcon.getTag() != null && viewIcon.getTag().equals(tag))
		{
			viewIcon.setImageBitmap(icon.use());
			viewIcon.invalidate();
		}
	}
}
