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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.util.NameStyles;
import net.lacolaco.smileessence.util.StringUtils;
import twitter4j.DirectMessage;

import java.util.Date;

public class MessageViewModel implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    private long id;
    private long senderID;
    private String senderScreenName;
    private String senderName;
    private String senderIconURL;
    private String text;
    private Date createdAt;
    private boolean myMessage;

    // --------------------------- CONSTRUCTORS ---------------------------

    public MessageViewModel(DirectMessage directMessage, Account account)
    {
        id = directMessage.getId();
        UserCache.getInstance().put(directMessage.getSender());
        senderID = directMessage.getSenderId();
        senderScreenName = directMessage.getSenderScreenName();
        senderName = directMessage.getSender().getName();
        senderIconURL = directMessage.getSender().getProfileImageURL();
        text = directMessage.getText();
        createdAt = directMessage.getCreatedAt();
        myMessage = isMyMessage(account);
    }

    private boolean isMyMessage(Account account)
    {
        return senderID == account.userID;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public long getID()
    {
        return id;
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
    public View getView(Context context, LayoutInflater inflater, View convertedView)
    {
        if(convertedView == null)
        {
            convertedView = inflater.inflate(R.layout.list_item_status, null);
        }
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(context);
        int textSize = preferenceHelper.getValue(R.string.key_setting_text_size, 10);
        int nameStyle = preferenceHelper.getValue(R.string.key_setting_namestyle, 0);
        NetworkImageView icon = (NetworkImageView)convertedView.findViewById(R.id.imageview_status_icon);
        ImageCache.getInstance().setImageToView(getSenderIconURL(), icon);
        TextView header = (TextView)convertedView.findViewById(R.id.textview_status_header);
        header.setTextSize(textSize);
        header.setText(NameStyles.getNameString(nameStyle, getSenderScreenName(), getSenderName()));
        TextView content = (TextView)convertedView.findViewById(R.id.textview_status_text);
        content.setTextSize(textSize);
        content.setText(getText());
        TextView footer = (TextView)convertedView.findViewById(R.id.textview_status_footer);
        footer.setTextSize(textSize);
        footer.setText(StringUtils.dateToString(getCreatedAt()));
        ImageView favorited = (ImageView)convertedView.findViewById(R.id.imageview_status_favorited);
        favorited.setVisibility(View.GONE);

        return convertedView;
    }
}
