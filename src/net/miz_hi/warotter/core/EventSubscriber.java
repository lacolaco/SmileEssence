package net.miz_hi.warotter.core;

public interface EventSubscriber
{
	public void onEventTriggered(final String eventName, final Message message);
}
