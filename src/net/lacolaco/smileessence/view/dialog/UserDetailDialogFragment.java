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
import android.widget.*;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.command.CommandOpenURL;
import net.lacolaco.smileessence.data.ImageCache;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.FollowTask;
import net.lacolaco.smileessence.twitter.task.ShowFriendshipTask;
import net.lacolaco.smileessence.twitter.task.UnfollowTask;
import net.lacolaco.smileessence.twitter.task.UserTimelineTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.Themes;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;
import net.lacolaco.smileessence.view.adapter.StatusListAdapter;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.User;

public class UserDetailDialogFragment extends DialogFragment implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener2<ListView>
{

    // ------------------------------ FIELDS ------------------------------

    public static final String USER_MENU_DIALOG = "userMenuDialog";
    private static final String KEY_USER_ID = "userID";
    private static final int ADAPTER_INDEX = 100;
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
    private PullToRefreshListView listViewTimeline;
    private TabHost tabHost;

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

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnClickListener ---------------------

    @Override
    public void onClick(final View v)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account account = activity.getCurrentAccount();
        TwitterUtils.tryGetUser(account, getUserID(), new TwitterUtils.UserCallback()
        {
            @Override
            public void success(final User user)
            {
                switch(v.getId())
                {
                    case R.id.imageview_user_detail_menu:
                    {
                        openUserMenu(activity, user);
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
                        ConfirmDialogFragment.show(activity, getString(R.string.dialog_confirm_commands), new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                toggleFollowing(user, account, activity);
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void error()
            {
                dismiss();
            }
        });
    }

    // --------------------- Interface OnRefreshListener2 ---------------------

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final StatusListAdapter adapter = getListAdapter(activity);
        Paging paging = TwitterUtils.getPaging(TwitterUtils.getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setSinceId(adapter.getTopID());
        }
        new UserTimelineTask(twitter, getUserID(), paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(int i = statuses.length - 1; i >= 0; i--)
                {
                    twitter4j.Status status = statuses[i];
                    adapter.addToTop(new StatusViewModel(status, currentAccount));
                }
                updateListView(refreshView.getRefreshableView(), adapter, true);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView)
    {
        final MainActivity activity = (MainActivity) getActivity();
        final Account currentAccount = activity.getCurrentAccount();
        Twitter twitter = TwitterApi.getTwitter(currentAccount);
        final StatusListAdapter adapter = getListAdapter(activity);
        Paging paging = TwitterUtils.getPaging(TwitterUtils.getPagingCount(activity));
        if(adapter.getCount() > 0)
        {
            paging.setMaxId(adapter.getLastID() - 1);
        }
        new UserTimelineTask(twitter, getUserID(), paging)
        {
            @Override
            protected void onPostExecute(twitter4j.Status[] statuses)
            {
                super.onPostExecute(statuses);
                for(twitter4j.Status status : statuses)
                {
                    adapter.addToBottom(new StatusViewModel(status, currentAccount));
                }
                updateListView(refreshView.getRefreshableView(), adapter, false);
                refreshView.onRefreshComplete();
            }
        }.execute();
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity) getActivity();
        View v = activity.getLayoutInflater().inflate(R.layout.dialog_user_detail, null);
        View menu = v.findViewById(R.id.imageview_user_detail_menu);
        menu.setOnClickListener(this);
        textViewScreenName = (TextView) v.findViewById(R.id.textview_user_detail_screenname);
        textViewScreenName.setOnClickListener(this);
        textViewName = (TextView) v.findViewById(R.id.textview_user_detail_name);
        textViewURL = (TextView) v.findViewById(R.id.textview_user_detail_url);
        textViewLocate = (TextView) v.findViewById(R.id.textview_user_detail_locate);
        textViewFollowed = (TextView) v.findViewById(R.id.textview_user_detail_followed);
        textViewProtected = (TextView) v.findViewById(R.id.texttview_user_detail_protected);
        textViewDescription = (TextView) v.findViewById(R.id.textview_user_detail_description);
        textViewTweetCount = (TextView) v.findViewById(R.id.textview_user_detail_tweet_count);
        textViewTweetCount.setOnClickListener(this);
        textViewFriendCount = (TextView) v.findViewById(R.id.textview_user_detail_friend_count);
        textViewFriendCount.setOnClickListener(this);
        textViewFollowerCount = (TextView) v.findViewById(R.id.textview_user_detail_follower_count);
        textViewFollowerCount.setOnClickListener(this);
        textViewFavoriteCount = (TextView) v.findViewById(R.id.textview_user_detail_favorite_count);
        textViewFavoriteCount.setOnClickListener(this);
        imageViewIcon = (NetworkImageView) v.findViewById(R.id.imageview_user_detail_icon);
        imageViewIcon.setOnClickListener(this);
        imageViewHeader = (NetworkImageView) v.findViewById(R.id.imageview_user_detail_header);
        buttonFollow = (Button) v.findViewById(R.id.button_user_detail_follow);
        buttonFollow.setOnClickListener(this);
        listViewTimeline = (PullToRefreshListView) v.findViewById(R.id.listview_user_detail_timeline);

        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1").setContent(R.id.tab1).setIndicator(getString(R.string.user_detail_tab_info));
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2").setContent(R.id.tab2).setIndicator(getString(R.string.user_detail_tab_timeline));
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);

        final Account account = activity.getCurrentAccount();
        TwitterUtils.tryGetUser(account, getUserID(), new TwitterUtils.UserCallback()
        {
            @Override
            public void success(User user)
            {
                try
                {
                    initUserData(user, account);
                }
                catch(Exception e)
                {
                    Logger.error(e);
                    error();
                }
            }

            @Override
            public void error()
            {
                dismiss();
            }
        });
        return new AlertDialog.Builder(activity)
                .setView(v)
                .setCancelable(true)
                .create();
    }

    private void executeUserTimelineTask(final User user, final Account account, final StatusListAdapter adapter)
    {
        tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.GONE);
        Twitter twitter = TwitterApi.getTwitter(account);
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
                tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private String getHtmlDescription(String description)
    {
        if(TextUtils.isEmpty(description))
        {
            return "";
        }
        String html = description;
        html = html.replaceAll("https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+", "<a href=\"$0\">$0</a>");
        html = html.replaceAll("@([a-zA-Z0-9_]+)", "<a href=\"" + TwitterUtils.getUserHomeURL("$1") + "\">$0</a>");
        html = html.replaceAll("\r\n", "<br />");
        return html;
    }

    private StatusListAdapter getListAdapter(MainActivity activity)
    {
        return (StatusListAdapter) activity.getListAdapter(ADAPTER_INDEX);
    }

    private void initUserData(User user, final Account account)
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
        MainActivity activity = (MainActivity) getActivity();
        final StatusListAdapter adapter = new StatusListAdapter(activity);
        listViewTimeline.setAdapter(adapter);
        listViewTimeline.setOnRefreshListener(this);
        activity.setListAdapter(ADAPTER_INDEX, adapter);
        executeUserTimelineTask(user, account, adapter);
        updateRelationship(activity, user.getId());
    }

