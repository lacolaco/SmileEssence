package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.menu.StatusMenuWarotaRT;
import net.miz_hi.smileessence.message.TweetMessage;
import net.miz_hi.smileessence.status.IconCaches;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusViewFactory;
import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusMenuAdapter extends DialogAdapter
{
	private StatusModel model;
	
	public StatusMenuAdapter(EventHandlerActivity activity, StatusModel status)
	{
		super(activity);
		this.model = status;
	}

	@Override
	public Dialog createMenuDialog()
	{	
		View viewStatus = StatusViewFactory.getView(layoutInflater, model);
		
		View viewCommands = layoutInflater.inflate(R.layout.dialog_statuscommand_layout, null);
		ImageView viewReply = (ImageView)viewCommands.findViewById(R.id.imageView_status_reply);
		ImageView viewRetweet = (ImageView)viewCommands.findViewById(R.id.imageView_status_retweet);
		ImageView viewFavorite = (ImageView)viewCommands.findViewById(R.id.imageView_status_favorite);
		
		final Handler handler = new Handler();
		viewReply.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
				v.invalidate();
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						activity.messenger.raise("tweet", new TweetMessage("@" + model.screenName + " "));
						dispose();
					}
				}, 20);

			}
		});
		viewRetweet.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
				v.invalidate();
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						AsyncRetweetTask.addTask(new AsyncRetweetTask(model.statusId, activity.getMainViewModel()));	
						dispose();
					}
				}, 20);
			}
		});
		viewFavorite.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				v.setBackgroundColor(Client.getResource().getColor(R.color.MetroBlue));
				v.invalidate();
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						AsyncFavoriteTask.addTask(new AsyncFavoriteTask(model.statusId, activity.getMainViewModel()));
						dispose();
					}
				},20);
			}
		});
		
		list.add(new StatusMenuWarotaRT(activity, this, model));

		return super.createMenuDialog(viewStatus, viewCommands);
	}

}
