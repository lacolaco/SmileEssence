package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.activity.ExtraWordActivity;
import net.miz_hi.smileessence.view.activity.TemplateActivity;
import android.app.Activity;
import android.content.Intent;

public class CommandEditExtraWord extends MenuCommand
{

	private Activity activity;

	public CommandEditExtraWord(Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "’Šoƒ[ƒh‚ÌŠÇ—";
	}

	@Override
	public void workOnUiThread()
	{
		Intent intent = new Intent(activity, ExtraWordActivity.class);
		activity.startActivity(intent);
	}

}
