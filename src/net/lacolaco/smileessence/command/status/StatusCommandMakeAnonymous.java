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
import net.lacolaco.smileessence.command.IConfirmable;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TweetBuilder;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.FavoriteTask;
import net.lacolaco.smileessence.twitter.task.TweetTask;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;

public class StatusCommandMakeAnonymous extends StatusCommand implements IConfirmable
{

    // ------------------------------ FIELDS ------------------------------

    private final Account account;

    // -------------------------- STATIC METHODS --------------------------

    public StatusCommandMakeAnonymous(Activity activity, Status status, Account account)
    {
        super(R.id.key_command_status_make_anonymous, activity, status);
        this.account = account;
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static String build(Activity activity, Status status, Account account)
    {
        String str = status.getText();
        String header = "";
        if(str.startsWith("."))
        {
            str = str.replaceFirst(".", "");
        }
        if(str.startsWith(String.format("@%s", account.screenName)))
        {
            str = str.replaceFirst(String.format("@%s", account.screenName), "").trim();
        }
        str = activity.getString(R.string.format_status_command_make_anonymous, str).trim();
        return str;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getText()
    {
        return getActivity().getString(R.string.command_status_make_anonymous);
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
        StatusUpdate update = new TweetBuilder().setText(build(getActivity(), getOriginalStatus(), account)).build();
        Twitter twitter = new TwitterApi(account).getTwitter();
        new TweetTask(twitter, update, getActivity()).execute();
        new FavoriteTask(twitter, getOriginalStatus().getId(), getActivity()).execute();
        return true;
    }
}
