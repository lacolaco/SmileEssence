package net.miz_hi.smileessence.core;

import net.miz_hi.smileessence.event.Event;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.IAttackEvent;
import net.miz_hi.smileessence.event.StatusEventModel;
import net.miz_hi.smileessence.util.CountUpInteger;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.MainActivity;
import twitter4j.User;
import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Notifier
{
	
	private static User enemyUser;
	private static long lastUserId = -1;
	private static long lastStatusId = -1;
	private static CountUpInteger counterSourceUser = new CountUpInteger(5);
	private static CountUpInteger counterTargetStatus = new CountUpInteger(5);
	
	public static void toast(final String text)
	{
		final Activity activity = MainActivity.getInstance();
		if(activity == null || activity.isFinishing())
		{
			return;
		}
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
			}
		}.post();
	}
	
	public static void info(String text)
	{
		crouton(new Event(text));
	}
	
	public static void alert(String text)
	{
		crouton(new Event(text).setStyle(Style.ALERT));
	}
	
	private static Style getStyle(Style type)
	{
		Style.Builder builder = new Style.Builder();
		builder.setHeight(70).setDuration(800).setGravity(Gravity.CENTER);
		if(type == Style.INFO)
		{
			builder.setBackgroundColorValue(Style.holoBlueLight);
		}
		else if(type == Style.ALERT)
		{
			builder.setBackgroundColorValue(Style.holoRedLight);
		}
		return builder.build();
	}
	
	public static void crouton(final Event event)
	{
		final Activity activity = MainActivity.getInstance();
		if(activity == null || activity.isFinishing())
		{
			return;
		}
		
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Crouton.showText(activity, event.getText(), getStyle(event.getStyle()));
			}
		}.post();
	}

	public static Event buildEvent(final EventModel model)
	{
		if(model instanceof StatusEventModel)
		{
			StatusEventModel se = (StatusEventModel)model;
			if(se instanceof IAttackEvent)
			{				
				if(lastUserId != se.source.userId)
				{
					counterSourceUser.reset();
					lastUserId = se.source.userId;
				}
				else
				{
					if (counterSourceUser.isOver())
					{
						return Event.getNullEvent();
					}
					
					if(counterSourceUser.countUp())
					{
						return new Event(se.source.screenName + "から攻撃を受けています");
					}
				}
				
				if(lastStatusId != se.targetModel.statusId)
				{
					counterTargetStatus.reset();
					lastStatusId = se.targetModel.statusId;
				}
				else
				{
					if(counterTargetStatus.isOver())
					{
						return Event.getNullEvent();
					}
					if(counterTargetStatus.countUp())
					{
						return new Event("あなたのツイートが攻撃を受けています");
					}
				}
			}
		}
		
		return new Event(model.getHeaderText());
	}
}
