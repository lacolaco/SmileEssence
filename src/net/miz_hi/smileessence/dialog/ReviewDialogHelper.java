package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ReviewDialogHelper
{

	private Activity activity;
	private String title;
	private OnClickListener listener;
	private int seekBarMax;
	private int seekBarStart = 1;
	private LayoutInflater layoutInflater;
	private View dialogView;
	private TextView levelView;
	private SeekBar seekbar;
	private EditText editView;
	private int levelCorrect = 0;

	public ReviewDialogHelper(Activity activity, String title)
	{
		this.activity = activity;
		this.title = title;
		layoutInflater = LayoutInflater.from(activity);
		dialogView = layoutInflater.inflate(R.layout.dialog_review_layout, null);
		levelView = (TextView) dialogView.findViewById(R.id.textView_seekLevel);
		seekbar = (SeekBar) dialogView.findViewById(R.id.seekBar_seekDialog);
		editView = (EditText)dialogView.findViewById(R.id.editText_comment);
	}

	public void setOnClickListener(OnClickListener listener)
	{
		this.listener = listener;
	}

	public void setSeekBarMax(int i)
	{
		this.seekBarMax = i;
	}

	public void setSeekBarStart(int i)
	{
		this.seekBarStart = i;
	}
	
	public void setLevelCorrect(int addition)
	{
		this.levelCorrect = addition;
	}

	public int getProgress()
	{
		return this.seekbar.getProgress();
	}
	
	public String getText()
	{
		return this.editView.getText().toString();
	}

	public AlertDialog createSeekBarDialog()
	{
		seekbar.setMax(seekBarMax);
		seekbar.setProgress(seekBarStart);
		levelView.setText(Integer.toString(seekBarStart + levelCorrect));
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			public void onStopTrackingTouch(SeekBar seekbar)
			{
			}

			public void onStartTrackingTouch(SeekBar seekbar)
			{
			}

			public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
			{
				levelView.setText(String.valueOf(i + levelCorrect));
			}
		});

		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setView(dialogView);
		builder.setNegativeButton("ƒLƒƒƒ“ƒZƒ‹", null);
		builder.setPositiveButton("OK", listener);

		return builder.create();
	}

}
