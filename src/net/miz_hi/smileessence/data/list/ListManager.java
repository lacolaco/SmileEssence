package net.miz_hi.smileessence.data.list;

public class ListManager
{

    public static void addList(List template)
    {
        ListModel.instance().save(template);
    }

    public static void deleteList(int id)
    {
        ListModel.instance().delete(id);
    }

    public static java.util.List<List> getLists()
    {
        return ListModel.instance().findAll();
    }
}
