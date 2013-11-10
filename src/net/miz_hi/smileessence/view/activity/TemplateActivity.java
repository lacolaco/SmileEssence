package net.miz_hi.smileessence.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.data.template.TemplateListAdapter;
import net.miz_hi.smileessence.data.template.TemplateManager;
import net.miz_hi.smileessence.dialog.ContentDialog;

public class TemplateActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listedit_layout);

        TextView titleView = (TextView) findViewById(R.id.listedit_title);
        titleView.setText("定型文の管理");
        ListView listView = (ListView) findViewById(R.id.listedit_listview);
        ImageButton buttonAdd = (ImageButton) findViewById(R.id.listedit_addbutton);
        final TemplateListAdapter adapter = new TemplateListAdapter(this);
        listView.setAdapter(adapter);
        adapter.addAll(TemplateManager.getTemplates());
        adapter.forceNotifyAdapter();
        buttonAdd.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                final EditText editText = new EditText(TemplateActivity.this);
                ContentDialog dialog = new ContentDialog(TemplateActivity.this, "編集");
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

                                Template template = new Template(newText);
                                TemplateManager.addTemplate(template);
                                adapter.clear();
                                adapter.addAll(TemplateManager.getTemplates());
                                adapter.forceNotifyAdapter();
                                break;
                            }
                        }
                    }
                });
                dialog.setTextPositive("決定");
                dialog.setTextNegative("キャンセル");
                dialog.create().show();
            }
        });
    }
}