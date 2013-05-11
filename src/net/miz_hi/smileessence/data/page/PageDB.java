package net.miz_hi.smileessence.data.page;

import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.DataBaseHelper;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class PageDB
{
	private Context context;
	private static PageDB instance = new PageDB(Client.getApplication());

	private PageDB(Context context)
	{
		this.context = context;
	}

	public static PageDB instance()
	{
		return instance;
	}

	public void save(Page Page)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Page, Integer> dao = helper.getDao(Page.class);
			dao.createOrUpdate(Page);
		}
		catch (Exception e)
		{
			Log.e(PageDB.class.getSimpleName(), "error on save");
		}
		finally
		{
			helper.close();
		}
	}

	public void delete(Page Page)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Page, Integer> dao = helper.getDao(Page.class);
			dao.delete(Page);
		}
		catch (Exception e)
		{
			Log.e(PageDB.class.getSimpleName(), "error on delete");
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
			for (Page Page : findAll())
			{
				Dao<Page, Integer> dao = helper.getDao(Page.class);
				dao.delete(Page);
			}
		}
		catch (Exception e)
		{
			Log.e(PageDB.class.getSimpleName(), "error on delete");
		}
		finally
		{
			helper.close();
		}
	}

	public List<Page> findAll()
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Page, Integer> dao = helper.getDao(Page.class);
			return dao.queryForAll();
		}
		catch (Exception e)
		{
			Log.e(PageDB.class.getSimpleName(), "error on findAll");
			return null;
		}
		finally
		{
			helper.close();
		}
	}
}
