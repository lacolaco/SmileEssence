package net.miz_hi.smileessence.data.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Templates
{
		
	private static List<Template> templates = new ArrayList<Template>();
	
	static
	{
		read();
	}
	
	private static void read()
	{
		templates.clear();
		if(TemplateDB.instance().findAll() != null)
		{
			for(Template template : TemplateDB.instance().findAll())
			{
				templates.add(template);
			}
		}
	}
	
	public static void update()
	{
		TemplateDB.instance().deleteAll();
		for(Template template : templates)
		{
			TemplateDB.instance().save(template);
		}
		read();
	}

	public static void addTemplate(String template)
	{
		templates.add(new Template(template));
		update();
	}
	
	public static void addTemplate(Template template)
	{
		templates.add(template);
		update();
	}


	public static void setTemplate(String template, int index)
	{
		templates.set(index, new Template(template));
		update();
	}
	
	public static void setTemplate(Template template, int index)
	{
		templates.set(index, template);
		update();
	}
	
	public static void deleteTemplate(Template template)
	{
		templates.remove(template);
		update();
	}
	
	public static List<Template> getTemplates()
	{
		return Collections.unmodifiableList(templates);
	}
}
