package net.miz_hi.smileessence.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.miz_hi.smileessence.message.EventMessage;
import net.miz_hi.smileessence.viewmodel.MainActivityViewModel;


public class EventHelper
{

	public static void receive(EventModel event)
	{
		MainActivityViewModel.singleton().messenger.raise("event", new EventMessage(event));
	}
}
