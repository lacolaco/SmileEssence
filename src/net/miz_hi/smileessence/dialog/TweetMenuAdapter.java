package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.data.Template;
import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.menu.MenuItemBase;
import net.miz_hi.smileessence.menu.MenuItemParent;
import net.miz_hi.smileessence.menu.TweetMenuHashtag;
import net.miz_hi.smileessence.menu.TweetMenuMorse;
import net.miz_hi.smileessence.menu.TweetMenuTemplate;
import net.miz_hi.smileessence.menu.TweetMenuWarota;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;
import android.app.Dialog;

public class TweetMenuAdapter extends DialogAdapter
{
	private TweetViewManager manager;

	public TweetMenuAdapter(Activity activity, TweetViewManager manager)
	{
		super(activity);
		this.manager = manager;
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{
		if (init)
		{
			list.clear();
			list.add(new TweetMenuWarota(activity, this, manager));
			list.add(new TweetMenuMorse(activity, this, manager));
			if(!getTemplateMenu().isEmpty())
			{
				list.add(new MenuItemParent(activity, this, "定型文", getTemplateMenu()));
			}
			if(!getHashtagMenu().isEmpty())
			{
				list.add(new MenuItemParent(activity, this, "最近見たハッシュタグ", getHashtagMenu()));
			}
			setTitle("メニュー");
		}

		return super.createMenuDialog();
	}
	
	private List<MenuItemBase> getHashtagMenu()
	{
		List<MenuItemBase> list = new ArrayList<MenuItemBase>();
		for(String hashtag : StatusStore.getHashtagList())
		{
			list.add(new TweetMenuHashtag(activity, this, manager, hashtag));
		}
		return list;
	}
	
	private List<MenuItemBase> getTemplateMenu()
	{
		List<MenuItemBase> list = new ArrayList<MenuItemBase>();
		for(Template template : Templates.getTemplates())
		{
			list.add(new TweetMenuTemplate(activity, this, manager, template.getText()));
		}
		return list;
	}

}
