package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.activity.MainActivity;

public class CommandMovePage extends MenuCommand
{

	String pageTitle;
	int position;
	
	public CommandMovePage(String title, int position)
	{
		this.pageTitle = title;
		this.position = position;
	}
	
	@Override
	public String getName()
	{
		return pageTitle;
	}

	@Override
	public void workOnUiThread()
	{
		MainActivity.moveViewPage(position);
	}

}
