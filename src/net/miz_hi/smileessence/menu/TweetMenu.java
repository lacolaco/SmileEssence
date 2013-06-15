package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.post.CommandAppendHashtag;
import net.miz_hi.smileessence.command.post.CommandInsertText;
import net.miz_hi.smileessence.command.post.CommandMakeAnonymous;
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
		setTitle("投稿メニュー");
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
	public List<MenuElement> getElements(List<MenuElement> list)
	{
		MenuElement warota = new MenuElement(new CommandInsertText("ワロタｗ"));
		MenuElement morse = new MenuElement(new CommandParseMorse());
		MenuElement anonymous = new MenuElement(new CommandMakeAnonymous());
		list.add(warota);
		list.add(morse);
		list.add(anonymous);
		
		MenuElement template = new MenuElement("定型文");
		List<ICommand> templates = getTemplateMenu();
		if(!templates.isEmpty())
		{
			for (ICommand iCommand : templates)
			{
				template.addChild(new MenuElement(iCommand));
			}
			list.add(template);
		}
		
		MenuElement hashtag = new MenuElement("最近見たハッシュタグ");
		List<ICommand> hashtags = getHashtagMenu();
		if(!hashtags.isEmpty())
		{
			for (ICommand iCommand : hashtags)
			{
				hashtag.addChild(new MenuElement(iCommand));
			}
			list.add(hashtag);
		}
		
		
		return list;
	}


}