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
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.data.FavoriteCache;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.data.UserCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.Morse;
import net.lacolaco.smileessence.util.NameStyles;
import net.lacolaco.smileessence.util.StringUtils;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.StatusDetailDialogFragment;
import net.lacolaco.smileessence.view.dialog.UserDetailDialogFragment;
import net.lacolaco.smileessence.view.listener.ListItemClickListener;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusViewModel implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    public static final String STATUS_DIALOG = "statusDialog";
    private long id;
    private long userID;
    private String screenName;
    private String name;
    private String iconURL;
    private String text;
    private Date createdAt;
    private String source;
    private boolean isProtected;
    private StatusViewModel retweetedStatus;
    private UserMentionEntity[] mentions;
    private HashtagEntity[] hashtags;
    private MediaEntity[] media;
    private URLEntity[] urls;
    private SymbolEntity[] symbols;
    private boolean isMyStatus;
    private boolean isMention;
    private boolean isRetweetOfMe;

    // --------------------------- CONSTRUCTORS ---------------------------

    public StatusViewModel(Status status, Account account)
    {
        if(status.isRetweet())
        {
            retweetedStatus = new StatusViewModel(status.getRetweetedStatus(), account);
        }
        id = status.getId();
        text = TwitterUtils.replaceURLEntities(status.getText(), status.getURLEntities(), false);
        createdAt = status.getCreatedAt();
        source = status.getSource();
        mentions = status.getUserMentionEntities();
        hashtags = status.getHashtagEntities();
        media = status.getMediaEntities();
        urls = status.getURLEntities();
        symbols = status.getSymbolEntities();
        User user = status.getUser();
        UserCache.getInstance().put(user);
        userID = user.getId();
        screenName = user.getScreenName();
        name = user.getName();
        iconURL = user.getProfileImageURLHttps();
        isProtected = user.isProtected();
        setMention(isMention(account.screenName));
        setMyStatus(isMyStatus(account.userID));
        setRetweetOfMe(isRetweetOfMe(account.userID));
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public Date getCreatedAt()
    {
        if(isRetweet())
        {
            return retweetedStatus.createdAt;
        }
        return createdAt;
    }

    private List<Long> getEmbeddedStatusIDs()
    {
        ArrayList<Long> list = new ArrayList<>();
        for(URLEntity url : urls)
        {
            Uri uri = Uri.parse(url.getExpandedURL());
            if(uri.getHost().equals("twitter.com"))
            {
                String[] arr = uri.toString().split("/");
                if(arr[arr.length - 2].equals("status"))
                {
                    list.add(Long.parseLong(arr[arr.length - 1].split("\\?")[0]));
                }
            }
        }
        return list;
    }

    private String getFooterText()
    {
        StringBuilder builder = new StringBuilder();
        if(isRetweet())
        {
            builder.append("(RT: ").append(this.screenName).append(") ");
        }
        builder.append(StringUtils.dateToString(getCreatedAt()));
        builder.append(" via ");
        builder.append(Html.fromHtml(getSource()));
        return builder.toString();
    }

    public HashtagEntity[] getHashtags()
    {
        if(isRetweet())
        {
            return retweetedStatus.hashtags;
        }
        return hashtags;
    }

    public String getIconURL()
    {
        if(isRetweet())
        {
            return retweetedStatus.iconURL;
        }
        return iconURL;
    }

    public long getID()
    {
        return id;
    }

    public MediaEntity[] getMedia()
    {
        if(isRetweet())
        {
            return retweetedStatus.media;
        }
        return media;
    }

    public UserMentionEntity[] getMentions()
    {
        if(isRetweet())
        {
            return retweetedStatus.mentions;
        }
        return mentions;
    }

    public String getName()
    {
        if(isRetweet())
        {
            return retweetedStatus.name;
        }
        return name;
    }

    public StatusViewModel getOriginal()
    {
        return isRetweet() ? retweetedStatus : this;
    }

    public long getOriginalUserID()
    {
        return isRetweet() ? getRetweetedStatus().getUserID() : getUserID();
    }

    public StatusViewModel getRetweetedStatus()
    {
        return retweetedStatus;
    }

    public String getScreenName()
    {
        if(isRetweet())
        {
            return retweetedStatus.screenName;
        }
        return screenName;
    }

    public String getSource()
    {
        if(isRetweet())
        {
            return retweetedStatus.source;
        }
        return source;
    }

    public SymbolEntity[] getSymbols()
    {
        return symbols;
    }

    public String getText()
    {
        if(isRetweet())
        {
            return retweetedStatus.text;
        }
        return text;
    }

    public URLEntity[] getURLs()
    {
        return urls;
    }

    public long getUserID()
    {
        return userID;
    }

    public boolean isFavorited()
    {
        if(isRetweet())
        {
            return FavoriteCache.getInstance().get(retweetedStatus.id);
        }
        return FavoriteCache.getInstance().get(id);
    }

    public boolean isMention()
    {
        if(isRetweet())
        {
            return retweetedStatus.isMention;
        }
        return isMention;
    }

    public void setMention(boolean mention)
    {
        isMention = mention;
    }

    public boolean isMyStatus()
    {
        if(isRetweet())
        {
            return retweetedStatus.isMyStatus;
        }
        return isMyStatus;
    }

    public void setMyStatus(boolean myStatus)
    {
        isMyStatus = myStatus;
    }

    public boolean isProtected()
    {
        if(isRetweet())
        {
            return retweetedStatus.isProtected;
        }
        return isProtected;
    }

    public boolean isRetweet()
    {
        return retweetedStatus != null;
    }

    public boolean isRetweetOfMe()
    {
        return isRetweetOfMe;
    }

    public void setRetweetOfMe(boolean retweet)
    {
        this.isRetweetOfMe = retweet;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface IViewModel ---------------------

    @Override
    public View getView(final Activity activity, final LayoutInflater inflater, View convertedView)
    {
        boolean extendStatusURL = new UserPreferenceHelper(activity).getValue(R.string.key_setting_extend_status_url, true);
        return getView(activity, inflater, convertedView, extendStatusURL);
    }

    // -------------------------- OTHER METHODS --------------------------

    public View getView(final Activity activity, final LayoutInflater inflater, View convertedView, boolean extendStatusURL)
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
        icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onIconClick(activity);
            }
        });
        TextView header = (TextView) convertedView.findViewById(R.id.textview_status_header);
        header.setTextSize(textSize);
        int colorHeader = Themes.getStyledColor(activity, theme, R.attr.color_status_text_header, 0);
        int colorMineHeader = Themes.getStyledColor(activity, theme, R.attr.color_status_text_mine, 0);
        header.setTextColor(isMyStatus() ? colorMineHeader : colorHeader);
        header.setText(NameStyles.getNameString(nameStyle, getScreenName(), getName()));
        TextView content = (TextView) convertedView.findViewById(R.id.textview_status_text);
        content.setTextSize(textSize);
        int colorNormal = Themes.getStyledColor(activity, theme, R.attr.color_status_text_normal, 0);
        content.setTextColor(colorNormal);
        String rawText = getText();
        if(isReadMorseEnabled((MainActivity) activity) && Morse.isMorse(rawText))
        {
            content.setText(String.format("%s\n(%s)", rawText, Morse.morseToJa(rawText)));
        }
        else
        {
            content.setText(rawText);
        }
        TextView footer = (TextView) convertedView.findViewById(R.id.textview_status_footer);
        footer.setTextSize(textSize - 2);
        int colorFooter = Themes.getStyledColor(activity, theme, R.attr.color_status_text_footer, 0);
        footer.setTextColor(colorFooter);
        footer.setText(getFooterText());
        ImageView favorited = (ImageView) convertedView.findViewById(R.id.imageview_status_favorited);
        favorited.setVisibility(isFavorited() ? View.VISIBLE : View.GONE);
        if(isRetweet())
        {
            int colorBgRetweet = Themes.getStyledColor(activity, theme, R.attr.color_status_bg_retweet, 0);
            convertedView.setBackgroundColor(colorBgRetweet);
        }
        else if(isMention())
        {
            int colorBgMention = Themes.getStyledColor(activity, theme, R.attr.color_status_bg_mention, 0);
            convertedView.setBackgroundColor(colorBgMention);
        }
        else
        {
            int colorBgNormal = Themes.getStyledColor(activity, theme, R.attr.color_status_bg_normal, 0);
            convertedView.setBackgroundColor(colorBgNormal);
        }
        convertedView.setOnClickListener(new ListItemClickListener(activity, new Runnable()
        {
            @Override
            public void run()
            {
                onClick(activity);
            }
        }));
        final ViewGroup embeddedStatus = (ViewGroup) convertedView.findViewById(R.id.view_status_embedded_status);
        embeddedStatus.removeAllViews();
        if(extendStatusURL)
        {
            if(containsStatusURL())
            {
                embeddedStatus.setVisibility(View.VISIBLE);
                final Account account = ((MainActivity) activity).getCurrentAccount();
                List<Long> embeddedStatusIDs = getEmbeddedStatusIDs();
                for(int i = 0; i < embeddedStatusIDs.size(); i++)
                {
                    long id = embeddedStatusIDs.get(i);
                    final int index = i;
                    final View finalConvertedView = convertedView;
                    TwitterUtils.tryGetStatus(account, id, new TwitterUtils.StatusCallback()
                    {
                        @Override
                        public void success(Status status)
                        {
                            StatusViewModel viewModel = new StatusViewModel(status, account);
                            View embeddedHolder = viewModel.getView(activity, inflater, null, false);
                            embeddedStatus.addView(embeddedHolder);
                            finalConvertedView.invalidate();
                        }

                        @Override
                        public void error()
                        {

                        }
                    });
                }

            }
        }
        else
        {
            embeddedStatus.setVisibility(View.GONE);
        }
        return convertedView;
    }

    public boolean isMention(String screenName)
    {
        if(mentions == null)
        {
            return false;
        }
        for(UserMentionEntity mention : mentions)
        {
            if(mention.getScreenName().equals(screenName))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isMyStatus(long userID)
    {
        return this.userID == userID;
    }

    public boolean isRetweetOfMe(long userID)
    {
        return retweetedStatus != null && retweetedStatus.getUserID() == userID;
    }

    private boolean containsStatusURL()
    {
        return getEmbeddedStatusIDs().size() > 0;
    }

    private boolean isReadMorseEnabled(MainActivity activity)
    {
        return activity.getUserPreferenceHelper().getValue(R.string.key_setting_read_morse, true);
    }

    private void onClick(Activity activity)
    {
        StatusDetailDialogFragment fragment = new StatusDetailDialogFragment();
        fragment.setStatusID(getID());
        DialogHelper.showDialog(activity, fragment, STATUS_DIALOG);
    }

    private void onIconClick(Activity activity)
    {
        DialogHelper.close(activity, STATUS_DIALOG);
        UserDetailDialogFragment dialogFragment = new UserDetailDialogFragment();
        dialogFragment.setUserID(isRetweet() ? getRetweetedStatus().userID : userID);
        DialogHelper.showDialog(activity, dialogFragment);
    }
}
