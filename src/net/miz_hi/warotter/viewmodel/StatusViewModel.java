package net.miz_hi.warotter.viewmodel;

import net.miz_hi.warotter.R;
import net.miz_hi.warotter.Warotter;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.model.StatusStore;
import net.miz_hi.warotter.model.IconCaches;

import android.graphics.Bitmap;
import android.text.Html;
import gueei.binding.Observable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import twitter4j.Status;

public class StatusViewModel extends ViewModel
{
	public long statusId;
	public long retweetSourceId;
	public boolean isRelpy = false;
	public IntegerObservable backgroundColor = new IntegerObservable(Warotter.getResource().getColor(R.color.White));
	public IntegerObservable nameColor = new IntegerObservable(Warotter.getResource().getColor(R.color.ThickGreen));
	public StringObservable screenName = new StringObservable();
	public StringObservable name = new StringObservable();
	public StringObservable text = new StringObservable();
	public StringObservable via = new StringObservable();
	public StringObservable retweetedBy = new StringObservable();
	public BooleanObservable isRetweet = new BooleanObservable(false);
	public Observable<Bitmap> iconBitmap = new Observable<Bitmap>(Bitmap.class);

	public static StatusViewModel createInstance(long id)
	{
		Status st = StatusStore.get(id);
		if (st == null)
		{
			return null;
		}
		if (st.isRetweet())
		{
			return new StatusViewModel(st.getRetweetedStatus(), st.getId());
		}
		else
		{
			return new StatusViewModel(st, -1);
		}
	}
	
	private StatusViewModel(Status st, long sourceId)
	{
		statusId = st.getId();
		if(sourceId > 0)
		{
			retweetSourceId = sourceId;
			backgroundColor.set(Warotter.getResource().getColor(R.color.LightBlue));
			retweetedBy.set("retweeted by " + StatusStore.get(sourceId).getUser().getScreenName());
			isRetweet.set(true);
		}
		else if (StatusStore.isMine(statusId))
		{
			nameColor.set(Warotter.getResource().getColor(R.color.DarkBlue));
		}
		else if (StatusStore.isReply(statusId))
		{
			backgroundColor.set(Warotter.getResource().getColor(R.color.LightRed));
			isRelpy = true;
		}
		screenName.set(st.getUser().getScreenName());
		name.set(st.getUser().getName());
		text.set(st.getText());
		via.set("via " + Html.fromHtml(st.getSource()).toString());
		IconCaches.setIconBitmap(st.getUser(), iconBitmap);
	}

}
