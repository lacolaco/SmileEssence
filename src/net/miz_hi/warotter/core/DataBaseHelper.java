package net.miz_hi.warotter.core;

import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.model.Warotter;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper
{
	public static final String dbName = Warotter.getApplication().getExternalFilesDir(null) + "/database.db";
	public static final int dbVersion = 1;

	public DataBaseHelper(Context context)
	{
		super(context, dbName, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1)
	{
		try
		{
			TableUtils.createTable(arg1, Account.class);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3)
	{
	
	}
}
