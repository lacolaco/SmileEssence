package net.lacolaco.smileessence.resource;

import android.content.Context;

public class ResourceHelper
{

    private Context context;

    public ResourceHelper(Context context)
    {
        this.context = context;
    }

    public String getString(int resID)
    {
        return context.getString(resID);
    }
}
