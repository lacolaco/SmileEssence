package net.miz_hi.smileessence.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.CommandAddTemplate;
import net.miz_hi.smileessence.command.CommandAppendHashtag;
import net.miz_hi.smileessence.command.CommandMenuParent;
import net.miz_hi.smileessence.command.CommandOpenUrl;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.command.StatusCommandAddReply;
import net.miz_hi.smileessence.command.StatusCommandChaseRelation;
import net.miz_hi.smileessence.command.StatusCommandClipboard;
import net.miz_hi.smileessence.command.StatusCommandCongrats;
import net.miz_hi.smileessence.command.StatusCommandCopy;
import net.miz_hi.smileessence.command.StatusCommandDelete;
import net.miz_hi.smileessence.command.StatusCommandFavAndRetweet;
import net.miz_hi.smileessence.command.StatusCommandNanigaja;
import net.miz_hi.smileessence.command.StatusCommandReview;
import net.miz_hi.smileessence.command.StatusCommandThankToFav;
import net.miz_hi.smileessence.command.StatusCommandTofuBuster;
import net.miz_hi.smileessence.command.StatusCommandUnOffFav;
import net.miz_hi.smileessence.command.StatusCommandUnOffRetweet;
import net.miz_hi.smileessence.command.StatusCommandWarotaRT;
import net.miz_hi.smileessence.command.UserCommandFollow;
import net.miz_hi.smileessence.command.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.UserCommandOpenPage;
import net.miz_hi.smileessence.command.UserCommandOpenProfiel;
import net.miz_hi.smileessence.command.UserCommandRemove;
import net.miz_hi.smileessence.command.UserCommandReply;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.util.TwitterManager;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.TweetView;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class StatusCommandDialog extends DialogAdapter
{
	private StatusModel model;

	public StatusCommandDialog(Activity activity, StatusModel model)
	{
		super(activity);
		this.model = model;
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{
		if (init)
		{
			View viewStatus = StatusViewFactory.getView(layoutInflater, model);

			View viewCommands = layoutInflater.inflate(R.layout.dialog_statuscommand_layout, null);
			ImageView viewReply = (ImageView) viewCommands.findViewById(R.id.imageView_status_reply);
			ImageView viewRetweet = (ImageView) viewCommands.findViewById(R.id.imageView_status_retweet);
			ImageView viewFavorite = (ImageView) viewCommands.findViewById(R.id.imageView_status_favorite);

			viewReply.setOnClickListener(onClickReply);
			viewRetweet.setOnClickListener(onClickRetweet);
			viewFavorite.setOnClickListener(onClickFavorite);
			
			if(model.user.isProtected)
			{
				viewRetweet.setVisibility(View.INVISIBLE);
			}
			else
			{
				viewRetweet.setVisibility(View.VISIBLE);
			}
			
			list.clear();
			
			for(MenuCommand item: getStatusMenu())
			{
				list.add(item);
			}

			if (!getURLMenu().isEmpty())
			{
				list.add(new CommandMenuParent(this, "URL‚ðŠJ‚­", getURLMenu()));
			}
			for(MenuCommand item : getHashtagMenu())
			{
				list.add(item);
			}
			for (String name : getUsersList())
			{
				list.add(new CommandMenuParent(this, "@" + name, getUserMenu(getUsersList()).get(name)));
			}
			setTitle(viewStatus, viewCommands);
		}

		return super.createMenuDialog();
	}
	
	public List<MenuCommand> getStatusMenu()
	{
		List<MenuCommand> list = new ArrayList<MenuCommand>();
		list.add(new StatusCommandAddReply(model));
		list.add(new StatusCommandDelete(model));
		list.add(new StatusCommandFavAndRetweet(model));
		list.add(new StatusCommandChaseRelation(model));
		list.add(new StatusCommandCopy(model));
		list.add(new StatusCommandUnOffRetweet(model));
		list.add(new StatusCommandWarotaRT(model));
		list.add(new StatusCommandNanigaja(model));
		list.add(new StatusCommandUnOffFav(model));
		list.add(new StatusCommandThankToFav(model));
		list.add(new StatusCommandCongrats(model));
		list.add(new StatusCommandReview(activity, model));
		list.add(new StatusCommandTofuBuster(activity, model));
		list.add(new CommandAddTemplate(model.text));
		list.add(new StatusCommandClipboard(model));
		
		return list;
	}

	private List<MenuCommand> getURLMenu()
	{
		List<MenuCommand> list = new ArrayList<MenuCommand>();
		if (model.urls != null)
		{
			for (URLEntity urlEntity : model.urls)
			{
				String url = urlEntity.getExpandedURL();
				if (url != null)
				{
					list.add(new CommandOpenUrl(activity, url));
				}
			}
		}
		if (model.medias != null)
		{
			for (MediaEntity mediaEntity : model.medias)
			{
				String url = mediaEntity.getMediaURL();
				if (url != null)
				{
					list.add(new CommandOpenUrl(activity, url));
				}
			}
		}
		return list;
	}
	
	private List<MenuCommand> getHashtagMenu()
	{
		List<MenuCommand> list = new ArrayList<MenuCommand>();
		if (model.hashtags != null)
		{
			for (HashtagEntity hashtag : model.hashtags)
			{
				list.add(new CommandAppendHashtag(hashtag.getText()));
			}
		}
		return list;
	}

	private List<String> getUsersList()
	{
		List<String> list = new ArrayList<String>();
		list.add(model.user.screenName);
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
		if(model.retweeter != null && !list.contains(model.retweeter))
		{
			list.add(model.retweeter.screenName);
		}
		return list;
	}

	private Map<String, List<MenuCommand>> getUserMenu(List<String> userList)
	{
		Map<String, List<MenuCommand>> map = new HashMap<String, List<MenuCommand>>();
		for (String userName : userList)
		{
			ArrayList<MenuCommand> list = new ArrayList<MenuCommand>();
			list.add(new UserCommandReply(userName));
			list.add(new UserCommandOpenProfiel(activity, userName));
			list.add(new UserCommandOpenPage(activity, userName));
			list.add(new UserCommandOpenFavstar(activity, userName));
			list.add(new UserCommandFollow(userName));
			list.add(new UserCommandRemove(userName));
			map.put(userName, list);
		}
		return map;
	}

	private OnClickListener onClickReply = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
			v.invalidate();
			new UiHandler()
			{

				@Override
				public void run()
				{
					TweetSystem.getInstance().setReply(model.screenName, model.statusId);
					TweetView.open();
					dispose();
				}
			}.postDelayed(20);
		}
	};

	private OnClickListener onClickRetweet = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
			v.invalidate();
			final Future<Boolean> resp = MyExecutor.submit(new AsyncRetweetTask(model.statusId));
			MyExecutor.execute(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						boolean b = resp.get();
						String str = b ? TwitterManager.MESSAGE_RETWEET_SUCCESS : TwitterManager.MESSAGE_RETWEET_DEPLICATE;
						ToastManager.show(str);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
			new UiHandler()
			{

				@Override
				public void run()
				{
					dispose();
				}
			}.postDelayed(20);
		}
	};

	private OnClickListener onClickFavorite = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
			v.invalidate();
			final Future<Boolean> resp = MyExecutor.submit(new AsyncFavoriteTask(model.statusId));
			MyExecutor.execute(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						boolean b = resp.get();
						String str = b ? TwitterManager.MESSAGE_FAVORITE_SUCCESS : TwitterManager.MESSAGE_FAVORITE_DEPLICATE;
						ToastManager.show(str);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
			new UiHandler()
			{

				@Override
				public void run()
				{
					dispose();
				}
			}.postDelayed(20);
		}
	};
}
