package net.miz_hi.smileessence.listener;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWords;
import net.miz_hi.smileessence.dialog.YesNoDialogHelper;
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

public class ExtraWordOnClickListener implements OnClickListener, OnLongClickListener
{
	private CustomListAdapter adapter;
	private ExtraWord ExtraWord;
	private Activity activity;

	public ExtraWordOnClickListener(CustomListAdapter adapter, Activity activity, ExtraWord ExtraWord)
	{
		this.adapter = adapter;
		this.activity = activity;
		this.ExtraWord = ExtraWord;
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
				editText.setText(ExtraWord.getText());

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
								ExtraWord.setText(newText);
								ExtraWords.update();
								adapter.clear();
								adapter.addAll(ExtraWords.getExtraWords());
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
								ExtraWords.deleteExtraWord(ExtraWord);
								adapter.clear();
								adapter.notifyDataSetChanged();
								adapter.addAll(ExtraWords.getExtraWords());
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
