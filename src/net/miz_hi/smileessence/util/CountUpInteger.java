package net.miz_hi.smileessence.util;

public class CountUpInteger
{

    private int count = 0;
    private int maxCount;

    public CountUpInteger(int maxCount)
    {
        this.maxCount = maxCount;
    }

    public boolean countUp()
    {
        count++;
        return isOver();
    }

    public boolean isOver()
    {
        return count >= maxCount;
    }

    public void reset()
    {
        count = 0;
    }

}
