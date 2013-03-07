package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import android.content.Context;
import android.text.ClipboardManager;

public class StatusCommandClipboard extends StatusCommand implements IHideable
{

	private static boolean isVisible = true;

	public StatusCommandClipboard(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "本文をクリップボードへコピー";
	}

	@Override
	public void workOnUiThread()
	{
		try
		{
			ClipboardManager manager = (ClipboardManager) Client.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
			manager.setText(status.text);
			ToastManager.getInstance().toast("コピーしました");
		}
		catch (Exception e)
		{
			ToastManager.getInstance().toast("コピー失敗しました");
		}
	}

	@Override
	public boolean getIsVisible()
	{
		return isVisible;
	}

	@Override
	public void setIsVisible(boolean value)
	{
		isVisible = value;
	}
}
