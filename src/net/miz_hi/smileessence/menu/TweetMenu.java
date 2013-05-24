package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.post.CommandAppendHashtag;
import net.miz_hi.smileessence.command.post.CommandInsertText;
import net.miz_hi.smileessence.command.post.CommandParseMorse;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.data.template.Templates;
import net.miz_hi.smileessence.dialog.ExpandMenuDialog;
import net.miz_hi.smileessence.status.StatusStore;
import android.app.Activity;

public class TweetMenu extends ExpandMenuDialog
{

	public TweetMenu(Activity activity)
	{
		super(activity);
		setTitle("ツイートメニュー");
	}

	private List<ICommand> getHashtagMenu()
	{
		List<ICommand> list = new ArrayList<ICommand>();
		for(String hashtag : StatusStore.getHashtagList())
		{
			list.add(new CommandAppendHashtag(hashtag));
		}
		return list;
	}
	
	private List<ICommand> getTemplateMenu()
	{
		List<ICommand> list = new ArrayList<ICommand>();
		for(Template template : Templates.getTemplates())
		{
			list.add(new CommandInsertText(template.getText()));
		}
		return list;
	}

	@Override
	public List<List<ICommand>> getLists()
	{
		List<List<ICommand>> list = new ArrayList<List<ICommand>>();
		
		//Basic
		List<ICommand> basic = new ArrayList<ICommand>();
		basic.add(new CommandInsertText("ワロタｗ"));
		basic.add(new CommandParseMorse());
		list.add(basic);
		
		List<ICommand> template = getTemplateMenu();
		if(!template.isEmpty())
		{
			list.add(template);
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
		list.add("基本");
		List<ICommand> template = getTemplateMenu();
		if(!template.isEmpty())
		{
			list.add("定型文");
		}
		List<ICommand> hashtag = getHashtagMenu();
		if(!hashtag.isEmpty())
		{
			list.add("最近見たハッシュタグ");
		}
		return list;
	}

}
