package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import net.miz_hi.smileessence.R;

public class SingleButtonDialog
{

    private Activity activity;
    private String title;
    private String text;
    private Runnable onClick;

    public SingleButtonDialog(Activity activity)
    {
        this.activity = activity;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Dialog create()
    {
        final Dialog dialog = new Dialog(activity);
        dialog.setTitle(title);
        Button button = (Button) activity.getLayoutInflater().inflate(R.layout.simplebutton, null);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (onClick != null)
                {
                    onClick.run();
                    dialog.dismiss();
                }
            }
        });
        dialog.addContentView(button, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return dialog;
    }

    public void setOnClick(Runnable runnable)
    {
        onClick = runnable;
    }

    public static void show(Activity activity, String title, String text, Runnable onClick)
    {
        SingleButtonDialog dialog = new SingleButtonDialog(activity);
        dialog.setTitle(title);
        dialog.setText(text);
        dialog.setOnClick(onClick);
        dialog.create().show();
    }

}
