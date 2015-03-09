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

import java.util.Stack;

public class DialogHelper
{

    // ------------------------------ FIELDS ------------------------------

    private static final String TAG_DIALOG = "dialog";

    private static Stack<String> dialogStack = new Stack<>();
    private final Activity activity;
    private final DialogFragment dialogFragment;

    // -------------------------- STATIC METHODS --------------------------

    public DialogHelper(Activity activity, DialogFragment dialogFragment)
    {
        this.activity = activity;
        this.dialogFragment = dialogFragment;
    }

    public static void closeDialog(Activity activity)
    {
        close(activity, TAG_DIALOG);
    }

    /**
     * Close all stacked dialog
     */
    public static void closeAll(Activity activity)
    {
        while(dialogStack.size() > 0)
        {
            String tag = dialogStack.pop();
            close(activity, tag);
        }
    }

    public static void showDialog(Activity activity, DialogFragment dialogFragment)
    {
        showDialog(activity, dialogFragment, TAG_DIALOG);
    }

    /**
     * Please expressly closing
     */
    public static void showDialog(Activity activity, DialogFragment dialogFragment, String tag)
    {
        close(activity, tag);
        dialogStack.push(tag);
        dialogFragment.show(activity.getFragmentManager().beginTransaction(), tag);
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static void close(Activity activity, String tag)
    {
        if(activity != null)
        {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            DialogFragment oldDialog = (DialogFragment) activity.getFragmentManager().findFragmentByTag(tag);
            if(oldDialog != null)
            {
                transaction.remove(oldDialog);
                //            oldDialog.dismiss();
            }
            //        transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    // -------------------------- OTHER METHODS --------------------------

    public void show()
    {
        showDialog(activity, dialogFragment);
    }
}
