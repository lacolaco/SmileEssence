package net.miz_hi.smileessence;

import android.app.Application;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthenticationDB;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.data.DBHelper;
import net.miz_hi.smileessence.permission.IPermission;
import net.miz_hi.smileessence.permission.PermissonChecker;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.preference.PreferenceHelper;

import java.io.File;

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

    public static boolean hasAuthorizedAccount()
    {
        return AuthenticationDB.instance().findAll() != null && !AuthenticationDB.instance().findAll().isEmpty();
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
        return new File(app.getExternalCacheDir(), fileName);
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
        new DBHelper(app).initialize();
        MyExecutor.init();
    }

    public static final String HOMEPAGE_URL = "http://smileessence.miz-hi.net/";
    public static final String CALLBACK_OAUTH = "oauth://smileessence";

}
