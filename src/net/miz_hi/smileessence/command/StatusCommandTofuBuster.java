package net.miz_hi.smileessence.command;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.Morse;

public class StatusCommandTofuBuster extends StatusCommand implements IHideable
{
	
	private Activity activity;

	public StatusCommandTofuBuster(Activity activity, StatusModel status)
	{
		super(status);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "TofuBuster";
	}

	@Override
	public void workOnUiThread()
	{
		String ACTION_SHOW_TEXT = "com.product.kanzmrsw.tofubuster.ACTION_SHOW_TEXT";
		String text = status.text;
		if (Morse.isMorse(text))
		{
			text = Morse.mcToJa(text);
		}
		Intent i = new Intent(ACTION_SHOW_TEXT);
		i.putExtra(Intent.EXTRA_TEXT, text);
		i.putExtra(Intent.EXTRA_SUBJECT, "SmileEssence");
		i.putExtra("isCopyEnabled", true);
		try
		{
			activity.startActivity(i);
		}
		catch(ActivityNotFoundException e)
		{
			ToastManager.show("TofuBusterがインストールされていません");
		}
	}
}
