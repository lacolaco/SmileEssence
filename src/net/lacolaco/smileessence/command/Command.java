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
import net.lacolaco.smileessence.command.message.MessageCommandDelete;
import net.lacolaco.smileessence.command.message.MessageCommandReply;
import net.lacolaco.smileessence.command.status.*;
import net.lacolaco.smileessence.command.user.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Command
{

    private final int key;
    private final Activity activity;

    public Command(int key, Activity activity)
    {
        this.key = key;
        this.activity = activity;
    }

    public int getKey()
    {
        return key;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public abstract String getText();

    public abstract boolean execute();

    public abstract boolean isEnabled();

    public static List<Command> getAll(Activity activity)
    {
        List<Command> commands = new ArrayList<>();
        //Status
        commands.add(new StatusCommandReply(activity, null));
        commands.add(new StatusCommandReplyToAll(activity, null, null));
        commands.add(new StatusCommandFavorite(activity, null, null));
        commands.add(new StatusCommandRetweet(activity, null, null));
        commands.add(new StatusCommandDelete(activity, null, null));
        commands.add(new StatusCommandFavAndRT(activity, null, null));
        commands.add(new StatusCommandQuote(activity, null));
        commands.add(new StatusCommandShare(activity, null));
        commands.add(new StatusCommandOpenInBrowser(activity, null));
        commands.add(new StatusCommandClipboard(activity, null));
        commands.add(new StatusCommandTofuBuster(activity, null));
        commands.add(new StatusCommandNanigaja(activity, null, null));
        commands.add(new StatusCommandMakeAnonymous(activity, null, null));
        //User
        commands.add(new UserCommandReply(activity, null));
        commands.add(new UserCommandAddToReply(activity, null));
        commands.add(new UserCommandSendMessage(activity, null));
        commands.add(new UserCommandFollow(activity, null, null));
        commands.add(new UserCommandUnfollow(activity, null, null));
        commands.add(new UserCommandBlock(activity, null, null));
        commands.add(new UserCommandUnblock(activity, null, null));
        commands.add(new UserCommandReportForSpam(activity, null, null));
        commands.add(new UserCommandOpenFavstar(activity, null));
        commands.add(new UserCommandOpenAclog(activity, null));
        commands.add(new UserCommandOpenTwilog(activity, null));
        commands.add(new UserCommandIntroduce(activity, null));
        //Message
        commands.add(new MessageCommandReply(activity, null));
        commands.add(new MessageCommandDelete(activity, null, null));
        return commands;
    }
}
