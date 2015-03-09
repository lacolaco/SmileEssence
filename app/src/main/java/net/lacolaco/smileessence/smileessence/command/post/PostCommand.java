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

import android.app.Activity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.view.adapter.PostState;

public abstract class PostCommand extends Command
{

    // --------------------------- CONSTRUCTORS ---------------------------

    public PostCommand(Activity activity)
    {
        super(-1, activity);
    }

    // -------------------------- OTHER METHODS --------------------------

    public abstract String build(String s);

    @Override
    public boolean execute()
    {
        PostState state = PostState.getState();
        String text = state.getText();
        int start = state.getSelectionStart();
        int end = state.getSelectionEnd();
        if(start == end && isReplaceCommand())
        {
            start = 0;
            end = text.length();
        }
        String substring = text.substring(start, end);
        String replacedText = build(substring);
        StringBuilder builder = new StringBuilder(text);
        String builtString = builder.replace(start, end, replacedText).toString();
        PostState.PostStateTransaction transaction = state.beginTransaction();
        int incremental = replacedText.length() - substring.length();
        if(incremental > 0)
        {
            transaction.setCursor(end + incremental);
        }
        transaction.setText(builtString).commit();
        return true;
    }

    public abstract boolean isReplaceCommand();
}
