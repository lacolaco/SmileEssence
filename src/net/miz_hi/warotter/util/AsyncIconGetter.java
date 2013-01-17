package net.miz_hi.warotter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.miz_hi.warotter.model.IconCaches;
import net.miz_hi.warotter.model.IconCaches.Icon;
import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;

public class AsyncIconGetter extends AsyncTask<User, Integer, Icon>
{

	private File file;
	private Bitmap iconBitmap;

	public AsyncIconGetter(File file, Bitmap observable)
	{
		this.file = file;
		this.iconBitmap = observable;
	}

	@Override
	protected void onPreExecute()
	{
	}

	@Override
	protected Icon doInBackground(User... params)
	{
		try
		{
			URL url = new URL(params[0].getProfileImageURL());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Options opt = new Options();
			opt.inPurgeable = true; // GC‰Â”\‚É‚·‚é
			Bitmap bm = BitmapFactory.decodeStream(input, null, opt);
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 90, fos);
			fos.close();
			Icon icon = new Icon(bm, file.getName());
			IconCaches.putIconToMap(params[0], icon);
			return icon;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Icon result)
	{
		iconBitmap = result.use();
	}

}
