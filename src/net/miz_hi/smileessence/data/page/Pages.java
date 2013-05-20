package net.miz_hi.smileessence.data.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.miz_hi.smileessence.view.IRemainable;

import android.support.v4.app.Fragment;

public class Pages
{
		
	private static List<Page> pages = new ArrayList<Page>();
	private static boolean transaction;
	
	static
	{
		read();
		transaction = false;
	}
	
	public static void startTransaction()
	{
		transaction = true;
	}
	
	public static void stopTransaction()
	{
		transaction = false;
		update();
	}
	
	private static void read()
	{
		pages.clear();
		if(PageDB.instance().findAll() != null)
		{
			for(Page Page : PageDB.instance().findAll())
			{
				pages.add(Page);
			}
		}
	}
	
	public static void update()
	{
		PageDB.instance().deleteAll();
		for(Page Page : pages)
		{
			PageDB.instance().save(Page);
		}
		read();
	}
	
	public static void clear()
	{
		PageDB.instance().deleteAll();
		read();
	}

	public static void addPage(Fragment fragment)
	{
		addPage(new Page(fragment.getClass().getSimpleName(), ((IRemainable)fragment).save()));
	}
	
	public static void addPage(Page Page)
	{
		pages.add(Page);
		if(!transaction)
		{
			update();
		}
	}

	public static void setPage(Fragment fragment, int index)
	{
		setPage(new Page(fragment.getClass().getSimpleName(), ((IRemainable)fragment).save()), index);
	}
	
	public static void setPage(Page Page, int index)
	{
		pages.set(index, Page);
		if(!transaction)
		{
			update();
		}
	}

	public static void deletePage(Page Page)
	{
		pages.remove(Page);
		if(!transaction)
		{
			update();
		}
	}
	
	public static List<Page> getPages()
	{
		return Collections.unmodifiableList(pages);
	}
}
