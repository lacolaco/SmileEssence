package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.TemplateActivity;
import android.app.Activity;
import android.content.Intent;

public class CommandEditTemplate extends MenuCommand
{

	private Activity activity;

	public CommandEditTemplate(Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "íËå^ï∂ÇÃä«óù";
	}

	@Override
	public void workOnUiThread()
	{
		Intent intent = new Intent(activity, TemplateActivity.class);
		activity.startActivity(intent);
	}

}
