package net.lacolaco.smileessence.resource;

import android.test.AndroidTestCase;
import net.lacolaco.smileessence.R;

public class ResourceHelperTest extends AndroidTestCase
{

    public void testReadResource() throws Exception
    {
        ResourceHelper pref = new ResourceHelper(getContext());
        assertNotNull(pref.getString(R.string.app_name));
    }
}
