package net.miz_hi.smileessence.command;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.ReviewDialogHelper;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class StatusCommandReview extends StatusCommand implements IHideable
{

	private Activity activity;

	public StatusCommandReview(Activity activity, StatusModel model)
	{
		super(model);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "レビューを書く";
	}

	@Override
	public void workOnUiThread()
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
				builder.append(status.screenName);
				builder.append("/status/");
				builder.append(status.statusId);
				builder.append(" )");

				Future<Boolean> f1 = MyExecutor.submit(new AsyncFavoriteTask(status.statusId));
				Future<Boolean> f2 = MyExecutor.submit(new AsyncTweetTask(new StatusUpdate(builder.toString())));
				try
				{
					if (f1.get() && f2.get())
					{
						ToastManager.toast(TwitterManager.MESSAGE_TWEET_SUCCESS);
					}
					else
					{
						ToastManager.toast(TwitterManager.MESSAGE_SOMETHING_ERROR);
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

	@Override
	public boolean getDefaultVisibility()
	{
		return !status.user.isProtected && Client.getPermission().canWarotaRT();
	}
	
	
}
