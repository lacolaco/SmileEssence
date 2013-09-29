package net.miz_hi.smileessence.auth;

import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.DBHelper;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

public class AuthentificationDB
{
	private Context context;
	private static AuthentificationDB instance = new AuthentificationDB(Client.getApplication());

	private AuthentificationDB(Context context)
	{
		this.context = context;
	}

	public static AuthentificationDB instance()
	{
		return instance;
	}

	public void save(Account account)
	{
		DBHelper helper = new DBHelper(context);
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
		DBHelper helper = new DBHelper(context);
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
		DBHelper helper = new DBHelper(context);
		try
		{
			for (Account account : findAll())
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
		DBHelper helper = new DBHelper(context);
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
