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
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.notification.NotificationType;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.ShowUserTask;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.UserDetailDialogFragment;
import twitter4j.User;

public class CommandOpenUserDetail extends Command
{

    // ------------------------------ FIELDS ------------------------------

    private final String screenName;
    private final Account account;

    // --------------------------- CONSTRUCTORS ---------------------------

    public CommandOpenUserDetail(Activity activity, String screenName, Account account)
    {
        super(-1, activity);
        this.screenName = screenName;
        this.account = account;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getText()
    {
        return String.format("@%s", screenName);
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    // -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean execute()
    {
        new ShowUserTask(new TwitterApi(account).getTwitter(), screenName)
        {
            @Override
            protected void onPostExecute(User user)
            {
                super.onPostExecute(user);
                if(user != null)
                {
                    UserDetailDialogFragment fragment = new UserDetailDialogFragment();
                    fragment.setUserID(user.getId());
                    DialogHelper.showDialog(getActivity(), fragment);
                }
                else
                {
                    Notificator.publish(getActivity(), R.string.notice_error_show_user, NotificationType.ALERT);
                }
            }
        }.execute();

        return false;
    }
}
