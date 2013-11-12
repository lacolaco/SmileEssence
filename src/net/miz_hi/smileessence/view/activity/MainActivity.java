package net.miz_hi.smileessence.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.OnCenterItemClickListener;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.IntentRouter;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.listener.PageChangeListener;
import net.miz_hi.smileessence.menu.MainMenu;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.system.MainActivitySystem;
import net.miz_hi.smileessence.system.PageController;

public class MainActivity extends FragmentActivity
{

    private static MainActivity instance;
    public MainActivitySystem system;
    private ViewPager pager;

    public static MainActivity getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_layout);
        instance = this;
        system = new MainActivitySystem();
        StatusListManager.initStatusLists(instance);
        initializeViews();
        IntentRouter.onNewIntent(getIntent());
    }

    private void initializeViews()
    {
        pager = (ViewPager) findViewById(R.id.viewpager);
        PageController.init(instance, pager);
        pager.setAdapter(PageController.getInstance().getAdapter());
        pager.destroyDrawingCache();

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setTextSize(21);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new PageChangeListener());
        indicator.setOnCenterItemClickListener(new OnCenterItemClickListener()
        {

            @Override
            public void onCenterItemClick(int position)
            {
                new MainMenu(instance).create().show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        if (system.checkAccount(instance))
        {
            system.startTwitter(instance);
            system.loadListPage(instance);
        }
        PageController.getInstance().move(1);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Crouton.cancelAllCroutons();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Crouton.cancelAllCroutons();
        system.onDestroyed();
        instance = null;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if (reqCode == EnumRequestCode.AUTHORIZE.ordinal())
        {
            system.authorize(instance, data.getData());
        }
        else if (reqCode == EnumRequestCode.PICTURE.ordinal() || reqCode == EnumRequestCode.CAMERA.ordinal())
        {
            system.receivePicture(instance, data, reqCode);
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        IntentRouter.onNewIntent(intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() != KeyEvent.ACTION_DOWN)
        {
            return super.dispatchKeyEvent(event);
        }
        switch (event.getKeyCode())
        {
            case KeyEvent.KEYCODE_BACK:
            {
                finish();
                return false;
            }
            case KeyEvent.KEYCODE_MENU:
            {
                openMenu();
                return true;
            }
            default:
            {
                return super.dispatchKeyEvent(event);
            }
        }
    }

    @Override
    public void finish()
    {
        if (pager.getCurrentItem() != 1)
        {
            pager.setCurrentItem(1, true);
        }
        else
        {
            finish(!Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG));
        }
    }

    private void forceFinish()
    {
        super.finish();
    }

    public void finish(boolean force)
    {
        if (!force)
        {
            ConfirmDialog.show(this, "終了しますか？", new Runnable()
            {

                @Override
                public void run()
                {
                    forceFinish();
                }
            });
        }
        else
        {
            forceFinish();
        }
    }

    public void openMenu()
    {
        new MainMenu(instance).create().show();
    }

    public ViewPager getViewPager()
    {
        return pager;
    }
}