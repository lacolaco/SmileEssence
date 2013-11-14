package net.miz_hi.smileessence.data.extra;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.DBHelper;

import java.util.List;

public class ExtractModel
{

    private Context context;
    private static ExtractModel instance = new ExtractModel(Client.getApplication());

    private ExtractModel(Context context)
    {
        this.context = context;
    }

    public static ExtractModel instance()
    {
        return instance;
    }

    public void save(ExtraWord data)
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
            dao.createOrUpdate(data);
        }
        catch (Exception e)
        {
            Log.e(ExtractModel.class.getSimpleName(), "error on save");
        }
        finally
        {
            helper.close();
        }
    }

    public void delete(ExtraWord data)
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
            dao.delete(data);
        }
        catch (Exception e)
        {
            Log.e(ExtractModel.class.getSimpleName(), "error on removeByLists");
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
            for (ExtraWord data : findAll())
            {
                Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
                dao.delete(data);
            }
        }
        catch (Exception e)
        {
            Log.e(ExtractModel.class.getSimpleName(), "error on removeByLists");
        }
        finally
        {
            helper.close();
        }
    }

    public List<ExtraWord> findAll()
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
            return dao.queryForAll();
        }
        catch (Exception e)
        {
            Log.e(ExtractModel.class.getSimpleName(), "error on findAll");
            return null;
        }
        finally
        {
            helper.close();
        }
    }
}
