package net.miz_hi.smileessence.core;

public interface EventSubscriber
{
	public void onEventTriggered(final String eventName, final Message message);
}
