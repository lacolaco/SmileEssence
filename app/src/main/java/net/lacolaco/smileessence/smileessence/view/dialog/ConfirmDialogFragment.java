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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;

public abstract class ConfirmDialogFragment extends DialogFragment
{

    // ------------------------------ FIELDS ------------------------------

    public static final String ARG_TEXT = "text";
    public static final String TAG = "confirmDialog";
    private final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            onButtonClick(which);
            dialog.dismiss();
        }
    };
    private int layoutResourceID;
    private String text;

    // -------------------------- STATIC METHODS --------------------------

    public static void show(Activity activity, String text, final Runnable onYes)
    {
        show(activity, text, onYes, null, true);
    }

    public static void show(Activity activity, String text, final Runnable onYes, boolean ignorable)
    {
        show(activity, text, onYes, null, ignorable);
    }

    public static void show(Activity activity, String text, final Runnable onOK, final Runnable onCancel, boolean ignorable)
    {
        boolean confirm = new UserPreferenceHelper(activity).getValue(R.string.key_setting_show_confirm_dialog, true);
        if(!confirm && ignorable)
        {
            onOK.run();
            return;
        }
        final ConfirmDialogFragment fragment = new ConfirmDialogFragment()
        {
            @Override
            public void onButtonClick(int which)
            {
                switch(which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                    {
                        if(onOK != null)
                        {
                            this.dismiss();
                            onOK.run();
                        }
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE:
                    {
                        if(onCancel != null)
                        {
                            this.dismiss();
                            onCancel.run();
                        }
                        break;
                    }
                }
            }
        };
        fragment.setText(text);
        DialogHelper.showDialog(activity, fragment, TAG);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public void setText(String text)
    {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        setArguments(args);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null)
        {
            text = args.getString(ARG_TEXT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getActivity()).setTitle(text)
                                                     .setCancelable(false)
                                                     .setPositiveButton(R.string.alert_dialog_ok, listener)
                                                     .setNegativeButton(R.string.alert_dialog_cancel, listener)
                                                     .create();
    }

    // -------------------------- OTHER METHODS --------------------------

    public abstract void onButtonClick(int which);
}
