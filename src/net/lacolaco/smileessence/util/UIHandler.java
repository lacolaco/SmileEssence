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

package net.lacolaco.smileessence.util;

import android.os.Handler;
import android.os.Looper;

public abstract class UIHandler extends Handler implements Runnable
{

    // --------------------------- CONSTRUCTORS ---------------------------

    public UIHandler()
    {
        super(Looper.getMainLooper());
    }

    public UIHandler(Handler.Callback callback)
    {
        super(Looper.getMainLooper(), callback);
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface Runnable ---------------------

    @Override
    public abstract void run();

    // -------------------------- OTHER METHODS --------------------------

    public boolean post()
    {
        return post(this);
    }

    public boolean postAtFrontOfQueue()
    {
        return postAtFrontOfQueue(this);
    }

    public boolean postAtTime(long uptimeMillis)
    {
        return postAtTime(this, uptimeMillis);
    }

    public boolean postAtTime(Object token, long uptimeMillis)
    {
        return postAtTime(this, token, uptimeMillis);
    }

    public boolean postDelayed(long delayMillis)
    {
        return postDelayed(this, delayMillis);
    }
}
