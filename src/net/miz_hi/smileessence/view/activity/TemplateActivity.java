package net.miz_hi.smileessence.view.activity;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.data.template.TemplateListAdapter;
import net.miz_hi.smileessence.data.template.Templates;
import net.miz_hi.smileessence.dialog.ContentDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class TemplateActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listedit_layout);
		
		TextView titleView = (TextView)findViewById(R.id.listedit_title);
		titleView.setText("íËå^ï∂ÇÃä«óù");		
		ListView listView = (ListView)findViewById(R.id.listedit_listview);
		ImageButton buttonAdd = (ImageButton)findViewById(R.id.listedit_addbutton);
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
				ContentDialog dialog = new ContentDialog(TemplateActivity.this, "ï“èW");
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
				dialog.setTextPositive("åàíË");
				dialog.setTextNegative("ÉLÉÉÉìÉZÉã");
				dialog.create().show();
			}
		});
	}
}
