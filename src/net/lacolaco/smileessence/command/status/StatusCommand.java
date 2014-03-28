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

import android.content.Context;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.ShowStatusTask;
import twitter4j.Status;

import java.util.concurrent.ExecutionException;

public abstract class StatusCommand extends Command
{

    private final Account account;
    private final long statusID;

    public StatusCommand(int key, Context context, Account account, long statusID)
    {
        super(key, context);
        this.account = account;
        this.statusID = statusID;
    }

    /**
     * Get status from api if cache is not found
     *
     * @return null if api error happen
     */
    public final Status tryGetStatus()
    {
        Status status = StatusCache.getInstance().get(statusID);
        if(status != null)
        {
            return status;
        }
        ShowStatusTask task = new ShowStatusTask(new TwitterApi(account).getTwitter(), statusID);
        task.execute();
        try
        {
            status = task.get();
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            Logger.error(e.toString());
        }
        return status;
    }

    public abstract String getText();

    public abstract boolean execute();

    public Account getAccount()
    {
        return account;
    }

    public long getStatusID()
    {
        return statusID;
    }
}
