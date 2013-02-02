package net.miz_hi.smileessence.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.dialog.UserMenuAdapter;
import net.miz_hi.smileessence.listener.StatusOnClickListener;
import net.miz_hi.smileessence.status.IconCaches;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserActivity extends Activity
{

	private User user;
	private TextView screennameView;
	private TextView nameView;
	private TextView homepageView;
	private TextView locateView;
	private TextView isFollowingView;
	private TextView isFollowedView;
	private TextView bioView;
	private TextView tweetView;
	private TextView followingView;
	private TextView followedView;
	private TextView favoriteView;
	private ImageView iconView;
	private LinearLayout statusList;
	
	private UserMenuAdapter userMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_layout);

		user = TwitterManager.getUser(Client.getMainAccount(), getIntent().getStringExtra("name"));
		if (user == null)
			finish();		

		userMenu = new UserMenuAdapter(this, user.getScreenName());
		
		screennameView = (TextView) findViewById(R.id.user_screenname);
		nameView = (TextView) findViewById(R.id.user_name);
		homepageView = (TextView) findViewById(R.id.user_homepage);
		locateView = (TextView) findViewById(R.id.user_locate);
		isFollowingView = (TextView) findViewById(R.id.user_isfollowing);
		isFollowedView = (TextView) findViewById(R.id.user_isfollowed);
		bioView = (TextView) findViewById(R.id.user_bio);
		tweetView = (TextView) findViewById(R.id.user_count_tweet);
		followingView = (TextView) findViewById(R.id.user_count_following);
		followedView = (TextView) findViewById(R.id.user_count_followed);
		favoriteView = (TextView) findViewById(R.id.user_count_favorite);
		iconView = (ImageView) findViewById(R.id.user_icon);
		statusList = (LinearLayout) findViewById(R.id.user_status);

		screennameView.setText(user.getScreenName());
		nameView.setText(user.getName() != null ? user.getName() : "No Name");
		homepageView.setText(user.getURL() != null ? user.getURL().toString() : "No Homepage");
		locateView.setText(user.getLocation() != null ? user.getLocation() : "No Location");
		isFollowingView.setText(TwitterManager.isFollowing(Client.getMainAccount(), user.getId()) ? "フォローしています" : (user.getScreenName().equals(Client.getMainAccount().getScreenName()) ? "あなたです" : "フォローしていません"));
		isFollowedView.setText(TwitterManager.isFollowed(Client.getMainAccount(), user.getId()) ? "フォローされています" : (user.getScreenName().equals(Client.getMainAccount().getScreenName()) ? "あなたです" : "フォローされていません"));
		bioView.setText(user.getDescription() != null ? user.getDescription() : "No Description");
		tweetView.setText(Integer.toString(user.getStatusesCount()));
		followingView.setText(Integer.toString(user.getFriendsCount()));
		followedView.setText(Integer.toString(user.getFollowersCount()));
		favoriteView.setText(Integer.toString(user.getFavouritesCount()));
		IconCaches.setIconBitmapToView(user, iconView);

		final Handler handler = new Handler();
		final LinkedList<View> stack = new LinkedList<View>();

		new Thread(new Runnable()
		{
			public void run()
			{
				ArrayList<Status> list = new ArrayList<Status>();
				try
				{
					list.addAll(TwitterManager.getTwitter(Client.getMainAccount()).getUserTimeline(user.getId()));
					for (Status status : list)
					{
						StatusStore.put(status);
						if(status.isRetweet())
						{
							StatusStore.put(status.getRetweetedStatus());
						}
						StatusModel model = StatusModel.createInstance(status);
						View view = StatusViewFactory.getView(getLayoutInflater(), model);
						view.setOnClickListener(new StatusOnClickListener(UserActivity.this, model));
						stack.offer(view);
					}
					handler.post(new Runnable()
					{
						public void run()
						{
							statusList.removeAllViews();
							while (!stack.isEmpty())
							{
								statusList.addView(stack.poll());
							}
							statusList.postInvalidate();
						}
					});
				}
				catch (TwitterException e)
				{
				}
			}
		}).start();
	}

	public User getUser()
	{
		return user;
	}

	public Object fetch(String address) throws MalformedURLException, IOException
	{
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			if(userMenu.isShowing())
			{
				userMenu.dispose();
			}
			else
			{
				userMenu.createMenuDialog(true).show();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
