package net.miz_hi.smileessence.menu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpandMenuListAdapter extends BaseExpandableListAdapter
{

    private List<MenuElement> elements;
    private Activity activity;
    private LayoutInflater inflater;

    public ExpandMenuListAdapter(Activity activity, Collection<MenuElement> elements)
    {
        this.activity = activity;
        this.elements = new ArrayList<MenuElement>(elements);
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public Object getChild(int arg0, int arg1)
    {
        return elements.get(arg0).getChildren().get(arg1);
    }

    @Override
    public long getChildId(int arg0, int arg1)
    {
        return arg1;
    }

    @Override
    public View getChildView(int arg0, int arg1, boolean arg2, View view, ViewGroup arg4)
    {

        view = inflater.inflate(R.layout.menuitem_white, null);

        final ICommand item = ((MenuElement) getChild(arg0, arg1)).getCommand();

        TextView textView = (TextView) view.findViewById(R.id.textView_menuItem);
        textView.setText(item.getName());
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (item instanceof IConfirmable && Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
                {
                    ConfirmDialog.show(activity, "実行しますか？", new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            item.run();
                        }
                    });
                }
                else
                {
                    item.run();
                }
            }
        });
        return view;
    }

    @Override
    public int getChildrenCount(int arg0)
    {
        return elements.get(arg0).getChildren().size();
    }

    @Override
    public Object getGroup(int arg0)
    {
        return elements.get(arg0);
    }

    @Override
    public int getGroupCount()
    {
        return elements.size();
    }

    @Override
    public long getGroupId(int arg0)
    {
        return arg0;
    }

    @Override
    public View getGroupView(int arg0, boolean isExpanded, View view, ViewGroup parent)
    {
        MenuElement element = elements.get(arg0);
        if (element.isParent())
        {
            view = inflater.inflate(R.layout.menuparent_white, null);
            TextView textView = (TextView) view.findViewById(R.id.textView_menuItem);
            textView.setText(element.getName());
            ImageView indicator = (ImageView) view.findViewById(R.id.menuparent_indicator);
            indicator.setImageResource(isExpanded ? R.drawable.expand_open : R.drawable.expand_close);
        }
        else
        {
            view = inflater.inflate(R.layout.menuitem_white, null);

            final ICommand item = element.getCommand();

            TextView textView = (TextView) view.findViewById(R.id.textView_menuItem);
            textView.setText(item.getName());

            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (item instanceof IConfirmable && Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
                    {
                        ConfirmDialog.show(activity, "実行しますか？", new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                item.run();
                            }
                        });
                    }
                    else
                    {
                        item.run();
                    }
                }
            });
        }

        return view;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1)
    {
        return true;
    }


}
