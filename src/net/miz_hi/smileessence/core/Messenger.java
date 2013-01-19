package net.miz_hi.smileessence.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import android.content.Context;

public class Messenger
{
	private static WeakHashMap<Context, Messenger> instances = new WeakHashMap<Context, Messenger>();

	public static Messenger getInstance(Context context)
	{
		if (!instances.containsKey(context))
		{
			instances.put(context, new Messenger());
		}
		return instances.get(context);
	}

	public static void removeRefs(Context context)
	{
		if (context == null)
			return;
		if (instances.containsKey(context))
			instances.remove(context);
	}

	private HashMap<String, ArrayList<EventSubscriber>> EventNameSubscribersMap = new HashMap<String, ArrayList<EventSubscriber>>();

	public void raise(final String eventName, final Message message)
	{
		ArrayList<EventSubscriber> subscribers = EventNameSubscribersMap.get(eventName);

		// Nobody subscribes this
		if (subscribers == null)
			return;

		int count = subscribers.size();

		// All Subscribers removed (maybe GCed)
		if (count == 0)
		{
			subscribers.remove(eventName);
			return;
		}

		for (int i = 0; i < count; i++)
		{
			EventSubscriber subscriber = subscribers.get(i);
			if (subscriber != null)
				subscriber.onEventTriggered(eventName, message);
		}
	}

	public void subscribe(final String eventName, final EventSubscriber subscriber)
	{
		ArrayList<EventSubscriber> subscribers = EventNameSubscribersMap.get(eventName);
		if (subscribers == null)
		{
			subscribers = new ArrayList<EventSubscriber>();
			EventNameSubscribersMap.put(eventName, subscribers);
		}

		subscribers.add(subscriber);
	}
}
