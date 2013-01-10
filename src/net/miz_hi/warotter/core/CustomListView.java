package net.miz_hi.warotter.core;

import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class CustomListView extends ListView implements IBindableView<CustomListView>
{
	
	private boolean isTop = false;
	private View footerView;
	
	public CustomListView(Context context)
	{
		super(context);
	}
	
	public CustomListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	
	@Override
	public void onFinishInflate()
	{
		super.onFinishInflate();
		this.setOnScrollListener(new OnScrollListener()
		{
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1)
			{
				if (arg0.getChildCount() == 0)
				{
					isScrollTopViewAttribute.set(true);
				}
				else if (arg0.getFirstVisiblePosition() == 0 && arg0.getChildAt(0).getTop() == 0)
				{
					isScrollTopViewAttribute.set(true);
				}
				else
				{
					isScrollTopViewAttribute.set(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3)
			{
				if (arg0.getChildCount() == 0)
				{
					isScrollTopViewAttribute.set(true);
				}
				else if (arg0.getFirstVisiblePosition() == 0 && arg0.getChildAt(0) != null && arg0.getChildAt(0).getTop() == 0)
				{
					isScrollTopViewAttribute.set(true);
				}
				else
				{
					isScrollTopViewAttribute.set(false);
				}				
			}
		});
		
	}

	@Override
	public ViewAttribute<CustomListView, ?> createViewAttribute(String arg0)
	{		
		if(arg0.equals("isScrollTop"))
		{
			return isScrollTopViewAttribute;
		}
		else if(arg0.equals("footerView"))
		{
			return footerViewAttribute;
		}
		return null;
	}
	
	public ViewAttribute<CustomListView, Boolean> isScrollTopViewAttribute = 
			new ViewAttribute<CustomListView, Boolean>(Boolean.class, CustomListView.this, "isScrollTop")
	{
		
		@Override
		public Boolean get()
		{
			return Boolean.valueOf(mHost.isTop);
		}
		
		@Override
		protected void doSetAttributeValue(Object arg0)
		{
			mHost.isTop = (Boolean)arg0;
		}
	};

	public ViewAttribute<CustomListView, View> footerViewAttribute = 
			new ViewAttribute<CustomListView, View>(View.class, CustomListView.this, "footerView")
	{

		@Override
		public View get()
		{
			return mHost.footerView;
		}

		@Override
		protected void doSetAttributeValue(Object arg0)
		{
			mHost.addFooterView((View) arg0);
		}
	};

}
