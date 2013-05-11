package net.miz_hi.smileessence.data.page;

import java.util.Map;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="pages")
public class Page
{
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private String className;
	@DatabaseField
	private String data;
	
	public Page()
	{
	}
	
	public Page(String className, String data)
	{
		this.className = className;
		this.data = data;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}
	
	
}
