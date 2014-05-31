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

package net.lacolaco.smileessence.view.listener;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.util.UIHandler;

public class ListItemClickListener implements View.OnClickListener
{

    // ------------------------------ FIELDS ------------------------------

    private final Activity activity;
    private final Runnable callback;

    // --------------------------- CONSTRUCTORS ---------------------------

    public ListItemClickListener(Activity activity, Runnable callback)
    {
        this.activity = activity;
        this.callback = callback;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnClickListener ---------------------

    @Override
    public void onClick(final View v)
    {
        final int currentBgColor = ((ColorDrawable) v.getBackground()).getColor();
        v.setBackgroundColor(activity.getResources().getColor(R.color.metro_blue));
        v.invalidate();
        new UIHandler()
        {

            @Override
            public void run()
            {
                v.setBackgroundColor(currentBgColor);
                callback.run();
            }
        }.post();
    }
}
