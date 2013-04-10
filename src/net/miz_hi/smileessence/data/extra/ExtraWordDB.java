package net.miz_hi.smileessence.data.extra;

import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.DataBaseHelper;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class ExtraWordDB
{
	private Context context;
	private static ExtraWordDB instance = new ExtraWordDB(Client.getApplication());

	private ExtraWordDB(Context context)
	{
		this.context = context;
	}

	public static ExtraWordDB instance()
	{
		return instance;
	}

	public void save(ExtraWord data)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
			dao.createOrUpdate(data);
		}
		catch (Exception e)
		{
			Log.e(ExtraWordDB.class.getSimpleName(), "error on save");
		}
		finally
		{
			helper.close();
		}
	}

	public void delete(ExtraWord data)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
			dao.delete(data);
		}
		catch (Exception e)
		{
			Log.e(ExtraWordDB.class.getSimpleName(), "error on delete");
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
			for (ExtraWord data : findAll())
			{
				Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
				dao.delete(data);
			}
		}
		catch (Exception e)
		{
			Log.e(ExtraWordDB.class.getSimpleName(), "error on delete");
		}
		finally
		{
			helper.close();
		}
	}

	public List<ExtraWord> findAll()
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<ExtraWord, Integer> dao = helper.getDao(ExtraWord.class);
			return dao.queryForAll();
		}
		catch (Exception e)
		{
			Log.e(ExtraWordDB.class.getSimpleName(), "error on findAll");
			return null;
		}
		finally
		{
			helper.close();
		}
	}
}
