package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.menu.MenuItemClose;
import net.miz_hi.smileessence.menu.StatusMenuCopyToClipboard;
import net.miz_hi.smileessence.menu.StatusMenuFavAndRetweet;
import net.miz_hi.smileessence.menu.StatusMenuWarotaRT;
import net.miz_hi.smileessence.message.ReplyMessage;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusViewFactory;
import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StatusMenuAdapter extends DialogAdapter
{
	private StatusModel model;
	Handler handler;
	
	public StatusMenuAdapter(EventHandlerActivity activity, StatusModel status)
	{
		super(activity);
		handler = new Handler();
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
		
		viewReply.setOnClickListener(onClickReply);
		viewRetweet.setOnClickListener(onClickRetweet);
		viewFavorite.setOnClickListener(onClickFavorite);
		
		list.add(new StatusMenuWarotaRT(activity, this, model));
		list.add(new StatusMenuFavAndRetweet(activity, this, model));
		list.add(new StatusMenuCopyToClipboard(activity, this, model));
		
		list.add(new MenuItemClose(activity, this));

		return super.createMenuDialog(viewStatus, viewCommands);
	}
	
	private OnClickListener onClickReply = new OnClickListener()
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
					activity.messenger.raise("reply", new ReplyMessage(model.user, model.statusId));
					dispose();
				}
			}, 20);
		}
	};
	
	private OnClickListener onClickRetweet = new OnClickListener()
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
	};

	private OnClickListener onClickFavorite = new OnClickListener()
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
	};
}
