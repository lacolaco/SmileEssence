package net.miz_hi.smileessence.data.list;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.DBHelper;

import java.util.Collections;
import java.util.List;

public class ListModel
{

    private Context context;
    private static ListModel instance = new ListModel(Client.getApplication());

    private ListModel(Context context)
    {
        this.context = context;
    }

    public static ListModel instance()
    {
        return instance;
    }

    public void save(net.miz_hi.smileessence.data.list.List list)
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<net.miz_hi.smileessence.data.list.List, Integer> dao = helper.getDao(net.miz_hi.smileessence.data.list.List.class);
            dao.createOrUpdate(list);
        }
        catch (Exception e)
        {
            Log.e(ListModel.class.getSimpleName(), "error on save");
        }
        finally
        {
            helper.close();
        }
    }

    public void delete(int listId)
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<net.miz_hi.smileessence.data.list.List, Integer> dao = helper.getDao(net.miz_hi.smileessence.data.list.List.class);
            dao.delete(dao.queryBuilder().where().eq("listId", listId).query());
        }
        catch (Exception e)
        {
            Log.e(ListModel.class.getSimpleName(), "error on delete");
        }
        finally
        {
            helper.close();
        }
    }

    public void deleteAll()
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            for (net.miz_hi.smileessence.data.list.List list : findAll())
            {
                Dao<net.miz_hi.smileessence.data.list.List, Integer> dao = helper.getDao(net.miz_hi.smileessence.data.list.List.class);
                dao.delete(list);
            }
        }
        catch (Exception e)
        {
            Log.e(ListModel.class.getSimpleName(), "error on delete");
        }
        finally
        {
            helper.close();
        }
    }

    public List<net.miz_hi.smileessence.data.list.List> findAll()
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<net.miz_hi.smileessence.data.list.List, Integer> dao = helper.getDao(net.miz_hi.smileessence.data.list.List.class);
            return dao.queryForAll();
        }
        catch (Exception e)
        {
            Log.e(ListModel.class.getSimpleName(), "error on findAll");
            return Collections.emptyList();
        }
        finally
        {
            helper.close();
        }
    }
}
