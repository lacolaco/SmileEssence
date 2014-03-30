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
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.RetweetTask;
import twitter4j.Status;
import twitter4j.User;

public class StatusCommandRetweet extends StatusCommand
{

    private final Account account;

    public StatusCommandRetweet(Activity activity, Status status, Account account)
    {
        super(R.id.key_command_status_retweet, activity, status);
        this.account = account;
    }

    @Override
    public String getText()
    {
        return getActivity().getString(R.string.command_status_retweet);
    }

    @Override
    public boolean execute()
    {
        RetweetTask task = new RetweetTask(new TwitterApi(account).getTwitter(), getStatus().getId(), getActivity());
        task.execute();
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        User user = getStatus().getUser();
        if(user.isProtected())
        {
            return false;
        }
        if(user.getId() == account.userID)
        {
            return false;
        }
        return true;
    }
}
