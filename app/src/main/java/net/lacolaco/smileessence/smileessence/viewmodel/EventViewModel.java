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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.util.StringUtils;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.UserDetailDialogFragment;
import net.lacolaco.smileessence.view.listener.ListItemClickListener;
import twitter4j.Status;
import twitter4j.User;

import java.util.Date;

public class EventViewModel implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    private EnumEvent event;
    private long sourceUserID;
    private long targetStatusID;
    private String sourceScreenName;
    private String sourceName;
    private String iconURL;
    private String targetText;
    private Date createdAt;

    // --------------------------- CONSTRUCTORS ---------------------------

    public EventViewModel(EnumEvent event, User source)
    {
        this(event, source, null);
    }

    public EventViewModel(EnumEvent event, User source, Status status)
    {
        this.event = event;
        this.createdAt = new Date();
        this.sourceUserID = source.getId();
        this.sourceScreenName = source.getScreenName();
        this.sourceName = source.getName();
        this.iconURL = source.getProfileImageURL();

        if(status != null)
        {
            if(event == EnumEvent.RETWEETED)
            {
                this.targetStatusID = status.getRetweetedStatus().getId();
                this.targetText = status.getRetweetedStatus().getText();
            }
            else
            {
                this.targetStatusID = status.getId();
                this.targetText = status.getText();
            }
        }
        else
        {
            this.targetStatusID = -1L;
            this.targetText = "";
        }
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public EnumEvent getEvent()
    {
        return event;
    }

    public String getIconURL()
    {
        return iconURL;
    }

    public String getSourceName()
    {
        return sourceName;
    }

    public String getSourceScreenName()
    {
        return sourceScreenName;
    }

    public long getSourceUserID()
    {
        return sourceUserID;
    }

    public long getTargetStatusID()
    {
        return targetStatusID;
    }

    public String getTargetText()
    {
        return targetText;
    }

    public boolean isStatusEvent()
    {
        return targetStatusID != -1L;
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
        ImageCache.getInstance().setImageToView(getIconURL(), icon);
        TextView header = (TextView) convertedView.findViewById(R.id.textview_status_header);
        header.setTextSize(textSize);
        int colorHeader = Themes.getStyledColor(activity, theme, R.attr.color_status_text_mine, 0);
        header.setTextColor(colorHeader);
        header.setText(getFormattedString(activity));
        TextView content = (TextView) convertedView.findViewById(R.id.textview_status_text);
        content.setTextSize(textSize);
        int colorNormal = Themes.getStyledColor(activity, theme, R.attr.color_status_text_normal, 0);
        content.setTextColor(colorNormal);
        content.setText(getTargetText());
        TextView footer = (TextView) convertedView.findViewById(R.id.textview_status_footer);
        footer.setTextSize(textSize - 2);
        int colorFooter = Themes.getStyledColor(activity, theme, R.attr.color_status_text_footer, 0);
        footer.setTextColor(colorFooter);
        footer.setText(StringUtils.dateToString(getCreatedAt()));
        ImageView favorited = (ImageView) convertedView.findViewById(R.id.imageview_status_favorited);
        favorited.setVisibility(View.GONE);
        int colorBgNormal = Themes.getStyledColor(activity, theme, R.attr.color_status_bg_normal, 0);
        convertedView.setBackgroundColor(colorBgNormal);
        convertedView.setOnClickListener(new ListItemClickListener(activity, new Runnable()
        {
            @Override
            public void run()
            {
                UserDetailDialogFragment fragment = new UserDetailDialogFragment();
                fragment.setUserID(getSourceUserID());
                DialogHelper.showDialog(activity, fragment);
            }
        }));
        return convertedView;
    }

    // -------------------------- OTHER METHODS --------------------------

    public String getFormattedString(Context context)
    {
        return context.getString(event.getTextFormatResourceID(), sourceScreenName);
    }
}
