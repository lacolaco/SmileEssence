package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class ProgressDialogHelper
{
	

	public static ProgressDialog makeProgressDialog(final Activity activity, ProgressDialog pd)
	{
		pd.setIndeterminate(false);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMax(100);
		pd.setMessage("Please Wait...");
		pd.setOnCancelListener(new OnCancelListener()
		{

			@Override
			public void onCancel(DialogInterface arg0)
			{
				activity.finish();							
			}
		});
		pd.incrementProgressBy(0);
		pd.incrementSecondaryProgressBy(0);
		pd.setCancelable(true);
		return pd;
	}

}
