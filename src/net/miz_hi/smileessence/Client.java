package net.miz_hi.smileessence;

import java.io.File;

import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthentificationDB;
import net.miz_hi.smileessence.core.DBHelper;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.page.Page;
import net.miz_hi.smileessence.data.template.Template;
import net.miz_hi.smileessence.permission.IPermission;
import net.miz_hi.smileessence.permission.PermissonChecker;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.preference.PreferenceHelper;
import net.miz_hi.smileessence.util.LogHelper;
import android.app.Application;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.j256.ormlite.table.TableUtils;

public class Client
{
	private static Application app;
	private static Account mainAccount;
	private static PreferenceHelper prefHelper;
	private static IPermission permission;
	private static int textSize;

	private Client()
	{
	}

	public static PreferenceHelper getPreferenceHelper()
	{
		return prefHelper;
	}

	public static void putPreferenceValue(EnumPreferenceKey key, Object value)
	{
		prefHelper.putPreferenceValue(key, value);
	}

	public static <T> T getPreferenceValue(EnumPreferenceKey key)
	{
		return prefHelper.getPreferenceValue(key);
	}

	public static boolean hasAuthedAccount()
	{
		if (AuthentificationDB.instance().findAll() == null)
		{
			return false;
		}
		return !AuthentificationDB.instance().findAll().isEmpty();
	}

	public static Application getApplication()
	{
		return app;
	}

	public static Account getMainAccount()
	{
		return mainAccount;
	}

	public static void setMainAccount(Account account)
	{
		if (account != null)
		{
			putPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID, account.getUserId());
		}
		else
		{
			putPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID, -1L);
		}
		mainAccount = account;

		setPermission(PermissonChecker.checkPermission(mainAccount));
	}

	public static IPermission getPermission()
	{
		return permission;
	}

	public static void setPermission(IPermission permission)
	{
		Client.permission = permission;
	}

	public static File getApplicationFile(String fileName)
	{
		File file = new File(app.getExternalCacheDir(), fileName);
		return file;
	}

	public static Resources getResource()
	{
		return app.getResources();
	}
	
	public static String getString(int id)
	{
		return app.getResources().getString(id);
	}

	public static int getColor(int resId)
	{
		return getResource().getColor(resId);
	}

	public static int getTextSize()
	{
		return textSize;
	}

	public static void loadPreferences()
	{
		int tSize = getPreferenceValue(EnumPreferenceKey.TEXT_SIZE);
		if (tSize < 0)
		{
			putPreferenceValue(EnumPreferenceKey.TEXT_SIZE, 10);
		}
		textSize = getPreferenceValue(EnumPreferenceKey.TEXT_SIZE);
	}

	public static void initialize(Application app)
	{
		Client.prefHelper = new PreferenceHelper(PreferenceManager.getDefaultSharedPreferences(app));
		Client.app = app;
		loadPreferences();

		DBHelper helper = new DBHelper(app);
		try
		{
			TableUtils.createTableIfNotExists(helper.getConnectionSource(), Account.class);
			TableUtils.createTableIfNotExists(helper.getConnectionSource(), Template.class);
			TableUtils.createTableIfNotExists(helper.getConnectionSource(), ExtraWord.class);
			TableUtils.createTableIfNotExists(helper.getConnectionSource(), Page.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LogHelper.d("error ontable created");
		}

		MyExecutor.init();
	}

	public static final String HOMEPAGE_URL = "http://smileessence.miz-hi.net/";
	public static final String PREF_OAUTH_NAME = "oauth_pref";
	public static final String CALLBACK_OAUTH = "oauth://smileessence";

}
