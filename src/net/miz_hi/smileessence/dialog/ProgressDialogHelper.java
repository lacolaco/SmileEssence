package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogHelper
{

	public static ProgressDialog makeProgressDialog(final Activity activity, ProgressDialog pd)
	{
		pd.setIndeterminate(false);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMax(100);
		pd.setMessage("Please Wait...");
		pd.incrementProgressBy(0);
		pd.incrementSecondaryProgressBy(0);
		pd.setCancelable(false);
		return pd;
	}

}
