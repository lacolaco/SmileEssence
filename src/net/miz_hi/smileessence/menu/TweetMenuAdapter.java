package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.CommandAppendHashtag;
import net.miz_hi.smileessence.command.CommandInsertText;
import net.miz_hi.smileessence.command.CommandMenuParent;
import net.miz_hi.smileessence.command.CommandParseMorse;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.data.Template;
import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.app.Dialog;

public class TweetMenuAdapter extends DialogAdapter
{

	public TweetMenuAdapter(Activity activity)
	{
		super(activity);
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{
		if (init)
		{
			list.clear();
			list.add(new CommandInsertText("ワロタｗ"));
			list.add(new CommandParseMorse());
			if(!getTemplateMenu().isEmpty())
			{
				list.add(new CommandMenuParent(this, "定型文", getTemplateMenu()));
			}
			if(!getHashtagMenu().isEmpty())
			{
				list.add(new CommandMenuParent(this, "最近見たハッシュタグ", getHashtagMenu()));
			}
			setTitle("メニュー");
		}

		return super.createMenuDialog();
	}
	
	private List<MenuCommand> getHashtagMenu()
	{
		List<MenuCommand> list = new ArrayList<MenuCommand>();
		for(String hashtag : StatusStore.getHashtagList())
		{
			list.add(new CommandAppendHashtag(hashtag));
		}
		return list;
	}
	
	private List<MenuCommand> getTemplateMenu()
	{
		List<MenuCommand> list = new ArrayList<MenuCommand>();
		for(Template template : Templates.getTemplates())
		{
			list.add(new CommandInsertText(template.getText()));
		}
		return list;
	}

}
