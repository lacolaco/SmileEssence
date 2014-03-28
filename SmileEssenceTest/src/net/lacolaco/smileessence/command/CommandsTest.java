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

import android.content.Context;
import android.test.InstrumentationTestCase;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.command.event.EventCommand;
import net.lacolaco.smileessence.command.event.EventCommandReply;
import net.lacolaco.smileessence.command.message.MessageCommand;
import net.lacolaco.smileessence.command.message.MessageCommandReply;
import net.lacolaco.smileessence.command.status.StatusCommand;
import net.lacolaco.smileessence.command.status.StatusCommandReply;
import net.lacolaco.smileessence.command.user.UserCommand;
import net.lacolaco.smileessence.command.user.UserCommandReply;
import net.lacolaco.smileessence.data.StatusCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.util.TwitterMock;
import net.lacolaco.smileessence.viewmodel.EnumEvent;
import net.lacolaco.smileessence.viewmodel.EventViewModel;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;

public class CommandsTest extends InstrumentationTestCase
{


    TwitterMock mock;

    @Override
    public void setUp() throws Exception
    {
        mock = new TwitterMock(getInstrumentation().getContext());
    }

    public void testStatusCommand() throws Exception
    {
        Context context = getInstrumentation().getTargetContext();
        Account account = new Account(mock.getAccessToken(), mock.getAccessTokenSecret(), mock.getUserMock().getId(), mock.getUserMock().getScreenName());
        Status status = mock.getStatusMock();
        StatusCache.getInstance().put(status);

        StatusCommand reply = new StatusCommandReply(context, account, status.getId());
        assertTrue(reply.isVisible());
        reply.setVisible(false);
        assertFalse(reply.isVisible());
        assertEquals(R.id.key_command_status_reply, reply.getKey());
        assertEquals(context.getString(R.string.command_status_reply), reply.getText());
        assertTrue(reply.execute());
    }

    public void testUserCommand() throws Exception
    {
        Context context = getInstrumentation().getTargetContext();
        Account account = new Account(mock.getAccessToken(), mock.getAccessTokenSecret(), mock.getUserMock().getId(), mock.getUserMock().getScreenName());
        User user = mock.getUserMock();

        UserCommand reply = new UserCommandReply(context, account, user.getId());
        assertTrue(reply.isVisible());
        reply.setVisible(false);
        assertFalse(reply.isVisible());
        assertEquals(context.getString(R.string.command_user_reply), reply.getText());
        assertTrue(reply.execute());
    }

    public void testMessageCommand() throws Exception
    {
        Context context = getInstrumentation().getTargetContext();
        Account account = new Account(mock.getAccessToken(), mock.getAccessTokenSecret(), mock.getUserMock().getId(), mock.getUserMock().getScreenName());
        DirectMessage message = mock.getDirectMessageMock();

        MessageCommand reply = new MessageCommandReply(context, account, message.getId());
        assertTrue(reply.isVisible());
        reply.setVisible(false);
        assertFalse(reply.isVisible());
        assertEquals(context.getString(R.string.command_message_reply), reply.getText());
        assertTrue(reply.execute());
    }

    public void testEventCommand() throws Exception
    {
        Context context = getInstrumentation().getTargetContext();
        Account account = new Account(mock.getAccessToken(), mock.getAccessTokenSecret(), mock.getUserMock().getId(), mock.getUserMock().getScreenName());
        EventViewModel message = new EventViewModel(EnumEvent.MENTIONED, mock.getStatusMock().getUser(), mock.getStatusMock());

        EventCommand reply = new EventCommandReply(context, account, message);
        assertTrue(reply.isVisible());
        reply.setVisible(false);
        assertFalse(reply.isVisible());
        assertEquals(context.getString(R.string.command_event_reply), reply.getText());
        assertTrue(reply.execute());
    }
}
