package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.dialog.ReviewDialog;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;
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
		final ReviewDialog reviewDialog = new ReviewDialog(activity, "ツイートを評価しよう");

		reviewDialog.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(which == DialogInterface.BUTTON_POSITIVE)
				{
					int star = reviewDialog.getRates();
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
					builder.append(reviewDialog.getText());
					builder.append("\r\n");
					builder.append("@");
					builder.append(status.screenName);				
					builder.append(" ( http://twitter.com/");
					builder.append(status.screenName);
					builder.append("/status/");
					builder.append(status.statusId);
					builder.append(" )");

					StatusUpdate update = new StatusUpdate(builder.toString());
					update.setInReplyToStatusId(status.statusId);
					Future<Boolean> f1 = MyExecutor.submit(new AsyncFavoriteTask(status.statusId));
					Future<Boolean> f2 = MyExecutor.submit(new AsyncTweetTask(update));
					try
					{
						if (f1.get() && f2.get())
						{
							Notifier.info(TwitterManager.MESSAGE_TWEET_SUCCESS);
						}
						else
						{
							Notifier.alert(TwitterManager.MESSAGE_SOMETHING_ERROR);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		reviewDialog.create().show();
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return !status.user.isProtected && Client.getPermission().canWarotaRT();
	}
	
	
}