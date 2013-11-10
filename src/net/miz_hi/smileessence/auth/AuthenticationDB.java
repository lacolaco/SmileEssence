package net.miz_hi.smileessence.auth;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.DBHelper;

import java.util.List;

public class AuthenticationDB
{

    private Context context;
    private static AuthenticationDB instance = new AuthenticationDB(Client.getApplication());

    private AuthenticationDB(Context context)
    {
        this.context = context;
    }

    public static AuthenticationDB instance()
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
            Log.e(AuthenticationDB.class.getSimpleName(), "error on save");
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
            Log.e(AuthenticationDB.class.getSimpleName(), "error on delete");
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
            Log.e(AuthenticationDB.class.getSimpleName(), "error on delete");
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
            Log.e(AuthenticationDB.class.getSimpleName(), "error on findAll");
            return null;
        }
        finally
        {
            helper.close();
        }
    }
}
