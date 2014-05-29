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

import android.test.ActivityInstrumentationTestCase2;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.util.TwitterMock;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;

public class CustomListAdapterTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    TwitterMock mock;
    CustomListAdapter<StatusViewModel> adapter;
    Account account;

    public CustomListAdapterTest()
    {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception
    {
        mock = new TwitterMock(getInstrumentation().getContext());
        adapter = new CustomListAdapter<>(getActivity(), StatusViewModel.class);
        account = mock.getAccount();
    }

    public void testAddItem() throws Exception
    {
        adapter.addToBottom(new StatusViewModel(mock.getStatusMock(), account));
        adapter.notifyDataSetChanged();
        assertEquals(1, adapter.getCount());
    }

    public void testUpdate() throws Exception
    {
        adapter.addToBottom(new StatusViewModel(mock.getStatusMock(), account));
        assertEquals(0, adapter.getCount());
        adapter.notifyDataSetChanged();
        assertEquals(1, adapter.getCount());
    }

    public void testAddItems() throws Exception
    {
        StatusViewModel viewModel1 = new StatusViewModel(mock.getStatusMock(), account);
        StatusViewModel viewModel2 = new StatusViewModel(mock.getStatusMock(), account);
        adapter.addToBottom(viewModel1, viewModel2);
        adapter.notifyDataSetChanged();
        assertEquals(2, adapter.getCount());
    }

    public void testRemoveItem() throws Exception
    {
        StatusViewModel viewModel = new StatusViewModel(mock.getStatusMock(), account);
        adapter.addToBottom(viewModel, viewModel);
        adapter.notifyDataSetChanged();
        assertEquals(1, adapter.getCount());
        adapter.removeItem(0);
        adapter.notifyDataSetChanged();
        assertEquals(0, adapter.getCount());
    }

    public void testAddPosition() throws Exception
    {
        StatusViewModel status1 = new StatusViewModel(mock.getStatusMock(), account);
        StatusViewModel status2 = new StatusViewModel(mock.getStatusMock(), account);
        adapter.addToBottom(status1);
        adapter.addToTop(status2);
        adapter.notifyDataSetChanged();
        assertEquals(status2, adapter.getItem(0));
    }

    @Override
    protected void tearDown() throws Exception
    {
        getActivity().finish(false);
    }
}
