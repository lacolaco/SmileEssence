package net.miz_hi.smileessence.data.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.miz_hi.smileessence.view.IRemainable;

import android.support.v4.app.Fragment;

public class Pages
{
		
	private static List<Page> Pages = new ArrayList<Page>();
	
	static
	{
		read();
	}
	
	private static void read()
	{
		Pages.clear();
		if(PageDB.instance().findAll() != null)
		{
			for(Page Page : PageDB.instance().findAll())
			{
				Pages.add(Page);
			}
		}
	}
	
	public static void update()
	{
		PageDB.instance().deleteAll();
		for(Page Page : Pages)
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
		Pages.add(new Page(fragment.getClass().getSimpleName(), ((IRemainable)fragment).save()));
		update();
	}
	
	public static void addPage(Page Page)
	{
		Pages.add(Page);
		update();
	}

	public static void setPage(Fragment fragment, int index)
	{
		Pages.set(index, new Page(fragment.getClass().getSimpleName(), ((IRemainable)fragment).save()));
		update();
	}
	
	public static void setPage(Page Page, int index)
	{
		Pages.set(index, Page);
		update();
	}
	
	public static void deletePage(Page Page)
	{
		Pages.remove(Page);
		update();
	}
	
	public static List<Page> getPages()
	{
		return Collections.unmodifiableList(Pages);
	}
}
