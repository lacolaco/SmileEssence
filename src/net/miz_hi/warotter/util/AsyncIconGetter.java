package net.miz_hi.warotter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.miz_hi.warotter.model.IconCaches;

import gueei.binding.Observable;
import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;

public class AsyncIconGetter extends AsyncTask<User, Integer, Bitmap>
{

	private File file;
	private Observable<Bitmap> observable;

	public AsyncIconGetter(File file, Observable<Bitmap> observable)
	{
		this.file = file;
		this.observable = observable;
	}

	@Override
	protected void onPreExecute()
	{
	}

	@Override
	protected Bitmap doInBackground(User... params)
	{
		try
		{
			URL url = new URL(params[0].getProfileImageURL());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bm = BitmapFactory.decodeStream(input);
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 90, fos);
			fos.close();
			IconCaches.putIconToMap(params[0], bm);
			return bm;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result)
	{
		observable.set(result);
	}

}
