package net.miz_hi.warotter.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.Statuses;
import net.miz_hi.warotter.model.IconCaches;

import android.R.color;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.Converter;
import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import twitter4j.Status;

public class StatusViewModel extends ViewModel implements Comparable<StatusViewModel>
{
	private long statusId;
	public IntegerObservable backgroundColor = new IntegerObservable(Color.WHITE);
	public IntegerObservable nameColor = new IntegerObservable(Color.BLACK);
	public StringObservable screenName = new StringObservable();
	public StringObservable name = new StringObservable();
	public StringObservable text = new StringObservable();
	public StringObservable via = new StringObservable();
	public StringObservable createdAt = new StringObservable();
	public Observable<Bitmap> iconBitmap = new Observable<Bitmap>(Bitmap.class);
	
	public StatusViewModel(long id)
	{
		statusId = id;
		Status st = Statuses.get(statusId);
		if(Statuses.isMine(statusId))
		{
			backgroundColor.set(Warotter.getResource().getColor(R.color.LightGreen));
		}
		else if(Statuses.isReply(statusId))
		{
			backgroundColor.set(Warotter.getResource().getColor(R.color.LightRed));
		}
		screenName.set(st.getUser().getScreenName());
		name.set(st.getUser().getName());
		text.set(st.getText());
		via.set("via " + Html.fromHtml(st.getSource()).toString());
		createdAt.set(st.getCreatedAt().toLocaleString());
		IconCaches.setIconBitmap(st.getUser(), iconBitmap);		
	}

	@Override
	public int compareTo(StatusViewModel another)
	{
		long sub = statusId - another.statusId;
		return sub > 0 ? 1 : (sub < 0 ? -1 : 0);
	}
		
}
