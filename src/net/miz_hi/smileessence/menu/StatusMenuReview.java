package net.miz_hi.smileessence.menu;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.dialog.ReviewDialogHelper;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class StatusMenuReview extends StatusMenuItemBase
{

	public StatusMenuReview(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return false;
	}

	@Override
	public String getText()
	{
		return "レビューを書く";
	}

	@Override
	public void work()
	{
		final ReviewDialogHelper helper = new ReviewDialogHelper(activity, "ツイートを評価しよう");
		helper.setSeekBarMax(4);
		helper.setSeekBarStart(0);
		helper.setLevelCorrect(1);
		helper.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				int star = helper.getProgress() + 1;
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < 5; i++)
				{
					if (i < star)
					{
						builder.append("★");
					}
					else
					{
						builder.append("☆");
					}
				}
				builder.append("\r\n");
				builder.append("コメント: ");
				builder.append(helper.getText());
				builder.append("\r\n");
				builder.append("( http://twitter.com/");
				builder.append(model.screenName);
				builder.append("/status/");
				builder.append(model.statusId);
				builder.append(" )");

				Future<Boolean> f1 = MyExecutor.submit(new AsyncFavoriteTask(model.statusId));
				Future<Boolean> f2 = MyExecutor.submit(new AsyncTweetTask(new StatusUpdate(builder.toString())));
				try
				{
					if (f1.get() && f2.get())
					{
						Toast.makeText(activity, TwitterManager.MESSAGE_TWEET_SUCCESS, Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(activity, TwitterManager.MESSAGE_SOMETHING_ERROR, Toast.LENGTH_SHORT).show();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		helper.createSeekBarDialog().show();
	}

}
