package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.extra.ExtraWords;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.data.template.Templates;
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
import android.widget.EditText;
import android.widget.TextView;

public class TemplateOnClickListener implements OnClickListener, OnLongClickListener
{
	private CustomListAdapter adapter;
	private Template template;
	private Activity activity;

	public TemplateOnClickListener(CustomListAdapter adapter, Activity activity, Template template)
	{
		this.adapter = adapter;
		this.activity = activity;
		this.template = template;
	}

	@Override
	public void onClick(final View v)
	{
		final EditText editText = new EditText(activity);
		editText.setText(template.getText());

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
						template.setText(newText);
						Templates.update();
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
						Templates.deleteTemplate(template);
						adapter.removeElement(template);
						adapter.forceNotifyAdapter();
						Templates.update();						
					}
				}.post();
			}
		});
		return true;
	}

}
