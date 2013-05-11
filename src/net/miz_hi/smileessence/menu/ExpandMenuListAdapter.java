package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.dialog.ConfirmDialog;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandMenuListAdapter extends BaseExpandableListAdapter
{
	
	private List<String> groups;
	private List<List<ICommand>> children;
	private Activity activity;
	private LayoutInflater inflater;
	
	public ExpandMenuListAdapter(Activity activity, Collection<String> groups, Collection<List<ICommand>> children)
	{
		this.activity = activity;
		this.groups = new ArrayList<String>(groups);
		this.children = new ArrayList<List<ICommand>>(children);
		this.inflater = activity.getLayoutInflater();
	}

	@Override
	public Object getChild(int arg0, int arg1)
	{
		return children.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1)
	{
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View view, ViewGroup arg4)
	{
		if(view == null)
		{
			view = inflater.inflate(R.layout.menuitem_white, null);
		}
		
		final ICommand item = (ICommand) getChild(arg0, arg1);
		
		TextView textView = (TextView) view.findViewById(R.id.textView_menuItem);
		textView.setText(item.getName());
		view.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(item instanceof IConfirmable && Client.<Boolean>getPreferenceValue(EnumPreferenceKey.CONFIRM_DIALOG))
				{
					ConfirmDialog.show(activity, "é¿çsÇµÇ‹Ç∑Ç©ÅH", new Runnable()
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
		return children.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0)
	{
		return groups.get(arg0);
	}

	@Override
	public int getGroupCount()
	{
		return children.size();
	}

	@Override
	public long getGroupId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3)
	{
		if(arg2 == null)
		{
			arg2 = inflater.inflate(R.layout.menuparent_white, null);
		}
		TextView textView = (TextView) arg2.findViewById(R.id.textView_menuItem);
		textView.setText(groups.get(arg0).toString());
		return arg2;
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
