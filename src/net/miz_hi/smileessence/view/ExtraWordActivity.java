package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWordListAdapter;
import net.miz_hi.smileessence.data.extra.ExtraWords;
import net.miz_hi.smileessence.dialog.YesNoDialogHelper;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ExtraWordActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extrawordactivity_layout);
		
		ListView listView = (ListView)findViewById(R.id.listView_extraword);
		ImageButton buttonAdd = (ImageButton)findViewById(R.id.imageButton_extraword_add);
		final ExtraWordListAdapter adapter = new ExtraWordListAdapter(this);
		listView.setAdapter(adapter);
		adapter.addAll(ExtraWords.getExtraWords());
		adapter.forceNotifyAdapter();
		buttonAdd.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				final EditText editText = new EditText(ExtraWordActivity.this);
				YesNoDialogHelper helper = new YesNoDialogHelper(ExtraWordActivity.this, "ï“èW");
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

								ExtraWord ExtraWord = new ExtraWord(newText);
								ExtraWords.addExtraWord(ExtraWord);
								ExtraWords.update();
								adapter.addLast(ExtraWord);
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
