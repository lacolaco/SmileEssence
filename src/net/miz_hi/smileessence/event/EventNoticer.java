package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.message.EventMessage;
import net.miz_hi.smileessence.message.ToastMessage;
import net.miz_hi.smileessence.util.CountUpInteger;
import net.miz_hi.smileessence.viewmodel.MainActivityViewModel;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class EventNoticer
{

	private static Toast toast;
	private static long lastUserId = -1;
	private static long lastStatusId = -1;
	private static CountUpInteger counterSourceUser = new CountUpInteger(5);
	private static CountUpInteger counterTargetStatus = new CountUpInteger(5);
	
	public static void receive(EventModel event)
	{
		MainActivityViewModel.singleton().messenger.raise("event", new EventMessage(event));
	}
	
	public static void noticeEvent(final EventHandlerActivity activity, final EventModel model)
	{
		if(lastUserId == model.source.getId())
		{
			if(counterSourceUser.isOver())
			{
				return;
			}
			else if(counterSourceUser.countUp())
			{
				activity.runOnUiThread(new Runnable()
				{
					public void run()
					{
						toast.cancel();
						Toast.makeText(activity, "ステルスモードON", Toast.LENGTH_LONG).show();
					}
				});
				return;
			}
		}
		else
		{
			lastUserId = model.source.getId();
			counterSourceUser.reset();
		}
		
		if(lastStatusId == model.targetModel.statusId)
		{
			if(counterTargetStatus.isOver())
			{
				return;
			}
			else if(counterTargetStatus.countUp())
			{
				activity.runOnUiThread(new Runnable()
				{
					public void run()
					{
						toast.cancel();
						Toast.makeText(activity, "ステルスモードON", Toast.LENGTH_LONG).show();
					}
				});
				return;
			}
		}
		else
		{
			lastStatusId = model.targetModel.statusId;
			counterTargetStatus.reset();
		}
		
		activity.runOnUiThread(new Runnable()
		{
			public void run()
			{
				if(toast == null)
				{
					toast = new Toast(activity);
				}
				View v = EventViewFactory.getView(activity, model);
				toast.setView(v);
				toast.setGravity(Gravity.BOTTOM, 0, 80);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}
}
