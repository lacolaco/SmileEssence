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

package net.lacolaco.smileessence.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.FollowTask;
import net.lacolaco.smileessence.twitter.task.ShowFriendshipTask;
import net.lacolaco.smileessence.twitter.task.UnfollowTask;
import net.lacolaco.smileessence.twitter.task.UserTimelineTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.view.adapter.StatusListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.User;

public class UserDetailDialogFragment extends DialogFragment implements View.OnClickListener
{

    // ------------------------------ FIELDS ------------------------------

    private static final String KEY_USER_ID = "userID";
    private TextView textViewScreenName;
    private TextView textViewName;
    private TextView textViewURL;
    private TextView textViewLocate;
    private TextView textViewFollowed;
    private TextView textViewProtected;
    private TextView textViewDescription;
    private TextView textViewTweetCount;
    private TextView textViewFriendCount;
    private TextView textViewFollowerCount;
    private TextView textViewFavoriteCount;
    private NetworkImageView imageViewIcon;
    private NetworkImageView imageViewHeader;
    private Button buttonFollow;
    private ListView listViewTimeline;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public long getUserID()
    {
        return getArguments().getLong(KEY_USER_ID);
    }

    public void setUserID(long userID)
    {
        Bundle args = new Bundle();
        args.putLong(KEY_USER_ID, userID);
        setArguments(args);
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity)getActivity();
        Account account = activity.getCurrentAccount();
        User user = TwitterUtils.tryGetUser(account, getUserID());

