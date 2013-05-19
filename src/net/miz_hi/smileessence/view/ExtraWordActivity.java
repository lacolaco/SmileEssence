package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWordListAdapter;
import net.miz_hi.smileessence.data.extra.ExtraWords;
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

public class ExtraWordActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listedit_layout);
		
		TextView titleView = (TextView)findViewById(R.id.listedit_title);
		titleView.setText("抽出ワードの管理");
		ListView listView = (ListView)findViewById(R.id.listedit_listview);
		ImageButton buttonAdd = (ImageButton)findViewById(R.id.listedit_addbutton);
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
				ContentDialog dialog = new ContentDialog(ExtraWordActivity.this, "編集");
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

								ExtraWord extraWord = new ExtraWord(newText);
								ExtraWords.addExtraWord(extraWord);
								adapter.addLast(extraWord);
								adapter.forceNotifyAdapter();
								break;
							}
						}					
					}
				});
				dialog.setTextPositive("決定");
				dialog.setTextNegative("キャンセル");
				dialog.create().show();
			}
		});
	}
}
