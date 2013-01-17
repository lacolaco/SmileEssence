package net.miz_hi.warotter.model;

import java.util.Date;

import gueei.binding.Observable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.R;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.core.ViewModel;
import twitter4j.Status;
import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Html;

public class StatusModel implements Comparable<StatusModel>
{
	public long statusId;
	public long statusIdToShow;
	public Bitmap iconBitmap;
	public String screenName;
	public String name;
	public String text;
	public String source;
	public String retweetedBy;	
	public Date createdAt;
	public int backgroundColor;
	public int nameColor;

	public static StatusModel createInstance(long id)
	{
		Status st = StatusStore.get(id);
		if (st == null)
		{
			return null;
		}
		if (st.isRetweet())
		{
			return new StatusModel(st, st.getRetweetedStatus().getId());
		}
		else
		{
			return new StatusModel(st, st.getId());
		}
	}

	private StatusModel(Status status, long statusIdToShow)
	{
		statusId = status.getId();
		this.statusIdToShow = statusIdToShow;
		
		Status statusToShow = !isRetweet() ? status : StatusStore.get(statusIdToShow);

		if (isRetweet())
		{
			backgroundColor = Warotter.getResource().getColor(R.color.LightBlue);
			retweetedBy = "(RTed by "+ status.getUser().getScreenName() + ")";
		}
		else if (isMine())
		{
			nameColor = Warotter.getResource().getColor(R.color.DarkBlue);
		}
		else if (isReply())
		{
			backgroundColor = Warotter.getResource().getColor(R.color.LightRed);
		}
		screenName = statusToShow.getUser().getScreenName();
		name = statusToShow.getUser().getName();
		text = statusToShow.getText();
		source = "via " + Html.fromHtml(statusToShow.getSource()).toString();
		createdAt = statusToShow.getCreatedAt();
		IconCaches.setIconBitmap(statusToShow.getUser(), iconBitmap);
	}
	
	public boolean isMine()
	{
		return StatusStore.isMine(statusIdToShow);
	}
	
	public boolean isReply()
	{
		return StatusStore.isReply(statusIdToShow);
	}
	
	public boolean isRetweet()
	{
		return statusId != statusIdToShow;
	}

	@Override
	public int compareTo(StatusModel another)
	{
		return this.createdAt.compareTo(another.createdAt);
	}

}
