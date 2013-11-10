package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.template.TemplateManager;
import net.miz_hi.smileessence.notification.Notificator;

public class CommandAddTemplate extends MenuCommand
{

    private String text;

    public CommandAddTemplate(String text)
    {
        this.text = text;
    }

    @Override
    public String getName()
    {
        return "定型文に追加";
    }

    @Override
    public void workOnUiThread()
    {
        TemplateManager.addTemplate(text);
        Notificator.info("追加しました");
    }
}