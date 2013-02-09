package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.content.Context;
import android.text.ClipboardManager;

public class StatusMenuCopyToClipboard extends StatusMenuItemBase
{

	public StatusMenuCopyToClipboard(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "本文をクリップボードへコピー";
	}

	@Override
	public void work()
	{
		try
		{
			ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
			manager.setText(model.text);
			toast("コピーしました");
		}
		catch (Exception e)
		{
			toast("コピー失敗しました");
		}
	}
}
