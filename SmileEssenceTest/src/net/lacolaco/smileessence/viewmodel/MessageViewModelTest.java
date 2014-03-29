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

import android.test.InstrumentationTestCase;
import net.lacolaco.smileessence.util.TwitterMock;
import twitter4j.DirectMessage;

public class MessageViewModelTest extends InstrumentationTestCase
{

    TwitterMock mock;

    @Override
    public void setUp() throws Exception
    {
        mock = new TwitterMock(getInstrumentation().getContext());
    }

    public void testNewInstance() throws Exception
    {
        DirectMessage dm = mock.getDirectMessageMock();
        MessageViewModel viewModel = new MessageViewModel(dm);
        assertEquals(dm.getId(), viewModel.getID());
        assertEquals(dm.getSenderId(), viewModel.getSenderID());
        assertEquals(dm.getSenderScreenName(), viewModel.getSenderScreenName());
        assertEquals(dm.getSender().getProfileImageURL(), viewModel.getSenderIconURL());
        assertEquals(dm.getText(), viewModel.getText());
        assertEquals(dm.getCreatedAt(), viewModel.getCreatedAt());
    }
}