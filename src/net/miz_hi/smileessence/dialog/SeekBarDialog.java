package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import net.miz_hi.smileessence.R;

public class SeekBarDialog
{

    private Activity activity;
    private String title, text;
    private OnClickListener listener;
    private int seekBarMax;
    private int seekBarStart = 1;
    private View dialogView;
    private TextView textView, levelView;
    private SeekBar seekbar;
    private int levelCorrect = 0;

    public SeekBarDialog(Activity activity, String title)
    {
        this.activity = activity;
        this.title = title;
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
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

    public void setLevelCorrect(int addition)
    {
        this.levelCorrect = addition;
    }

    public int getProgress()
    {
        return this.seekbar.getProgress();
    }

    public AlertDialog createSeekBarDialog()
    {
        ContentDialog dialog = new ContentDialog(activity, title);
        textView.setText(text);
        seekbar.setMax(seekBarMax);
        seekbar.setProgress(seekBarStart);
        levelView.setText(Integer.toString(seekBarStart + levelCorrect));
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            @Override
            public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
            {
                levelView.setText(String.valueOf(i + levelCorrect));
            }
        });

        dialog.setContentView(dialogView);
        dialog.setOnClickListener(listener);
        return dialog.create();
    }

}
