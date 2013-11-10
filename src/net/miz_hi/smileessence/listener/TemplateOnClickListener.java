package net.miz_hi.smileessence.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.data.template.TemplateManager;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.dialog.ContentDialog;
import net.miz_hi.smileessence.util.CustomListAdapter;
import net.miz_hi.smileessence.util.UiHandler;

public class TemplateOnClickListener implements OnClickListener, OnLongClickListener
{

    private CustomListAdapter<Template> adapter;
    private Template template;
    private Activity activity;

    public TemplateOnClickListener(CustomListAdapter<Template> adapter, Activity activity, Template template)
    {
        this.adapter = adapter;
        this.activity = activity;
        this.template = template;
    }

    @Override
    public void onClick(final View v)
    {
        final EditText editText = new EditText(activity);
        editText.setText(template.getText());

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
                        template.setText(newText);
                        TemplateManager.addTemplate(template);
                        adapter.clear();
                        adapter.addAll(TemplateManager.getTemplates());
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
                        TemplateManager.deleteTemplate(template);
                        adapter.clear();
                        adapter.addAll(TemplateManager.getTemplates());
                        adapter.forceNotifyAdapter();
                    }
                }.post();
            }
        });
        return true;
    }

}