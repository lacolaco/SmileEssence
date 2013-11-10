package net.miz_hi.smileessence.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.list.List;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.util.LogHelper;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper
{

    public static final String dbName = Client.getApplication().getExternalFilesDir(null) + "/database.db";
    public static final int dbVersion = 3;

    public DBHelper(Context context)
    {
        super(context, dbName, null, dbVersion);
    }

    public void initialize()
    {
        try
        {
            TableUtils.createTableIfNotExists(getConnectionSource(), Account.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), Template.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), ExtraWord.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), List.class);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            LogHelper.d("error on created");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1)
    {
        try
        {
            TableUtils.createTableIfNotExists(getConnectionSource(), Account.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), Template.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), ExtraWord.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), List.class);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            LogHelper.d("error on created");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3)
    {
    }
}
