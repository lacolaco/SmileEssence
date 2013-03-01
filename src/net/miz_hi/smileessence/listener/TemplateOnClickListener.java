package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.CustomListAdapter;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.Template;
import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.dialog.YesNoDialogHelper;
import net.miz_hi.smileessence.util.ColorUtils;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
		v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
		v.invalidate();
		new UiHandler()
		{
			@Override
			public void run()
			{
				v.setBackgroundColor(ColorUtils.setAlpha(Client.getColor(R.color.LightGray), 200));

				final EditText editText = new EditText(activity);
				editText.setText(template.getText());

				YesNoDialogHelper helper = new YesNoDialogHelper(activity, "編集");
				helper.setContentView(editText);
				helper.setOnClickListener(new DialogInterface.OnClickListener()
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
								adapter.clear();
								adapter.addAll(Templates.getTemplates());
								adapter.forceNotifyAdapter();
								v.getParent().requestLayout();
								break;
							}
						}					
					}
				});
				helper.setTextPositive("決定");
				helper.setTextNegative("キャンセル");
				helper.createYesNoAlert().show();
			}
		}.postDelayed(50);
	}

	@Override
	public boolean onLongClick(final View v)
	{
		v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
		v.invalidate();
		new UiHandler()
		{
			@Override
			public void run()
			{
				v.setBackgroundColor(ColorUtils.setAlpha(Client.getColor(R.color.LightGray), 200));

				TextView viewText = new TextView(activity);
				viewText.setText("削除してよろしいですか？");
				viewText.setTextColor(Client.getColor(R.color.White));
				YesNoDialogHelper helper = new YesNoDialogHelper(activity, "削除");
				helper.setContentView(viewText);
				helper.setOnClickListener(new DialogInterface.OnClickListener()
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
								Templates.deleteTemplate(template);
								adapter.clear();
								adapter.notifyDataSetChanged();
								adapter.addAll(Templates.getTemplates());
								adapter.notifyDataSetChanged();
								
								break;
							}
						}					
					}
				});
				helper.setTextPositive("はい");
				helper.setTextNegative("いいえ");
				helper.createYesNoAlert().show();
			}
		}.postDelayed(50);
		return true;
	}

}
