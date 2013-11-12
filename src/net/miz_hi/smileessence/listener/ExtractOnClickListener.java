package net.miz_hi.smileessence.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWordManager;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.dialog.ContentDialog;
import net.miz_hi.smileessence.util.CustomListAdapter;
import net.miz_hi.smileessence.util.UiHandler;

public class ExtractOnClickListener implements OnClickListener, OnLongClickListener
{

    private CustomListAdapter<ExtraWord> adapter;
    private ExtraWord extraWord;
    private Activity activity;

    public ExtractOnClickListener(CustomListAdapter<ExtraWord> adapter, Activity activity, ExtraWord extraWord)
    {
        this.adapter = adapter;
        this.activity = activity;
        this.extraWord = extraWord;
    }

    @Override
    public void onClick(final View v)
    {
        final EditText editText = new EditText(activity);
        editText.setText(extraWord.getText());

        ContentDialog dialog = new ContentDialog(activity, "編集");
        dialog.setContentView(editText);
        dialog.setOnClickListener(new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_NEGATIVE:
                    {
                        break;
                    }
                    case DialogInterface.BUTTON_POSITIVE:
                    {
                        String newText = editText.getText().toString();
                        extraWord.setText(newText);
                        ExtraWordManager.addExtractWord(extraWord);
                        adapter.clear();
                        adapter.addAll(ExtraWordManager.getExtraWords());
                        adapter.forceNotifyAdapter();
                        break;
                    }
                }
            }
        });
        dialog.create().show();
    }

    @Override
    public boolean onLongClick(final View v)
    {
        ConfirmDialog.show(activity, "削除してよろしいですか？", new Runnable()
        {

            @Override
            public void run()
            {
                new UiHandler()
                {

                    @Override
                    public void run()
                    {
                        ExtraWordManager.deleteExtraWord(extraWord);
                        adapter.clear();
                        adapter.addAll(ExtraWordManager.getExtraWords());
                        adapter.forceNotifyAdapter();
                    }
                }.post();
            }
        });
        return true;
    }

}