package net.miz_hi.warotter.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.miz_hi.warotter.WarotterApplication;
import net.miz_hi.warotter.model.IconCaches;
import net.miz_hi.warotter.model.StatusModel;
import net.miz_hi.warotter.model.IconCaches.Icon;
import net.miz_hi.warotter.model.Warotter;
import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsyncIconGetter extends ConcurrentAsyncTask<Icon>
{	
	private User user;
	private ImageView viewIcon;
	private StatusModel model;

	public AsyncIconGetter(User user, ImageView viewIcon, StatusModel model)
	{
		this.user = user;
		this.viewIcon = viewIcon;
		this.model = model;
	}
		
	@Override
	protected Icon doInBackground(Object... params)
	{
		try
		{
			URL url = new URL(user.getProfileImageURL());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Options opt = new Options();
			opt.inPurgeable = true; // GC‰Â”\‚É‚·‚é
			Bitmap bm = BitmapFactory.decodeStream(input, null, opt);
			File file = Warotter.getApplicationFile(IconCaches.genIconName(user));
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 90, fos);
			fos.close();
			Icon icon = new Icon(bm, file.getName());
			IconCaches.putIconToMap(user, icon);
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
		model.icon = result;
		if(viewIcon != null)
		{
			viewIcon.setImageBitmap(result.use());
		}
	}

}
