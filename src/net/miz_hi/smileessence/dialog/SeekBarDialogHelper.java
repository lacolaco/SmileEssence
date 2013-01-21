package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarDialogHelper
{

	private Activity activity;
	private String title, text;
	private OnClickListener listener;
	private int seekBarMax, seekBarStart = 1;
	private LayoutInflater layoutInflater;
	private View dialogView;
	private TextView textView, levelView;
	private SeekBar seekbar;

	public SeekBarDialogHelper(Activity activity, String title)
	{
		this.activity = activity;
		this.title = title;
		layoutInflater = LayoutInflater.from(activity);
		dialogView = layoutInflater.inflate(R.layout.dialog_seekbar_layout, null);
		textView = (TextView) dialogView.findViewById(R.id.textView_seekDialog);
		levelView = (TextView) dialogView.findViewById(R.id.textView_seekLevel);
		seekbar = (SeekBar) dialogView.findViewById(R.id.seekBar_seekDialog);
	}

	public void setText(String text)
	{
		this.text = text;
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

	public int getProgress()
	{
		return this.seekbar.getProgress();
	}

	public AlertDialog createSeekBarDialog()
	{
		textView.setText(text);
		textView.setTextColor(Client.getResource().getColor(R.color.White));
		seekbar.setMax(seekBarMax);
		seekbar.setProgress(seekBarStart);
		levelView.setText(Integer.toString(seekBarStart));
		levelView.setTextColor(Client.getResource().getColor(R.color.White));
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
				levelView.setText(String.valueOf(i));
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
