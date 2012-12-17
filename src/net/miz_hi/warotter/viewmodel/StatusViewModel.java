package net.miz_hi.warotter.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.model.Statuses;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.Html;
import gueei.binding.DependentObservable;
import gueei.binding.observables.StringObservable;
import twitter4j.Status;

public class StatusViewModel implements Comparable<StatusViewModel>
{
	private long statusId;
	public StringObservable screenName = new StringObservable();
	public StringObservable name = new StringObservable();
	public StringObservable text = new StringObservable();
	public StringObservable via = new StringObservable();
	public StringObservable createdAt = new StringObservable();
	public StringObservable iconUrl = new StringObservable();
	public DependentObservable<Bitmap> iconBitmap = new DependentObservable<Bitmap>(Bitmap.class, iconUrl)
	{
		
		@Override
		public Bitmap calculateValue(Object... arg0) throws Exception
		{
			File file = Warotter.getApplicationFile(Statuses.get(statusId).getUser().getScreenName() + ".png");
			if(file.exists())
			{
				return BitmapFactory.decodeFile(file.getPath());
			}
			else
			{
				try 
				{  
					URL url = new URL(iconUrl.get());  
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
					connection.setDoInput(true);  
					connection.connect();  
					InputStream input = connection.getInputStream();  
					Bitmap bm = BitmapFactory.decodeStream(input);
					FileOutputStream fos = new FileOutputStream(file);
					bm.compress(CompressFormat.PNG, 90, fos);
					fos.close();
					return bm;  
				} 
				catch (IOException e) 
				{  
					e.printStackTrace();  
					return null;  
				}
			}
		}
	};
	
	public StatusViewModel(long id)
	{
		statusId = id;
		Status st = Statuses.get(statusId);
		screenName.set(st.getUser().getScreenName());
		name.set(st.getUser().getName());
		text.set(st.getText());
		via.set("via " + Html.fromHtml(st.getSource()).toString());
		createdAt.set(st.getCreatedAt().toLocaleString());
		iconUrl.set(st.getUser().getProfileImageURL());
	}

	@Override
	public int compareTo(StatusViewModel another)
	{
		long sub = statusId - another.statusId;
		return sub > 0 ? 1 : (sub < 0 ? -1 : 0);
	}
		
}
