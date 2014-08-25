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

package net.lacolaco.smileessence.command.status;

import android.app.Activity;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.TalkChainDialogFragment;
import twitter4j.Status;

public class StatusCommandOpenTalkView extends StatusCommand
{

    // ------------------------------ FIELDS ------------------------------

    private final Account account;

    // --------------------------- CONSTRUCTORS ---------------------------

    public StatusCommandOpenTalkView(Activity activity, Status status, Account account)
    {
        super(R.id.key_command_status_open_chain, activity, status);
        this.account = account;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getText()
    {
        return getActivity().getString(R.string.command_status_open_talk_view);
    }

    @Override
    public boolean isEnabled()
    {
        return getOriginalStatus().getInReplyToStatusId() >= 0;
    }

    // -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean execute()
    {
        TalkChainDialogFragment dialogFragment = new TalkChainDialogFragment();
        dialogFragment.setStatusID(getOriginalStatus().getId());
        DialogHelper.showDialog(getActivity(), dialogFragment);
        return false;
    }
}
