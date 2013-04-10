package net.miz_hi.smileessence.data.template;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="templates")
public class Template
{
	
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String text;
	
	public Template()
	{
	}
	
	public Template(String text)
	{
		this.text = text;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
}
