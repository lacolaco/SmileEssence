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

package net.lacolaco.smileessence.command.user;

import android.content.Context;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import twitter4j.User;

public abstract class UserCommand extends Command
{

    private final Account account;
    private final long userID;

    public UserCommand(int key, Context context, Account account, long userID)
    {
        super(key, context);
        this.account = account;
        this.userID = userID;
    }

    public Account getAccount()
    {
        return account;
    }

    public long getUserID()
    {
        return userID;
    }

    protected final User tryGetUser()
    {
        return TwitterUtils.tryGetUser(account, userID);
    }
}
