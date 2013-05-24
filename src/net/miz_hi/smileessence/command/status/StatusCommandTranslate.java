package net.miz_hi.smileessence.command.status;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.status.StatusModel;

public class StatusCommandTranslate extends StatusCommand implements IHideable
{

	private Activity activity;
	
	public StatusCommandTranslate(Activity activity, StatusModel status)
	{
		super(status);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "日本語に直す";
	}

	@Override
	public void workOnUiThread()
	{
		try
		{
			String query = URLEncoder.encode(status.text, "UTF-8");
			String url = "http://translate.google.co.jp/m/translate?q=" + query;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			activity.startActivity(intent);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return;
		}
		
	}
}