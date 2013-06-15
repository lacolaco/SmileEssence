package net.miz_hi.smileessence.command;

import android.app.Activity;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.ListFragment;
import twitter4j.UserList;
import twitter4j.api.ListsResources;

public class CommandOpenUserList extends MenuCommand
{

	UserList userList;
	
	public CommandOpenUserList(UserList userList)
	{
		this.userList = userList;
	}
	
	@Override
	public String getName()
	{
		return userList.getFullName();
	}

	@Override
	public void workOnUiThread()
	{
		ListFragment fragment = ListFragment.newInstance(userList.getFullName(), userList.getId());
		MainActivity.addPage(fragment);
		MainActivity.moveViewPage(MainActivity.getPagerCount());
	}

}