        View v = activity.getLayoutInflater().inflate(R.layout.dialog_user_detail, null);
        View menu = v.findViewById(R.id.imageview_user_detail_menu);
        menu.setOnClickListener(this);
        textViewScreenName = (TextView)v.findViewById(R.id.textview_user_detail_screenname);
        textViewScreenName.setOnClickListener(this);
        textViewName = (TextView)v.findViewById(R.id.textview_user_detail_name);
        textViewURL = (TextView)v.findViewById(R.id.textview_user_detail_url);
        textViewLocate = (TextView)v.findViewById(R.id.textview_user_detail_locate);
        textViewFollowed = (TextView)v.findViewById(R.id.textview_user_detail_followed);
        textViewProtected = (TextView)v.findViewById(R.id.texttview_user_detail_protected);
        textViewDescription = (TextView)v.findViewById(R.id.textview_user_detail_description);
        textViewTweetCount = (TextView)v.findViewById(R.id.textview_user_detail_tweet_count);
        textViewTweetCount.setOnClickListener(this);
        textViewFriendCount = (TextView)v.findViewById(R.id.textview_user_detail_friend_count);
        textViewFriendCount.setOnClickListener(this);
        textViewFollowerCount = (TextView)v.findViewById(R.id.textview_user_detail_follower_count);
        textViewFollowerCount.setOnClickListener(this);
        textViewFavoriteCount = (TextView)v.findViewById(R.id.textview_user_detail_favorite_count);
        textViewFavoriteCount.setOnClickListener(this);
        imageViewIcon = (NetworkImageView)v.findViewById(R.id.imageview_user_detail_icon);
        imageViewIcon.setOnClickListener(this);
        imageViewHeader = (NetworkImageView)v.findViewById(R.id.imageview_user_detail_header);
        buttonFollow = (Button)v.findViewById(R.id.button_user_detail_follow);
        buttonFollow.setOnClickListener(this);
        listViewTimeline = (ListView)v.findViewById(R.id.listview_user_detail_timeline);
        setUserData(user, account);
        return new AlertDialog.Builder(activity)
                .setView(v)
                .setCancelable(true)
                .create();
    }

    private void setUserData(User user, final Account account)
    {
        textViewName.setText(user.getName());
        textViewScreenName.setText(user.getScreenName());
        if(TextUtils.isEmpty(user.getLocation()))
        {
            textViewLocate.setVisibility(View.GONE);
        }
        else
        {
            textViewLocate.setText(user.getLocation());
        }
        if(TextUtils.isEmpty(user.getURL()))
        {
            textViewURL.setVisibility(View.GONE);
        }
        else
        {
            textViewURL.setText(user.getURL());
        }
        textViewTweetCount.setText(String.valueOf(user.getStatusesCount()));
        textViewFriendCount.setText(String.valueOf(user.getFriendsCount()));
        textViewFollowerCount.setText(String.valueOf(user.getFollowersCount()));
        textViewFavoriteCount.setText(String.valueOf(user.getFavouritesCount()));
        textViewProtected.setVisibility(user.isProtected() ? View.VISIBLE : View.GONE);
        String htmlDescription = getHtmlDescription(user.getDescription());
        textViewDescription.setText(Html.fromHtml(htmlDescription));
        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
        ImageCache.getInstance().setImageToView(user.getBiggerProfileImageURL(), imageViewIcon);
        ImageCache.getInstance().setImageToView(user.getProfileBannerURL(), imageViewHeader);
        final StatusListAdapter adapter = new StatusListAdapter(getActivity());
        listViewTimeline.setAdapter(adapter);

        Twitter twitter = new TwitterApi(account).getTwitter();
        if(user.getId() == account.userID)
        {
            textViewFollowed.setText(R.string.user_detail_followed_is_me);
            buttonFollow.setVisibility(View.GONE);
        }
        else
        {
            int theme = ((MainActivity)getActivity()).getThemeIndex();
            final Drawable blue = Themes.getStyledDrawable(getActivity(), theme, R.attr.button_round_blue);
            final Drawable red = Themes.getStyledDrawable(getActivity(), theme, R.attr.button_round_red);

            new ShowFriendshipTask(twitter, user.getId())
            {
                @Override
                protected void onPostExecute(Relationship relationship)
                {
                    if(relationship != null)
                    {
                        boolean isFollowing = relationship.isSourceFollowingTarget();
                        buttonFollow.setText(isFollowing ? R.string.user_detail_unfollow : R.string.user_detail_follow);
                        buttonFollow.setBackground(isFollowing ? red : blue);
                        buttonFollow.setTag(isFollowing);
                        textViewFollowed.setText(relationship.isSourceFollowedByTarget() ? R.string.user_detail_followed : R.string.user_detail_not_followed);
                    }
                }
            }.execute();
            buttonFollow.setText(R.string.user_detail_loading);
            textViewFollowed.setText(R.string.user_detail_loading);
        }
        new UserTimelineTask(twitter, user.getId())
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(twitter4j.Status status : statuses)
                {
                    adapter.addToBottom(new StatusViewModel(status, account));
                }
                adapter.updateForce();
            }
        }.execute();
    }

    private String getHtmlDescription(String description)
    {
        String html = description;
        html = html.replaceAll("https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+", "<a href=\"$0\">$0</a>");
        html = html.replaceAll("@([a-zA-Z0-9_]+)", "<a href=\"" + TwitterUtils.getUserHomeURL("$1") + "\">$0</a>");
        html = html.replaceAll("\r\n", "<br />");
        return html;
    }

    @Override
    public void onClick(View v)
    {
        MainActivity activity = (MainActivity)getActivity();
        final Account account = activity.getCurrentAccount();
        User user = TwitterUtils.tryGetUser(account, getUserID());
        switch(v.getId())
        {
            case R.id.imageview_user_detail_menu:
            {
                UserMenuDialogFragment menuFragment = new UserMenuDialogFragment();
                menuFragment.setUserID(user.getId());
                new DialogHelper(activity, menuFragment).show();
                break;
            }
            case R.id.imageview_user_detail_icon:
            {
                openUrl(user.getBiggerProfileImageURLHttps());
                break;
            }
            case R.id.textview_user_detail_screenname:
            {
                openUrl(TwitterUtils.getUserHomeURL(user.getScreenName()));
                break;
            }
            case R.id.textview_user_detail_tweet_count:
            {
                openUrl(TwitterUtils.getUserHomeURL(user.getScreenName()));
                break;
            }
            case R.id.textview_user_detail_friend_count:
            {
                openUrl(String.format("%s/following", TwitterUtils.getUserHomeURL(user.getScreenName())));
                break;
            }
            case R.id.textview_user_detail_follower_count:
            {
                openUrl(String.format("%s/followers", TwitterUtils.getUserHomeURL(user.getScreenName())));
                break;
            }
            case R.id.textview_user_detail_favorite_count:
            {
                openUrl(String.format("%s/favorites", TwitterUtils.getUserHomeURL(user.getScreenName())));
                break;
            }
            case R.id.button_user_detail_follow:
            {
                toggleFollowing(user, account, activity);
                break;
            }
        }
    }

    private void openUrl(String url)
    {
        new CommandOpenURL(getActivity(), url).execute();
    }

    private void toggleFollowing(User user, final Account account, Activity activity)
    {
        buttonFollow.setText(R.string.user_detail_loading);
        buttonFollow.setBackground(activity.getResources().getDrawable(R.drawable.button_round_gray));
        Boolean isFollowing = buttonFollow.getTag() != null ? (Boolean)buttonFollow.getTag() : false;
        Twitter twitter = new TwitterApi(account).getTwitter();
        if(isFollowing)
        {
            new UnfollowTask(twitter, user.getId(), activity)
            {
                @Override
                public void onPostExecute(User result)
                {
                    super.onPostExecute(result);
                    setUserData(result, account);
                }
            }.execute();
        }
        else
        {
            new FollowTask(twitter, user.getId(), activity)
            {
                @Override
                public void onPostExecute(User result)
                {
                    super.onPostExecute(result);
                    setUserData(result, account);
                }
            }.execute();
        }
    }
}
