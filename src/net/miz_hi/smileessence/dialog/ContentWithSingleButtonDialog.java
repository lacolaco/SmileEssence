package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

public class ContentWithSingleButtonDialog
{

    private Activity activity;
    private View view;

    public ContentWithSingleButtonDialog(Activity activity, View view)
    {
        this.activity = activity;
        this.view = view;
    }

    public Dialog create()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        dialog.setTitle(null);
        dialog.setView(view);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        return dialog.create();
    }

}
