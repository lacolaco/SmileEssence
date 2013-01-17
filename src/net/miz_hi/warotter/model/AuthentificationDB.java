package net.miz_hi.warotter.model;

import java.util.List;

import net.miz_hi.warotter.core.DataBaseHelper;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class AuthentificationDB
{
	private Context context;

	public AuthentificationDB(Context context)
	{
		this.context = context;
	}

	public static AuthentificationDB instance()
	{
		return new AuthentificationDB(Warotter.getApplication());
	}

	public void save(Account account)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Account, Integer> dao = helper.getDao(Account.class);
			dao.createOrUpdate(account);
		}
		catch (Exception e)
		{
			Log.e(AuthentificationDB.class.getSimpleName(), "error on save");
		}
		finally
		{
			helper.close();
		}
	}

	public void delete(Account account)
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Account, Integer> dao = helper.getDao(Account.class);
			dao.delete(account);
		}
		catch (Exception e)
		{
			Log.e(AuthentificationDB.class.getSimpleName(), "error on delete");
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
			for(Account account : findAll())
			{
				Dao<Account, Integer> dao = helper.getDao(Account.class);
				dao.delete(account);			
			}
		}
		catch (Exception e)
		{
			Log.e(AuthentificationDB.class.getSimpleName(), "error on delete");
		}
		finally
		{
			helper.close();
		}
	}

	public List<Account> findAll()
	{
		DataBaseHelper helper = new DataBaseHelper(context);
		try
		{
			Dao<Account, Integer> dao = helper.getDao(Account.class);
			return dao.queryForAll();
		}
		catch (Exception e)
		{
			Log.e(AuthentificationDB.class.getSimpleName(), "error on findAll");
			return null;
		}
		finally
		{
			helper.close();
		}
	}
}
