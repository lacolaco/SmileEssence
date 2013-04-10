package net.miz_hi.smileessence.data.template;

import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.DataBaseHelper;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class TemplateDB
{
	private Context context;
	private static TemplateDB instance = new TemplateDB(Client.getApplication());

	private TemplateDB(Context context)
	{
		this.context = context;
	}

	public static TemplateDB instance()
	{
		return instance;
	}

	public void save(Template template)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Template, Integer> dao = helper.getDao(Template.class);
			dao.createOrUpdate(template);
		}
		catch (Exception e)
		{
			Log.e(TemplateDB.class.getSimpleName(), "error on save");
		}
		finally
		{
			helper.close();
		}
	}

	public void delete(Template template)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Template, Integer> dao = helper.getDao(Template.class);
			dao.delete(template);
		}
		catch (Exception e)
		{
			Log.e(TemplateDB.class.getSimpleName(), "error on delete");
		}
		finally
		{
			helper.close();
		}
	}

	public void deleteAll()
	{
		DataBaseHelper helper = new DataBaseHelper(context);
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
			Log.e(TemplateDB.class.getSimpleName(), "error on delete");
		}
		finally
		{
			helper.close();
		}
	}

	public List<Template> findAll()
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Template, Integer> dao = helper.getDao(Template.class);
			return dao.queryForAll();
		}
		catch (Exception e)
		{
			Log.e(TemplateDB.class.getSimpleName(), "error on findAll");
			return null;
		}
		finally
		{
			helper.close();
		}
	}
}
