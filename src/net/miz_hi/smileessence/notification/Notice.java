package net.miz_hi.smileessence.notification;

import de.keyboardsurfer.android.widget.crouton.Style;

public class Notice
{

    String text;
    Style style;

    public Notice(String text)
    {
        this.text = text;
        this.style = Style.INFO;
    }

    public Notice setStyle(Style style)
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
        Notificator.crouton(this);
    }

    public static Notice getNullEvent()
    {
        return new NullEvent();
    }

    public static class NullEvent extends Notice
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
