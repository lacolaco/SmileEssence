package net.miz_hi.smileessence.core;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.page.Page;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.util.LogHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper
{
	public static final String dbName = Client.getApplication().getExternalFilesDir(null) + "/database.db";
	public static final int dbVersion = 2;

	public DBHelper(Context context)
	{
		super(context, dbName, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1)
	{
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3)
	{
	}
}
