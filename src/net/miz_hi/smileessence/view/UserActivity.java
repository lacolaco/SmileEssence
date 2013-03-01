package net.miz_hi.smileessence.view;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.AsyncTimelineGetter;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.dialog.UserMenuAdapter;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.StringUtils;
import twitter4j.Paging;
import twitter4j.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
	private TextView isProtectedView;
	private TextView descriptionView;
	private TextView tweetView;
	private TextView followingView;
	private TextView followedView;
	private TextView favoriteView;
	private ImageView iconView;
	private LinearLayout statusesView;

	private UserMenuAdapter userMenu;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.useractivity_layout);

		UserModel model = UserStore.get(getIntent().getLongExtra("user_id", -1));
		if (model == null)
		{
			finish();
		}

		user = model.getUser();
		model.updateData(user);

		userMenu = new UserMenuAdapter(this, model);

		screennameView = (TextView) findViewById(R.id.user_screenname);
		nameView = (TextView) findViewById(R.id.user_name);
		homepageView = (TextView) findViewById(R.id.user_homepage);
		locateView = (TextView) findViewById(R.id.user_locate);
		isFollowingView = (TextView) findViewById(R.id.user_isfollowing);
		isFollowedView = (TextView) findViewById(R.id.user_isfollowed);
		isProtectedView = (TextView) findViewById(R.id.user_isprotected);
		descriptionView = (TextView) findViewById(R.id.user_bio);
		tweetView = (TextView) findViewById(R.id.user_count_tweet);
		followingView = (TextView) findViewById(R.id.user_count_following);
		followedView = (TextView) findViewById(R.id.user_count_followed);
		favoriteView = (TextView) findViewById(R.id.user_count_favorite);
		iconView = (ImageView) findViewById(R.id.user_icon);
		statusesView = (LinearLayout) findViewById(R.id.user_status);

		screennameView.setText(model.screenName);
		nameView.setText(model.name);
		if (StringUtils.isNullOrEmpty(model.homePageUrl))
		{
			homepageView.setVisibility(View.GONE);
		}
		else
		{
			homepageView.setText(model.homePageUrl);
		}
		if(StringUtils.isNullOrEmpty(model.location))
		{
			locateView.setVisibility(View.GONE);
		}
		else
		{
			locateView.setText(model.location);
		}
		isFollowingView.setText(model.isFriend() ? "フォローしています" : model.isMe() ? "あなたです" : "フォローしていません");
		isFollowedView.setText(model.isFollower() ? "フォローされています" : model.isMe() ? "あなたです" : "フォローされていません");
		isProtectedView.setText(model.isProtected ? "非公開" : "公開");
		descriptionView.setText(model.description);
		tweetView.setText(Integer.toString(model.statusCount));
		followingView.setText(Integer.toString(model.friendCount));
		followedView.setText(Integer.toString(model.followerCount));
		favoriteView.setText(Integer.toString(model.favoriteCount));
		IconCaches.setIconBitmapToView(model, iconView);

		TextView emptyText = new TextView(this);
		emptyText.setText("Now Loading...");
		statusesView.addView(emptyText);

		final Future<List<StatusModel>> resp = MyExecutor.submit(new AsyncTimelineGetter(Client.getMainAccount(), user.getId(), new Paging(1)));
		MyExecutor.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final LinkedList<StatusModel> list = new LinkedList<StatusModel>(resp.get());

					new UiHandler()
					{

						@Override
						public void run()
						{
							statusesView.removeAllViews();
							while (!list.isEmpty())
							{
								statusesView.addView(StatusViewFactory.getView(getLayoutInflater(), list.poll()));
							}
							statusesView.postInvalidate();
						}
					}.post();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public User getUser()
	{
		return user;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			if (userMenu.isShowing())
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
