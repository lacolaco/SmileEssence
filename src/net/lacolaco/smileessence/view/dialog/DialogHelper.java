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

package net.lacolaco.smileessence.view.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

public class DialogHelper
{

    private static final String TAG_DIALOG = "dialog";
    private final Activity activity;
    private final DialogFragment dialogFragment;

    public DialogHelper(Activity activity, DialogFragment dialogFragment)
    {
        this.activity = activity;
        this.dialogFragment = dialogFragment;
    }

    public static void showDialog(Activity activity, DialogFragment dialogFragment)
    {
        close(activity);
        dialogFragment.show(activity.getFragmentManager().beginTransaction(), TAG_DIALOG);
    }

    public static void close(Activity activity)
    {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        DialogFragment oldDialog = (DialogFragment) activity.getFragmentManager().findFragmentByTag(TAG_DIALOG);
        if(oldDialog != null)
        {
            transaction.remove(oldDialog);
            //            oldDialog.dismiss();
        }
        //        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void show()
    {
        showDialog(activity, dialogFragment);
    }
}
