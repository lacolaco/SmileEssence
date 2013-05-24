package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.CommandAddTemplate;
import net.miz_hi.smileessence.command.CommandOpenUrl;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.post.CommandAppendHashtag;
import net.miz_hi.smileessence.command.status.StatusCommandChaseRelation;
import net.miz_hi.smileessence.command.status.StatusCommandClipboard;
import net.miz_hi.smileessence.command.status.StatusCommandCongrats;
import net.miz_hi.smileessence.command.status.StatusCommandCopy;
import net.miz_hi.smileessence.command.status.StatusCommandDelete;
import net.miz_hi.smileessence.command.status.StatusCommandFavAndRetweet;
import net.miz_hi.smileessence.command.status.StatusCommandFavorite;
import net.miz_hi.smileessence.command.status.StatusCommandNanigaja;
import net.miz_hi.smileessence.command.status.StatusCommandReply;
import net.miz_hi.smileessence.command.status.StatusCommandRetweet;
import net.miz_hi.smileessence.command.status.StatusCommandReview;
import net.miz_hi.smileessence.command.status.StatusCommandThankToFav;
import net.miz_hi.smileessence.command.status.StatusCommandTofuBuster;
import net.miz_hi.smileessence.command.status.StatusCommandTranslate;
import net.miz_hi.smileessence.command.status.StatusCommandUnOffFav;
import net.miz_hi.smileessence.command.status.StatusCommandUnOffRetweet;
import net.miz_hi.smileessence.command.status.StatusCommandUnfavorite;
import net.miz_hi.smileessence.command.status.StatusCommandWarotaRT;
import net.miz_hi.smileessence.command.user.UserCommandAddReply;
import net.miz_hi.smileessence.command.user.UserCommandOpenInfo;
import net.miz_hi.smileessence.command.user.UserCommandOpenTimeline;
import net.miz_hi.smileessence.command.user.UserCommandReply;
import net.miz_hi.smileessence.dialog.ExpandMenuDialog;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusViewFactory;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class StatusMenu extends ExpandMenuDialog
{
	private StatusModel model;

	public StatusMenu(Activity activity, StatusModel model)
	{
		super(activity);
		this.model = model;
		setTitle(getHeaderView());
	}
	
	private View getHeaderView()
	{
		View viewStatus = StatusViewFactory.getView(inflater, model);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		p.setMargins(5, 5, 5, 0);
		viewStatus.setLayoutParams(p);
		
		View commands = inflater.inflate(R.layout.statusmenu_header, null);
		View reply = commands.findViewById(R.id.statusmenu_reply);
		View retweet = commands.findViewById(R.id.statusmenu_retweet);
		View favorite = commands.findViewById(R.id.statusmenu_favorite);
		
		final StatusCommandReply commandReply = new StatusCommandReply(model);
		final StatusCommandRetweet commandRetweet = new StatusCommandRetweet(model);
		final StatusCommandFavorite commandFavorite = new StatusCommandFavorite(model);
		
		if(!commandRetweet.getDefaultVisibility())
		{
			retweet.setVisibility(View.INVISIBLE);
		}
		
		reply.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				commandReply.run();
				dispose();
			}
		});
		retweet.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				commandRetweet.run();
				dispose();
			}
		});
		favorite.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				commandFavorite.run();
				dispose();
			}
		});
		
		commands.setLayoutParams(p);
		
		LinearLayout header = new LinearLayout(activity);
		header.setOrientation(LinearLayout.VERTICAL);
		
		header.setBackgroundColor(Client.getColor(R.color.White));
		header.addView(viewStatus);
		header.addView(commands);
		return header;
	}

	public List<ICommand> getStatusMenu()
	{
		List<ICommand> list = new ArrayList<ICommand>();
		list.add(new StatusCommandDelete(model));
		list.add(new StatusCommandFavAndRetweet(model));
		list.add(new StatusCommandChaseRelation(model));
		list.add(new StatusCommandUnfavorite(model));
		list.add(new StatusCommandCopy(model));
		list.add(new StatusCommandTofuBuster(activity, model));
		list.add(new StatusCommandUnOffRetweet(model));
		list.add(new StatusCommandWarotaRT(model));
		list.add(new StatusCommandNanigaja(model));
		list.add(new StatusCommandUnOffFav(model));
		list.add(new StatusCommandThankToFav(model));
		list.add(new StatusCommandCongrats(model));
		list.add(new StatusCommandReview(activity, model));
		list.add(new StatusCommandTranslate(activity, model));
		list.add(new CommandAddTemplate(model.text));
		list.add(new StatusCommandClipboard(model));
		
		return list;
	}

	private List<ICommand> getURLMenu()
	{
		List<ICommand> list = new ArrayList<ICommand>();
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
	
	private List<ICommand> getHashtagMenu()
	{
		List<ICommand> list = new ArrayList<ICommand>();
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

	private Map<String, List<ICommand>> getUserMenu(List<String> userList)
	{
		Map<String, List<ICommand>> map = new HashMap<String, List<ICommand>>();
		for (String userName : userList)
		{
			ArrayList<ICommand> list = new ArrayList<ICommand>();
			list.add(new UserCommandReply(userName));
			list.add(new UserCommandAddReply(userName));
			list.add(new UserCommandOpenInfo(userName, activity));
			list.add(new UserCommandOpenTimeline(userName));
			map.put(userName, list);
		}
		return map;
	}

	@Override
	public List<List<ICommand>> getLists()
	{
		
		List<List<ICommand>> list = new ArrayList<List<ICommand>>();
					
		List<ICommand> url = getURLMenu();
		if (!url.isEmpty())
		{
			list.add(url);
		}
		
		List<ICommand> commands = getStatusMenu();
		list.add(commands);
		
		for (String name : getUsersList())
		{
			List<ICommand> user = getUserMenu(getUsersList()).get(name);
			list.add(user);
		}
		
		List<ICommand> hashtag = getHashtagMenu();
		if(!hashtag.isEmpty())
		{
			list.add(hashtag);
		}	

		return list;
	}

	@Override
	public List<String> getGroups()
	{
		List<String> list = new ArrayList<String>();
		List<ICommand> url = getURLMenu();
		if (!url.isEmpty())
		{
			list.add("URL");
		}		
		list.add("コマンド");
		for (String name : getUsersList())
		{
			list.add("@" + name);
		}
		List<ICommand> hashtag = getHashtagMenu();
		if(!hashtag.isEmpty())
		{
			list.add("ハッシュタグ");
		}	
		return list;
	}
}
