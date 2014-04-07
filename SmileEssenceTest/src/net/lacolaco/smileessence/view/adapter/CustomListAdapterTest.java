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
import net.lacolaco.smileessence.util.TwitterMock;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;

public class CustomListAdapterTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    TwitterMock mock;
    CustomListAdapter<StatusViewModel> adapter;

    public CustomListAdapterTest()
    {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception
    {
        mock = new TwitterMock(getInstrumentation().getContext());
        adapter = new CustomListAdapter<>(getActivity(), StatusViewModel.class);
    }

    public void testAddItem() throws Exception
    {
        adapter.addToBottom(new StatusViewModel(mock.getStatusMock(), ));
        assertEquals(1, adapter.getCount());
    }

    public void testAddItems() throws Exception
    {
        StatusViewModel viewModel1 = new StatusViewModel(mock.getStatusMock(), );
        StatusViewModel viewModel2 = new StatusViewModel(mock.getStatusMock(), );
        adapter.addToBottom(viewModel1, viewModel2);
        assertEquals(2, adapter.getCount());
    }

    public void testRemoveItem() throws Exception
    {
        StatusViewModel viewModel = new StatusViewModel(mock.getStatusMock(), );
        adapter.addToBottom(viewModel, viewModel);
        assertTrue(adapter.removeItem(0) == viewModel);
    }

    public void testGetView() throws Exception
    {
        final StatusViewModel viewModel = new StatusViewModel(mock.getStatusMock(), );
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                adapter.addToBottom(viewModel);
                assertNotNull(adapter.getView(0, null, null));
            }
        });
    }

    public void testAddPosition() throws Exception
    {
        StatusViewModel status1 = new StatusViewModel(mock.getStatusMock(), );
        StatusViewModel status2 = new StatusViewModel(mock.getStatusMock(), );
        adapter.addToBottom(status1);
        adapter.addToTop(status2);
        assertEquals(status2, adapter.getItem(0));
    }

    public void testGetActivity() throws Exception
    {
        assertNotNull(adapter.getActivity());
    }
}
