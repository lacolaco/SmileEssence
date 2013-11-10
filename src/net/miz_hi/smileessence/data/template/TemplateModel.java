package net.miz_hi.smileessence.data.template;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.DBHelper;

import java.util.List;

public class TemplateModel
{

    private Context context;
    private static TemplateModel instance = new TemplateModel(Client.getApplication());

    private TemplateModel(Context context)
    {
        this.context = context;
    }

    public static TemplateModel instance()
    {
        return instance;
    }

    public void save(Template template)
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<Template, Integer> dao = helper.getDao(Template.class);
            dao.createOrUpdate(template);
        }
        catch (Exception e)
        {
            Log.e(TemplateModel.class.getSimpleName(), "error on save");
        }
        finally
        {
            helper.close();
        }
    }

    public void delete(Template template)
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<Template, Integer> dao = helper.getDao(Template.class);
            dao.delete(template);
        }
        catch (Exception e)
        {
            Log.e(TemplateModel.class.getSimpleName(), "error on delete");
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
            for (Template template : findAll())
            {
                Dao<Template, Integer> dao = helper.getDao(Template.class);
                dao.delete(template);
            }
        }
        catch (Exception e)
        {
            Log.e(TemplateModel.class.getSimpleName(), "error on delete");
        }
        finally
        {
            helper.close();
        }
    }

    public List<Template> findAll()
    {
        DBHelper helper = new DBHelper(context);
        try
        {
            Dao<Template, Integer> dao = helper.getDao(Template.class);
            return dao.queryForAll();
        }
        catch (Exception e)
        {
            Log.e(TemplateModel.class.getSimpleName(), "error on findAll");
            return null;
        }
        finally
        {
            helper.close();
        }
    }
}
