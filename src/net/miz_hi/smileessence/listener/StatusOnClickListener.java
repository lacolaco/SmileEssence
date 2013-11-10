package net.miz_hi.smileessence.listener;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.menu.EventMenu;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.menu.UserMenu;
import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.status.event.EventModel;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.util.UiHandler;

public class StatusOnClickListener implements OnClickListener
{

    private Activity activity;
    private IStatusModel model;

    public StatusOnClickListener(Activity activity, IStatusModel model)
    {
        this.activity = activity;
        this.model = model;
    }

    @Override
    public void onClick(final View v)
    {
        final int bg = ((ColorDrawable) v.getBackground()).getColor();
        v.setBackgroundColor(Client.getColor(R.color.MetroBlue));
        v.invalidate();
        new UiHandler()
        {

            @Override
            public void run()
            {
                v.setBackgroundColor(bg);
                Dialog dialog = getDialog();
                if (dialog != null)
                {
                    dialog.show();
                }
            }
        }.postDelayed(20);
    }

    private Dialog getDialog()
    {
        if (model instanceof TweetModel)
        {
            return new TweetMenu(activity, (TweetModel) model).create();
        }
        else if (model instanceof EventModel)
        {
            return new EventMenu(activity, (EventModel) model).create();
        }
        else if (model instanceof UserModel)
        {
            return new UserMenu(activity, (UserModel) model).create();
        }
        else
        {
            return null;
        }
    }


}
