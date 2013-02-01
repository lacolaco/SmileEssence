package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.util.CountUpInteger;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class EventNoticer
{
	
	private Activity _activity;
	private Toast _toast;
	private long _lastUserId = -1;
	private long _lastStatusId = -1;
	private CountUpInteger _counterSourceUser = new CountUpInteger(5);
	private CountUpInteger _counterTargetStatus = new CountUpInteger(5);
	
	public EventNoticer(Activity activity)
	{
		_activity = activity;
	}
	
	public static void receive(EventModel event)
	{
		MainActivity.getInstance().eventNotify(event);
	}
	
	public void noticeEvent(final EventModel model)
	{
		if(_lastUserId == model.source.getId())
		{
			if(_counterSourceUser.isOver())
			{
				return;
			}
			else if(_counterSourceUser.countUp())
			{
				_activity.runOnUiThread(new Runnable()
				{
					public void run()
					{
						_toast.cancel();
						Toast.makeText(_activity, "ステルスモードON", Toast.LENGTH_LONG).show();
					}
				});
				return;
			}
		}
		else
		{
			_lastUserId = model.source.getId();
			_counterSourceUser.reset();
		}
		
		if(_lastStatusId == model.targetModel.statusId)
		{
			if(_counterTargetStatus.isOver())
			{
				return;
			}
			else if(_counterTargetStatus.countUp())
			{
				_activity.runOnUiThread(new Runnable()
				{
					public void run()
					{
						_toast.cancel();
						Toast.makeText(_activity, "ステルスモードON", Toast.LENGTH_LONG).show();
					}
				});
				return;
			}
		}
		else
		{
			_lastStatusId = model.targetModel.statusId;
			_counterTargetStatus.reset();
		}
		
		_activity.runOnUiThread(new Runnable()
		{
			public void run()
			{
				if(_toast == null)
				{
					_toast = new Toast(_activity);
				}
				View v = EventViewFactory.getView(_activity, model);
				_toast.setView(v);
				_toast.setGravity(Gravity.BOTTOM, 0, 80);
				_toast.setDuration(Toast.LENGTH_LONG);
				_toast.show();
			}
		});
	}
}
