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

package net.lacolaco.smileessence.viewmodel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class PageListAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener, ActionBar.OnNavigationListener
{

    private final Context context;
    private final ActionBar actionBar;
    private final ViewPager viewPager;
    private final ArrayList<PageInfo> pages = new ArrayList<>();

    static final class PageInfo
    {

        private final String name;
        private final Class<?> fragmentClass;
        private final Bundle args;

        PageInfo(String name, Class<?> clss, Bundle args)
        {
            this.name = name;
            this.fragmentClass = clss;
            this.args = args;
        }
    }

    public PageListAdapter(Activity activity, ViewPager viewPager)
    {
        super(activity.getFragmentManager());
        this.context = activity;
        this.actionBar = activity.getActionBar();
        this.viewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * Add new tab and new page
     *
     * @param name Page name
     * @param clss Fragment class
     * @param args Bundle for Fragment instantiate
     */
    public void addTab(String name, Class<?> clss, Bundle args)
    {
        PageInfo info = new PageInfo(name, clss, args);
        pages.add(info);
        refreshListNavigation();
        notifyDataSetChanged();
    }

    /**
     * Add new tab and new page without notify.
     * You must call notifyDataSetChanged after adding tab.
     *
     * @param name Page name
     * @param clss Fragment class
     * @param args Bundle for Fragment instantiate
     */
    public void addTabWithoutNotify(String name, Class<?> clss, Bundle args)
    {
        PageInfo info = new PageInfo(name, clss, args);
        pages.add(info);
        refreshListNavigation();
    }

    private void refreshListNavigation()
    {
        ArrayList<String> itemList = new ArrayList<>();
        for(PageInfo page : pages)
        {
            itemList.add(page.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, itemList);
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
}
