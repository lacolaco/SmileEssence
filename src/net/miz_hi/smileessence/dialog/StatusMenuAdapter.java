package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.message.TweetMessage;
import net.miz_hi.smileessence.model.Client;
import net.miz_hi.smileessence.model.IconCaches;
import net.miz_hi.smileessence.model.StatusModel;
import net.miz_hi.smileessence.statusmenu.StatusMenuWarotaRT;
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
		View viewStatus = layoutInflater.inflate(R.layout.status_layout, null);
		ImageView viewIcon = (ImageView)viewStatus.findViewById(R.id.imageView_icon);
		TextView viewScreenName = (TextView)viewStatus.findViewById(R.id.textView_screenName);
		TextView viewName = (TextView)viewStatus.findViewById(R.id.textView_name);
		TextView viewText = (TextView)viewStatus.findViewById(R.id.textView_text);
		TextView viewSource = (TextView)viewStatus.findViewById(R.id.textView_source);
		TextView viewRetweetedBy = (TextView)viewStatus.findViewById(R.id.textView_retweetedBy);
		TextView viewCreatedAt = (TextView)viewStatus.findViewById(R.id.textView_createdAt);
		if(!model.isRetweet && !model.isReply())
		{
			model.backgroundColor = Client.getResource().getColor(R.color.White);
		}
		viewStatus.setBackgroundColor(model.backgroundColor);
		if(model.icon == null)
		{
			viewIcon.setImageBitmap(IconCaches.getEmptyIcon());
			IconCaches.setIconBitmapToView(model.getUserToShow(), viewIcon, model);
		}
		else
		{
			viewIcon.setImageBitmap(model.icon.use());
		}
		viewScreenName.setText(model.screenName);
		viewScreenName.setTextColor(model.nameColor);
		viewName.setText(model.name);
		viewName.setTextColor(model.nameColor);
		viewText.setText(model.text);
		viewSource.setText(model.source);
		viewCreatedAt.setText(model.createdAtString);
		viewRetweetedBy.setText(model.retweetedBy);
		if(model.isRetweet)
		{
			viewRetweetedBy.setVisibility(View.VISIBLE);
		}
		else
		{
			viewRetweetedBy.setVisibility(View.GONE);
		}
		
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
