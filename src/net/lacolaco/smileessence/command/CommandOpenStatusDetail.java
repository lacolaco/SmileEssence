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

package net.lacolaco.smileessence.command;

import android.app.Activity;
import android.app.ProgressDialog;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.ShowStatusTask;
import twitter4j.Status;

import java.util.concurrent.ExecutionException;

public class CommandOpenStatusDetail extends Command
{

    private final long statusID;
    private final Account account;

    public CommandOpenStatusDetail(Activity activity, long statusID, Account account)
    {
        super(-1, activity);
        this.statusID = statusID;
        this.account = account;
    }

    @Override
    public String getText()
    {
        return getActivity().getString(R.string.command_open_status_detail);
    }

    @Override
    public boolean execute()
    {
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getActivity().getString(R.string.dialog_message_now_loading));
        ShowStatusTask task = new ShowStatusTask(new TwitterApi(account).getTwitter(), statusID);
        task.execute();
        try
        {
            Status status = task.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
            new Notificator(getActivity(), R.string.notice_error_show_status, NotificationType.ALERT).publish();
            return false;
        }
        finally
        {
            progressDialog.dismiss();
        }
        //TODO status dialog
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
