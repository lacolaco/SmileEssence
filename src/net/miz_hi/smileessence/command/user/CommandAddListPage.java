package net.miz_hi.smileessence.command.user;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.CommandOpenUserList;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.twitter.UserLists;
import net.miz_hi.smileessence.util.UiHandler;
import twitter4j.ResponseList;
import twitter4j.UserList;
import android.app.Activity;
import android.app.ProgressDialog;

public class CommandAddListPage extends MenuCommand
{
	
	Activity activity;

	public CommandAddListPage(Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
	public String getName()
	{
		return "リスト";
	}

	@Override
	public void workOnUiThread()
	{
		final ProgressDialog pd = ProgressDialog.show(activity, null, "更新中...", true);
		MyExecutor.execute(new Runnable()
		{
			public void run()
			{
				long cursor = -1;
				try
				{
					final List<UserList> lists = UserLists.getReadableLists(Client.getMainAccount());
					if(lists.isEmpty())
					{
						Notifier.alert("リストがありません");
					}
					else
					{
						final SimpleMenuDialog menu = new SimpleMenuDialog(activity, "リストを選択")
						{
							@Override
							public List<ICommand> getMenuList()
							{
								List<ICommand> commands = new ArrayList<ICommand>();
								for(UserList list : lists)
								{
									commands.add(new CommandOpenUserList(list));
								}
								return commands;
							}
						};
						new UiHandler()
						{

							@Override
							public void run()
							{
								menu.create().show();
							}
						}.post();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Notifier.alert("リストの取得に失敗しました");
				}
				pd.dismiss();
			}
		});
	}

}
