/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.view.adapter;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.logging.Logger;

import java.util.ArrayList;

public class PageListAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener,
        ActionBar.OnNavigationListener
{

    // ------------------------------ FIELDS ------------------------------

    private final MainActivity context;
    private final ActionBar actionBar;
    private final ViewPager viewPager;
    private final ArrayList<PageInfo> pages = new ArrayList<>();

    // --------------------------- CONSTRUCTORS ---------------------------

    public PageListAdapter(MainActivity activity, ViewPager viewPager)
    {
        super(activity.getFragmentManager());
        this.context = activity;
        this.actionBar = activity.getActionBar();
        this.viewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.setOnPageChangeListener(this);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public synchronized int getCount()
    {
        return pages.size();
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnNavigationListener ---------------------

    @Override
    public synchronized boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        viewPager.setCurrentItem(itemPosition, true);
        return true;
    }

    // --------------------- Interface OnPageChangeListener ---------------------

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public synchronized void onPageSelected(int position)
    {
        //Synchronize pager and navigation.
        Logger.debug(String.format("Page selected:%d", position));
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Add new tab and new page
     *
     * @param name Page name
     * @param clss Fragment class
     * @param args Bundle for Fragment instantiate
     * @return True if adding is complete successfully
     */
    public synchronized boolean addPage(String name, Class<? extends Fragment> clss, Bundle args)
    {
        if(addPageWithoutNotify(name, clss, args))
        {
            refreshListNavigation();
            return true;
        }
        return false;
    }

    /**
     * Add new tab and new page without notify.
     * You must call notifyDataSetChanged after adding tab.
     *
     * @param name Page name
     * @param clss Fragment class
     * @param args Bundle for Fragment instantiate
     * @return True if adding is complete successfully
     */
    public synchronized boolean addPageWithoutNotify(String name, Class<? extends Fragment> clss, Bundle args)
    {
        PageInfo info = new PageInfo(name, clss, args);
        return pages.add(info);
    }

    @Override
    public synchronized Fragment getItem(int position)
    {
        PageInfo info = pages.get(position);
        return Fragment.instantiate(context, info.fragmentClass.getName(), info.args);
    }

    public synchronized PageInfo getPage(int position)
    {
        return pages.get(position);
    }

    public synchronized void refreshListNavigation()
    {
        ArrayList<String> itemList = new ArrayList<>();
        for(PageInfo page : pages)
        {
            itemList.add(page.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.navigation_list_item, R.id.navigation_list_item_text, itemList);
        actionBar.setListNavigationCallbacks(adapter, this);
        notifyDataSetChanged();
    }

    public synchronized boolean removePage(int position)
    {
        if(removePageWithoutNotify(position))
        {
            refreshListNavigation();
            return true;
        }
        return false;
    }

    private synchronized boolean removePageWithoutNotify(int position)
    {
        return pages.remove(position) != null;
    }

    // -------------------------- INNER CLASSES --------------------------

    public static final class PageInfo
    {

        private final String name;
        private final Class<? extends Fragment> fragmentClass;
        private final Bundle args;

        PageInfo(String name, Class<? extends Fragment> clss, Bundle args)
        {
            this.name = name;
            this.fragmentClass = clss;
            this.args = args;
        }

        public String getName()
        {
            return name;
        }

        public Class<? extends Fragment> getFragmentClass()
        {
            return fragmentClass;
        }

        public Bundle getArgs()
        {
            return args;
        }
    }
}
