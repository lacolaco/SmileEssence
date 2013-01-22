package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.message.TweetMessage;

public class MenuItemReport extends MenuItemBase
{

	public MenuItemReport(EventHandlerActivity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "ìÒ‚ÖƒŒƒ|[ƒg‚ğ‘—‚é";
	}

	@Override
	public void work()
	{
		activity.messenger.raise("tweet", new TweetMessage("#SmileEssence @laco0416 "));
	}

}
