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

package net.lacolaco.smileessence.viewmodel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.util.NameStyles;
import net.lacolaco.smileessence.util.StringUtils;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.MessageDetailDialogFragment;
import net.lacolaco.smileessence.view.dialog.UserDetailDialogFragment;
import net.lacolaco.smileessence.view.listener.ListItemClickListener;
import twitter4j.DirectMessage;

import java.util.Date;

public class MessageViewModel implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    public static final String DETAIL_DIALOG = "messageDetail";
    private final long id;
    private final long senderID;
    private final String senderScreenName;
    private final String senderName;
    private final String senderIconURL;
    private final long recipientId;
    private final String recipientScreenName;
    private final String text;
    private final Date createdAt;
    private final boolean myMessage;

    // --------------------------- CONSTRUCTORS ---------------------------

    public MessageViewModel(DirectMessage directMessage, Account account)
    {
        id = directMessage.getId();
        UserCache.getInstance().put(directMessage.getSender());
        senderID = directMessage.getSenderId();
        senderScreenName = directMessage.getSenderScreenName();
        senderName = directMessage.getSender().getName();
        senderIconURL = directMessage.getSender().getProfileImageURL();
        recipientId = directMessage.getRecipientId();
        recipientScreenName = directMessage.getRecipientScreenName();
        text = directMessage.getText();
        createdAt = directMessage.getCreatedAt();
        myMessage = isMyMessage(account);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Date getCreatedAt()
    {
        return createdAt;
    }

    private String getFooterText()
    {
        String s = StringUtils.dateToString(getCreatedAt());
        if(isMyMessage())
        {
            s = String.format("%s to @%s", s, recipientScreenName);
        }
        return s;
    }

    public long getID()
    {
        return id;
    }

    public long getRecipientId()
    {
        return recipientId;
    }

    public String getRecipientScreenName()
    {
        return recipientScreenName;
    }

    public String getSenderIconURL()
    {
        return senderIconURL;
    }

    public long getSenderID()
    {
        return senderID;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public String getSenderScreenName()
    {
        return senderScreenName;
    }

    public String getText()
    {
        return text;
    }

    public boolean isMyMessage()
    {
        return myMessage;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface IViewModel ---------------------

    @Override
    public View getView(final Activity activity, LayoutInflater inflater, View convertedView)
    {
        if(convertedView == null)
        {
            convertedView = inflater.inflate(R.layout.list_item_status, null);
        }
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(activity);
        int textSize = preferenceHelper.getValue(R.string.key_setting_text_size, 10);
        int nameStyle = preferenceHelper.getValue(R.string.key_setting_namestyle, 0);
        int theme = ((MainActivity) activity).getThemeIndex();
        NetworkImageView icon = (NetworkImageView) convertedView.findViewById(R.id.imageview_status_icon);
        ImageCache.getInstance().setImageToView(getSenderIconURL(), icon);
        icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserDetailDialogFragment dialogFragment = new UserDetailDialogFragment();
                dialogFragment.setUserID(senderID);
                DialogHelper.showDialog(activity, dialogFragment);
            }
        });
        TextView header = (TextView) convertedView.findViewById(R.id.textview_status_header);
        header.setTextSize(textSize);
        int colorHeader = Themes.getStyledColor(activity, theme, R.attr.color_message_text_header, 0);
        header.setTextColor(colorHeader);
        header.setText(getNameString(nameStyle));
        TextView content = (TextView) convertedView.findViewById(R.id.textview_status_text);
        content.setTextSize(textSize);
        int colorNormal = Themes.getStyledColor(activity, theme, R.attr.color_status_text_normal, 0);
        content.setTextColor(colorNormal);
        content.setText(getText());
        TextView footer = (TextView) convertedView.findViewById(R.id.textview_status_footer);
        footer.setTextSize(textSize - 2);
        int colorFooter = Themes.getStyledColor(activity, theme, R.attr.color_status_text_footer, 0);
        footer.setTextColor(colorFooter);
        footer.setText(getFooterText());
        ImageView favorited = (ImageView) convertedView.findViewById(R.id.imageview_status_favorited);
        favorited.setVisibility(View.GONE);
        int colorBgMessage = Themes.getStyledColor(activity, theme, R.attr.color_message_bg_normal, 0);
        convertedView.setBackgroundColor(colorBgMessage);
        convertedView.setOnClickListener(new ListItemClickListener(activity, new Runnable()
        {
            @Override
            public void run()
            {
                MessageDetailDialogFragment dialogFragment = new MessageDetailDialogFragment();
                dialogFragment.setMessageID(getID());
                DialogHelper.showDialog(activity, dialogFragment);
            }
        }));
        return convertedView;
    }

    private String getNameString(int nameStyle)
    {
        return NameStyles.getNameString(nameStyle, getSenderScreenName(), getSenderName());
    }

    private boolean isMyMessage(Account account)
    {
        return senderID == account.userID;
    }
}
