package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.menu.ExpandMenuListAdapter;
import net.miz_hi.smileessence.menu.MenuElement;
import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;
import net.miz_hi.smileessence.preference.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpandMenuDialog extends MenuDialog
{

    protected View titleView;
    protected String title;

    public ExpandMenuDialog(Activity activity)
    {
        super(activity);
    }

    public void setTitle(View view)
    {
        titleView = view;
    }

    public View getTitleView()
    {
        return titleView;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public abstract List<MenuElement> getElements();

    public Dialog create()
    {
        dispose();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (titleView == null)
        {
            builder.setTitle(title);
        }
        else
        {
            builder.setCustomTitle(titleView);
        }

        List<MenuElement> list3 = getElements();
        List stub = new ArrayList();
        for (MenuElement menuElement : list3)
        {
            if (menuElement.isParent())
            {
                List<MenuElement> children = menuElement.getChildren();
                List stub1 = new ArrayList();
                for (MenuElement menuElement2 : children)
                {
                    boolean isEnabled = true;
                    ICommand command = menuElement2.getCommand();
                    if (command != null)
                    {
                        if (command instanceof IHideable)
                        {
                            PreferenceHelper pref = Client.getPreferenceHelper();
                            isEnabled = pref.getPreferenceValue(command.getClass().getSimpleName(), EnumValueType.BOOLEAN, false);
                        }

                        if (!command.getDefaultVisibility() || !isEnabled)
                        {
                            stub1.add(menuElement2);
                        }
                    }
                }
                children.removeAll(stub1);
            }
            else
            {

                boolean isEnabled = true;
                ICommand command = menuElement.getCommand();
                if (command != null)
                {
                    if (command instanceof IHideable)
                    {
                        PreferenceHelper pref = Client.getPreferenceHelper();
                        isEnabled = pref.getPreferenceValue(command.getClass().getSimpleName(), EnumValueType.BOOLEAN, false);
                    }

                    if (!command.getDefaultVisibility() || !isEnabled)
                    {
                        stub.add(menuElement);
                    }
                }
            }
        }
        list3.removeAll(stub);

        ExpandableListView listview = new ExpandableListView(activity);
        listview.setGroupIndicator(Client.getResource().getDrawable(android.R.color.transparent));
        listview.setOnGroupClickListener(new OnGroupClickListener()
        {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                parent.smoothScrollToPosition(groupPosition);
                return false;
            }
        });

        ExpandMenuListAdapter adapter = new ExpandMenuListAdapter(activity, list3);
        listview.setAdapter(adapter);
        builder.setView(listview);
        dialog = builder.create();
        LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        lp.width = (int) (metrics.widthPixels * 0.9);
        lp.gravity = Gravity.CENTER;
        lp.height = (int) (metrics.heightPixels * 0.8);

        return dialog;
    }
}
