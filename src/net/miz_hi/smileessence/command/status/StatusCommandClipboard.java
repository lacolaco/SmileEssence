package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import android.content.ClipboardManager;
import android.content.Context;

public class StatusCommandClipboard extends StatusCommand implements IHideable
{

	public StatusCommandClipboard(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "クリップボードにコピー";
	}

	@Override
	public void workOnUiThread()
	{
		try
		{
			ClipboardManager manager = (ClipboardManager) Client.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
			manager.setText(status.text);
			Notifier.info("�R�s�[���܂���");
		}
		catch (Exception e)
		{
			Notifier.alert("�R�s�[���s���܂���");
		}
	}

}
