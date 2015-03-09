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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.logging.Logger;

public class Themes
{

    // ------------------------------ FIELDS ------------------------------

    public static final int THEME_DARK = 0;
    public static final int THEME_LIGHT = 1;

    // -------------------------- STATIC METHODS --------------------------

    public static int getTheme(int index)
    {
        switch(index)
        {
            case THEME_DARK:
            {
                Logger.debug("Theme:Dark");
                return R.style.theme_dark;
            }
            case THEME_LIGHT:
            {
                Logger.debug("Theme:Light");
                return R.style.theme_light;
            }
            default:
            {
                Logger.debug("Theme:Default");
                return R.style.theme_dark;
            }
        }
    }

    public static int getStyledColor(Context context, int theme, int attribute, int defaultColor)
    {
        int styleResID = theme == THEME_LIGHT ? R.style.theme_light : R.style.theme_dark;
        TypedArray array = context.obtainStyledAttributes(styleResID, new int[]{attribute});
        int color = array.getColor(0, defaultColor);
        array.recycle();
        return color;
    }

    public static Drawable getStyledDrawable(Context context, int theme, int attribute)
    {
        int styleResID = theme == THEME_LIGHT ? R.style.theme_light : R.style.theme_dark;
        TypedArray array = context.obtainStyledAttributes(styleResID, new int[]{attribute});
        Drawable drawable = array.getDrawable(0);
        array.recycle();
        return drawable;
    }
}
