package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.async.ConcurrentAsyncTaskHelper;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.menu.MenuItemBase;
import net.miz_hi.smileessence.menu.MenuItemClose;
import net.miz_hi.smileessence.menu.MenuItemParent;
import net.miz_hi.smileessence.menu.StatusMenuCopyToClipboard;
import net.miz_hi.smileessence.menu.StatusMenuFavAndRetweet;
import net.miz_hi.smileessence.menu.StatusMenuOpenUrl;
import net.miz_hi.smileessence.menu.StatusMenuWarotaRT;
import net.miz_hi.smileessence.message.ReplyMessage;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusViewFactory;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StatusMenuAdapter extends DialogAdapter
{
	private StatusModel model;
	Handler handler;
	
	public StatusMenuAdapter(EventHandlerActivity activity, StatusModel model)
	{
		super(activity);
		handler = new Handler();
		this.model = model;
	}
	
	@Override
	public Dialog createMenuDialog(boolean init)
	{	
		View viewStatus = StatusViewFactory.getView(layoutInflater, model);
		
		View viewCommands = layoutInflater.inflate(R.layout.dialog_statuscommand_layout, null);
		ImageView viewReply = (ImageView)viewCommands.findViewById(R.id.imageView_status_reply);
		ImageView viewRetweet = (ImageView)viewCommands.findViewById(R.id.imageView_status_retweet);
		ImageView viewFavorite = (ImageView)viewCommands.findViewById(R.id.imageView_status_favorite);
		
		viewReply.setOnClickListener(onClickReply);
		viewRetweet.setOnClickListener(onClickRetweet);
		viewFavorite.setOnClickListener(onClickFavorite);

		if(init)
		{
			list.clear();
			list.add(new StatusMenuWarotaRT(activity, this, model));

			list.add(new StatusMenuFavAndRetweet(activity, this, model));
			list.add(new StatusMenuCopyToClipboard(activity, this, model));
			if (!getURLMenu().isEmpty())
			{
				list.add(new MenuItemParent(activity, this, "URL‚ðŠJ‚­", getURLMenu()));
			}
		}

		return super.createMenuDialog(viewStatus, viewCommands);
	}
	
	private List<MenuItemBase> getURLMenu()
	{
		List<MenuItemBase> list = new ArrayList<MenuItemBase>();
		if (model.urls != null)
		{
			for (URLEntity urlEntity : model.urls)
			{
				String url = urlEntity.getExpandedURL();
				if (url != null)
					list.add(new StatusMenuOpenUrl(activity, this, model, url));
			}
		}
		if (model.medias != null)
		{
			for (MediaEntity mediaEntity : model.medias)
			{
				String url = mediaEntity.getExpandedURL();
				if (url != null)
					list.add(new StatusMenuOpenUrl(activity, this, model, url));
			}
		}
		return list;
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
					ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncRetweetTask(model.statusId, activity.getMainViewModel()));	
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
					ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncFavoriteTask(model.statusId, activity.getMainViewModel()));
					dispose();
				}
			},20);
		}
	};
}
