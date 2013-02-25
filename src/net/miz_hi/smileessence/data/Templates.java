package net.miz_hi.smileessence.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Templates
{
		
	private static List<String> templates = new ArrayList<String>();
	
	static
	{
		readFromFile();
	}
	
	private static void readFromFile()
	{
		if(TemplateDB.instance().findAll() != null)
		{
			for(Template template : TemplateDB.instance().findAll())
			{
				templates.add(template.getText());
			}
		}
	}
	
	private static void writeToFile()
	{
		TemplateDB.instance().deleteAll();
		for(String template : templates)
		{
			TemplateDB.instance().save(new Template(template));
		}
	}

	public static boolean addTemplate(String template)
	{
		if(templates.contains(template))
		{
			return false;
		}
		else
		{
			templates.add(template);
			writeToFile();
			return true;
		}
	}
	
	public static void setTemplate(String template, int index)
	{
		templates.set(index, template);
		writeToFile();
	}	
	
	public static List<String> getTemplates()
	{
		return Collections.unmodifiableList(templates);
	}
}
