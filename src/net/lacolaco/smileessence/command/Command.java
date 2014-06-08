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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.command.status.*;
import net.lacolaco.smileessence.command.user.*;
import net.lacolaco.smileessence.viewmodel.IViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    private final int key;
    private final Activity activity;

    // -------------------------- STATIC METHODS --------------------------

    public Command(int key, Activity activity)
    {
        this.key = key;
        this.activity = activity;
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static List<Command> getAll(Activity activity)
    {
        List<Command> commands = new ArrayList<>();
        //Status
        commands.add(new StatusCommandAddToReply(activity, null));
        commands.add(new StatusCommandReplyToAll(activity, null, null));
        commands.add(new StatusCommandOpenTalkView(activity, null, null));
        commands.add(new StatusCommandFavAndRT(activity, null, null));
        commands.add(new StatusCommandQuote(activity, null));
        commands.add(new StatusCommandShare(activity, null));
        commands.add(new StatusCommandOpenInBrowser(activity, null));
        commands.add(new StatusCommandClipboard(activity, null));
        commands.add(new StatusCommandTofuBuster(activity, null));
        commands.add(new StatusCommandNanigaja(activity, null, null));
        commands.add(new StatusCommandMakeAnonymous(activity, null, null));
        commands.add(new StatusCommandAddToIgnore(activity, null));
        //User
        commands.add(new UserCommandReply(activity, null));
        commands.add(new UserCommandAddToReply(activity, null));
        commands.add(new UserCommandSendMessage(activity, null, null));
        commands.add(new UserCommandBlock(activity, null, null));
        commands.add(new UserCommandUnblock(activity, null, null));
        commands.add(new UserCommandReportForSpam(activity, null, null));
        commands.add(new UserCommandOpenFavstar(activity, null));
        commands.add(new UserCommandOpenAclog(activity, null));
        commands.add(new UserCommandOpenTwilog(activity, null));
        commands.add(new UserCommandIntroduce(activity, null));
        return commands;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Activity getActivity()
    {
        return activity;
    }

    public int getKey()
    {
        return key;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface IViewModel ---------------------

    @Override
    public View getView(Activity activity, LayoutInflater inflater, View convertedView)
    {
        if(convertedView == null)
        {
            convertedView = inflater.inflate(R.layout.menu_item_simple_text, null);
        }
        TextView textView = (TextView) convertedView.findViewById(R.id.textView_menuItem_simple);
        textView.setText(getText());
        return convertedView;
    }

    // -------------------------- OTHER METHODS --------------------------

    public abstract boolean execute();

    public abstract String getText();

    public abstract boolean isEnabled();
}
