package net.miz_hi.smileessence.command.status.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.dialog.ReviewDialog;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandReview extends StatusCommand implements IHideable
{

    private Activity activity;

    public StatusCommandReview(Activity activity, TweetModel model)
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
                if (which == DialogInterface.BUTTON_POSITIVE)
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
                    builder.append(status.getOriginal().user.screenName);
                    builder.append(" ( http://twitter.com/");
                    builder.append(status.getOriginal().user.screenName);
                    builder.append("/status/");
                    builder.append(status.getOriginal().statusId);
                    builder.append(" )");

                    StatusUpdate update = new StatusUpdate(builder.toString());
                    update.setInReplyToStatusId(status.getOriginal().statusId);
                    new TweetTask(update).callAsync();
                    status.getOriginal().favorite();
                }
            }
        });
        reviewDialog.create().show();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return !status.getOriginal().user.isProtected && Client.getPermission().canWarotaRT();
    }


}