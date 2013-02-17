package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.menu.TweetMenuMorse;
import net.miz_hi.smileessence.menu.TweetMenuWarota;
import net.miz_hi.smileessence.menu.UserMenuFollow;
import net.miz_hi.smileessence.menu.UserMenuOpenFavstar;
import net.miz_hi.smileessence.menu.UserMenuOpenPage;
import net.miz_hi.smileessence.menu.UserMenuRemove;
import net.miz_hi.smileessence.menu.UserMenuReply;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

public class TweetMenuAdapter extends DialogAdapter
{
	private TweetViewManager manager;

	public TweetMenuAdapter(Activity activity, TweetViewManager manager)
	{
		super(activity);
		this.manager = manager;
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{
		if (init)
		{
			list.clear();
			list.add(new TweetMenuWarota(activity, this, manager));
			list.add(new TweetMenuMorse(activity, this, manager));
		}

		return super.createMenuDialog("ÉÅÉjÉÖÅ[");
	}

}
