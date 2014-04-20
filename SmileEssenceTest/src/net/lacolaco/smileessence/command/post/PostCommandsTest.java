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

package net.lacolaco.smileessence.command.post;

import android.test.ActivityInstrumentationTestCase2;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.util.Morse;
import net.lacolaco.smileessence.view.adapter.PostState;

public class PostCommandsTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    public PostCommandsTest()
    {
        super(MainActivity.class);
    }

    public void testReplace() throws Exception
    {
        String s = "テスト（テスト）";
        PostCommandMorse morse = new PostCommandMorse(getActivity());
        PostState.getState().removeListener();
        PostState.getState().beginTransaction().setText(s).commit();
        assertEquals(Morse.jaToMorse(s), morse.build(s));
        morse.execute();
        assertEquals(PostState.getState().getText(), morse.build(s));
        PostState.newState().beginTransaction().setText(s).setSelection(0, 3).commit();
        morse.execute();
        assertEquals(Morse.jaToMorse("テスト") + "（テスト）", PostState.getState().getText());
    }

    public void testInsert() throws Exception
    {
        String s = "テスト";
        String inserted = "AAA";
        PostCommandInsert insert = new PostCommandInsert(getActivity(), inserted);
        PostState.getState().removeListener();
        PostState.getState().beginTransaction().setText(s).commit();
        assertEquals("テストAAA", insert.build(s));
        PostState.newState().beginTransaction().setText(s).setCursor(0).commit();
        insert.execute();
        assertEquals("AAAテスト", PostState.getState().getText());
    }
}
