package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.Template;
import net.miz_hi.smileessence.data.TemplateListAdapter;
import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.dialog.YesNoDialogHelper;
import net.miz_hi.smileessence.util.ColorUtils;
import net.miz_hi.smileessence.util.UiHandler;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class TemplateActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.templateactivity_layout);
		
		ListView listView = (ListView)findViewById(R.id.listView_template);
		ImageButton buttonAdd = (ImageButton)findViewById(R.id.imageButton_template_add);
		final TemplateListAdapter adapter = new TemplateListAdapter(this);
		listView.setAdapter(adapter);
		adapter.addAll(Templates.getTemplates());
		adapter.forceNotifyAdapter();
		buttonAdd.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				final EditText editText = new EditText(TemplateActivity.this);
				YesNoDialogHelper helper = new YesNoDialogHelper(TemplateActivity.this, "ï“èW");
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

								Template template = new Template(newText);
								Templates.addTemplate(template);
								Templates.update();
								adapter.addLast(template);
								adapter.forceNotifyAdapter();
								break;
							}
						}					
					}
				});
				helper.setTextPositive("åàíË");
				helper.setTextNegative("ÉLÉÉÉìÉZÉã");
				helper.createYesNoAlert().show();
			}
		});
	}
}
