package net.miz_hi.smileessence.command;

import android.app.Activity;
import android.content.Intent;
import net.miz_hi.smileessence.view.activity.TemplateActivity;

public class CommandEditTemplate extends MenuCommand
{

    private Activity activity;

    public CommandEditTemplate(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "定型文の管理";
    }

    @Override
    public void workOnUiThread()
    {
        Intent intent = new Intent(activity, TemplateActivity.class);
        activity.startActivity(intent);
    }

}
