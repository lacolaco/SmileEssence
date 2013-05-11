package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWords;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.dialog.ContentDialog;
import net.miz_hi.smileessence.util.ColorUtils;
import net.miz_hi.smileessence.util.CustomListAdapter;
import net.miz_hi.smileessence.util.UiHandler;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ExtraWordOnClickListener implements OnClickListener, OnLongClickListener
{
	private CustomListAdapter adapter;
	private ExtraWord extraWord;
	private Activity activity;

	public ExtraWordOnClickListener(CustomListAdapter adapter, Activity activity, ExtraWord extraWord)
	{
		this.adapter = adapter;
		this.activity = activity;
		this.extraWord = extraWord;
	}

	@Override
	public void onClick(final View v)
	{
		final EditText editText = new EditText(activity);
		editText.setText(extraWord.getText());

		ContentDialog dialog = new ContentDialog(activity, "ï“èW");
		dialog.setContentView(editText);
		dialog.setOnClickListener(new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
					case DialogInterface.BUTTON_NEGATIVE:
					{
						break;
					}
					case DialogInterface.BUTTON_POSITIVE:
					{
						String newText = editText.getText().toString();
						extraWord.setText(newText);
						ExtraWords.update();
						adapter.forceNotifyAdapter();
						break;
					}
				}					
			}
		});
		dialog.create().show();
	}

	@Override
	public boolean onLongClick(final View v)
	{
		ConfirmDialog.show(activity, "çÌèúÇµÇƒÇÊÇÎÇµÇ¢Ç≈Ç∑Ç©ÅH", new Runnable()
		{

			@Override
			public void run()
			{
				new UiHandler()
				{
					
					@Override
					public void run()
					{
						ExtraWords.deleteExtraWord(extraWord);
						adapter.removeElement(extraWord);
						adapter.forceNotifyAdapter();
						ExtraWords.update();						
					}
				}.post();
			}
		});
		return true;
	}

}
