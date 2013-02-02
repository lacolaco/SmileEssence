package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.async.ConcurrentAsyncTaskHelper;
import net.miz_hi.smileessence.menu.MenuItemBase;
import net.miz_hi.smileessence.menu.MenuItemParent;
import net.miz_hi.smileessence.menu.StatusMenuAddReply;
import net.miz_hi.smileessence.menu.StatusMenuCopyToClipboard;
import net.miz_hi.smileessence.menu.StatusMenuFavAndRetweet;
import net.miz_hi.smileessence.menu.StatusMenuOpenUrl;
import net.miz_hi.smileessence.menu.StatusMenuReview;
import net.miz_hi.smileessence.menu.StatusMenuWarotaRT;
import net.miz_hi.smileessence.menu.UserMenuFollow;
import net.miz_hi.smileessence.menu.UserMenuOpenFavstar;
import net.miz_hi.smileessence.menu.UserMenuOpenPage;
import net.miz_hi.smileessence.menu.UserMenuOpenProfiel;
import net.miz_hi.smileessence.menu.UserMenuRemove;
import net.miz_hi.smileessence.menu.UserMenuReply;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusViewFactory;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StatusMenuAdapter extends DialogAdapter
{
	private StatusModel model;
	Handler handler;
	
	public StatusMenuAdapter(Activity activity, StatusModel model)
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
			list.add(new StatusMenuFavAndRetweet(activity, this, model));
			list.add(new StatusMenuAddReply(activity, this, model));

			list.add(new StatusMenuWarotaRT(activity, this, model));
			list.add(new StatusMenuReview(activity, this, model));
			list.add(new StatusMenuCopyToClipboard(activity, this, model));
			
			if (!getURLMenu().isEmpty())
			{
				list.add(new MenuItemParent(activity, this, "URL‚ðŠJ‚­", getURLMenu()));
			}
			for (String name : getUsersList())
			{
				list.add(new MenuItemParent(activity, this, "@" + name, getUserMenu(getUsersList()).get(name)));
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
	
	private List<String> getUsersList()
	{
		List<String> list = new ArrayList<String>();
		list.add(model.screenName);
		if (model.userMentions != null)
		{
			for (UserMentionEntity e : model.userMentions)
			{
				if (!list.contains(e.getScreenName()))
				{
					list.add(e.getScreenName());
				}
			}
		}
		return list;
	}
	
	private Map<String, List<MenuItemBase>> getUserMenu(List<String> nameList)
	{
		Map<String, List<MenuItemBase>> map = new HashMap<String, List<MenuItemBase>>();
		for (String name : nameList)
		{
			ArrayList<MenuItemBase> list = new ArrayList<MenuItemBase>();
			list.add(new UserMenuReply(activity, this, name));
			list.add(new UserMenuOpenProfiel(activity, this, name));
			list.add(new UserMenuOpenPage(activity, this, name));
			list.add(new UserMenuOpenFavstar(activity, this, name));
			list.add(new UserMenuFollow(activity, this, name));
			list.add(new UserMenuRemove(activity, this, name));
			map.put(name, list);
		}
		return map;
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
					MainActivity.getInstance().openTweetViewToReply(model.screenName, model.statusId, false);
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
					ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncRetweetTask(model.statusId));	
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
					ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncFavoriteTask(model.statusId));
					dispose();
				}
			},20);
		}
	};
}
