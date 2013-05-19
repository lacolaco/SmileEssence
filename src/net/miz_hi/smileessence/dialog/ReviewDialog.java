package net.miz_hi.smileessence.dialog;

import java.util.zip.Inflater;

import net.miz_hi.smileessence.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ReviewDialog
{

	private Activity activity;
	private String title;
	private OnClickListener listener;
	private LayoutInflater layoutInflater;
	private RatingBar ratingBar;
	private EditText editText;
	

	public ReviewDialog(Activity activity, String title)
	{
		this.activity = activity;
		this.title = title;
		layoutInflater = LayoutInflater.from(activity);
	}

	public void setOnClickListener(OnClickListener listener)
	{
		this.listener = listener;
	}

	public int getRates()
	{
		return (int) this.ratingBar.getRating();
	}

	public String getText()
	{
		return this.editText.getText().toString();
	}

	public AlertDialog create()
	{
		View v = layoutInflater.inflate(R.layout.dialog_review, null);
		ratingBar = (RatingBar) v.findViewById(R.id.review_rating);
		editText = (EditText) v.findViewById(R.id.review_comment);

		ContentDialog dialog = new ContentDialog(activity, title);
		dialog.setContentView(v);
		dialog.setOnClickListener(listener);
		return dialog.create();
	}

}
