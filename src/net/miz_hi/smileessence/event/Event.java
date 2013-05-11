package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.core.Notifier;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Event
{

	String text;
	Style style;

	public Event(String text)
	{
		this.text = text;
		this.style = Style.INFO;
	}
	
	public Event setStyle(Style style)
	{
		this.style = style;
		return this;
	}	

	public String getText()
	{
		return text;
	}

	public Style getStyle()
	{
		return style;
	}

	public void raise()
	{
		Notifier.crouton(this);
	}
	
	public static Event getNullEvent()
	{
		return new NullEvent();
	}
	
	public static class NullEvent extends Event
	{
		
		private NullEvent()
		{
			super("");
		}
		
		@Override
		public void raise()
		{
			//Null Operation
		}
	}
}
