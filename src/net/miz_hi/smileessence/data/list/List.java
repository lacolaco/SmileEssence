package net.miz_hi.smileessence.data.list;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "lists")
public class List
{

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private int listId;
    @DatabaseField
    private String name;

    public List()
    {
    }

    public List(int id, String name)
    {
        this.listId = id;
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getListId()
    {
        return listId;
    }

    public void setListId(int listId)
    {
        this.listId = listId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
