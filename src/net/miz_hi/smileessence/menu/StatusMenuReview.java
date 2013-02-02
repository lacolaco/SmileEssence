package net.miz_hi.smileessence.menu;

import twitter4j.StatusUpdate;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.activity.SettingActivity;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.ConcurrentAsyncTaskHelper;
import net.miz_hi.smileessence.core.EnumPreferenceKey;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.dialog.ReviewDialogHelper;
import net.miz_hi.smileessence.dialog.SeekBarDialogHelper;
import net.miz_hi.smileessence.status.StatusModel;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class StatusMenuReview extends StatusMenuItemBase
{

	public StatusMenuReview(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "レビューを書く";
	}

	@Override
	public void work()
	{
		final ReviewDialogHelper helper = new ReviewDialogHelper(_activity, "ツイートを評価しよう");
		helper.setSeekBarMax(4);
		helper.setSeekBarStart(0);
		helper.setLevelCorrect(1);
		helper.setOnClickListener(new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				int star = helper.getProgress() + 1;
				StringBuilder builder = new StringBuilder();
				for(int i = 0; i < 5; i++)
				{
					if(i < star)
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
				builder.append(_model.screenName);
				builder.append("/status/");
				builder.append(_model.statusId);
				builder.append(" )");
				ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncTweetTask(new StatusUpdate(builder.toString())));
				ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncFavoriteTask(_model.statusId));
			}
		});
		helper.createSeekBarDialog().show();
	}

}