    private void lockFollowButton(Activity activity)
    {
        buttonFollow.setText(R.string.user_detail_loading);
        buttonFollow.setBackground(activity.getResources().getDrawable(R.drawable.button_round_gray));
        buttonFollow.setEnabled(false);
    }

    private void openUrl(String url)
    {
        new CommandOpenURL(getActivity(), url).execute();
    }

    private void openUserMenu(final MainActivity activity, final User user)
    {
        UserMenuDialogFragment menuFragment = new UserMenuDialogFragment()
        {
            @Override
            protected void executeCommand(Command command)
            {
                super.executeCommand(command);
                new UIHandler()
                {
                    @Override
                    public void run()
                    {
                        if(UserDetailDialogFragment.this.isDetached())
                        {
                            return;
                        }
                        updateRelationship(activity, user.getId());
                    }
                }.postDelayed(1000);
            }
        };
        menuFragment.setUserID(user.getId());
        DialogHelper.showDialog(activity, menuFragment, USER_MENU_DIALOG);
    }

    private void setFollowButtonState(boolean isFollowing, Drawable unfollowColor, Drawable followColor)
    {
        buttonFollow.setText(isFollowing ? R.string.user_detail_unfollow : R.string.user_detail_follow);
        buttonFollow.setBackground(isFollowing ? unfollowColor : followColor);
        buttonFollow.setTag(isFollowing);
        buttonFollow.setEnabled(true);
    }

    private void toggleFollowing(final User user, final Account account, final Activity activity)
    {
        lockFollowButton(activity);
        Boolean isFollowing = buttonFollow.getTag() != null ? (Boolean) buttonFollow.getTag() : false;
        Twitter twitter = new TwitterApi(account).getTwitter();
        if(isFollowing)
        {
            new UnfollowTask(twitter, user.getId(), activity)
            {
                @Override
                public void onPostExecute(User result)
                {
                    super.onPostExecute(result);
                    updateRelationship(activity, user.getId());
                    buttonFollow.setEnabled(true);
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
                    updateRelationship(activity, user.getId());
                    buttonFollow.setEnabled(true);
                }
            }.execute();
        }
    }

    protected void updateListView(AbsListView absListView, CustomListAdapter<?> adapter, boolean addedToTop)
    {
        int before = adapter.getCount();
        adapter.notifyDataSetChanged(); // synchronized call (not adapter#updateForce())
        int after = adapter.getCount();
        int increments = after - before;
        if(increments > 0)
        {
            adapter.setNotifiable(false);
            if(addedToTop)
            {
                absListView.setSelection(increments + 1);
                absListView.smoothScrollToPositionFromTop(increments, 0);
                absListView.setSelection(increments);
            }
            else
            {
                absListView.smoothScrollToPositionFromTop(before, 0);
            }

            if(increments == 1)
            {
                adapter.setNotifiable(true);
            }
        }
        else
        {
            adapter.setNotifiable(true);
        }
    }

    private void updateRelationship(Activity activity, final long userId)
    {
        MainActivity mainActivity = (MainActivity) activity;
        Account account = mainActivity.getCurrentAccount();
        Twitter twitter = new TwitterApi(account).getTwitter();
        if(userId == account.userID)
        {
            textViewFollowed.setText(R.string.user_detail_followed_is_me);
            buttonFollow.setVisibility(View.GONE);
        }
        else
        {
            int theme = mainActivity.getThemeIndex();
            lockFollowButton(activity);
            textViewFollowed.setText(R.string.user_detail_loading);
            final Drawable red = Themes.getStyledDrawable(activity, theme, R.attr.button_round_red);
            final Drawable blue = Themes.getStyledDrawable(activity, theme, R.attr.button_round_blue);
            new ShowFriendshipTask(twitter, userId)
            {
                @Override
                protected void onPostExecute(Relationship relationship)
                {
                    if(relationship != null)
                    {
                        boolean isFollowing = relationship.isSourceFollowingTarget();
                        boolean isFollowed = relationship.isSourceFollowedByTarget();
                        setFollowButtonState(isFollowing, red, blue);
                        textViewFollowed.setText(isFollowed ? R.string.user_detail_followed : R.string.user_detail_not_followed);
                    }
                }
            }.execute();
        }
    }
}
