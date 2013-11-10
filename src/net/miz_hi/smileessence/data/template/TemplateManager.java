package net.miz_hi.smileessence.data.template;

import java.util.List;

public class TemplateManager
{

    public static void addTemplate(String template)
    {
        addTemplate(new Template(template));
    }

    public static void addTemplate(Template template)
    {
        TemplateModel.instance().save(template);
    }

    public static void deleteTemplate(Template template)
    {
        TemplateModel.instance().delete(template);
    }

    public static List<Template> getTemplates()
    {
        return TemplateModel.instance().findAll();
    }
}
