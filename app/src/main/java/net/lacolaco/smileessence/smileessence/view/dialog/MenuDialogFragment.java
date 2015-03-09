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

import android.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.IConfirmable;

public abstract class MenuDialogFragment extends DialogFragment
{

    // ------------------------------ FIELDS ------------------------------

    protected final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            MenuDialogFragment.this.onItemClick(adapterView, i);
        }
    };

    protected void executeCommand(Command command)
    {
        if(command.execute())
        {
            dismiss();
            DialogHelper.closeAll(getActivity());
        }
    }

    protected void onItemClick(AdapterView<?> adapterView, int i)
    {
        final Command command = (Command) adapterView.getItemAtPosition(i);
        if(command != null)
        {
            if(command instanceof IConfirmable)
            {
                ConfirmDialogFragment.show(getActivity(), getString(R.string.dialog_confirm_commands), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        executeCommand(command);
                    }
                });
            }
            else
            {
                executeCommand(command);
            }
        }
    }
}
