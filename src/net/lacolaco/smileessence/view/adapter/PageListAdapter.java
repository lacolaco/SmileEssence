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
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import net.lacolaco.smileessence.R;

import java.util.ArrayList;

public class PageListAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener, ActionBar.OnNavigationListener
{

    private final Context context;
    private final ActionBar actionBar;
    private final ViewPager viewPager;
    private final ArrayList<PageInfo> pages = new ArrayList<>();

    public PageListAdapter(Activity activity, ViewPager viewPager)
    {
        super(activity.getFragmentManager());
        this.context = activity;
        this.actionBar = activity.getActionBar();
        this.viewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.setOnPageChangeListener(this);
    }

    public PageInfo getPage(int position)
    {
        return pages.get(position);
    }

    @Override
    public void notifyDataSetChanged()
    {
        refreshListNavigation();
        super.notifyDataSetChanged();
    }

    /**
     * Add new tab and new page
     *
     * @param name Page name
     * @param clss Fragment class
     * @param args Bundle for Fragment instantiate
     * @return True if adding is complete successfully
     */
    public boolean addPage(String name, Class<? extends Fragment> clss, Bundle args)
    {
        if(addPageWithoutNotify(name, clss, args))
        {
            notifyDataSetChanged();
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
    public boolean addPageWithoutNotify(String name, Class<? extends Fragment> clss, Bundle args)
    {
        PageInfo info = new PageInfo(name, clss, args);
        return pages.add(info);
    }

    /**
     * Remove page by position.
     *
     * @param position
     * @return True if removing is complete successfully
     */
    public boolean removePage(int position)
    {
        if(removePageWithoutNotify(position))
        {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    /**
     * Remove page by position without notify.
     *
     * @param position
     * @return True if removing is complete successfully
     */
    private boolean removePageWithoutNotify(int position)
    {
        return pages.remove(position) != null;
    }

    private void refreshListNavigation()
    {
        ArrayList<String> itemList = new ArrayList<>();
        for(PageInfo page : pages)
        {
            itemList.add(page.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.navigation_list_item, R.id.navigation_list_item_text, itemList);
        actionBar.setListNavigationCallbacks(adapter, this);
    }

    @Override
    public Fragment getItem(int position)
    {
        PageInfo info = pages.get(position);
        return Fragment.instantiate(context, info.fragmentClass.getName(), info.args);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageSelected(int position)
    {
        //Synchronize pager and navigation.
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    @Override
    public int getCount()
    {
        return pages.size();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        viewPager.setCurrentItem(itemPosition);
        return true;
    }

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
