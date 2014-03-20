

package net.lacolaco.smileessence.preference;

import android.test.AndroidTestCase;
import net.lacolaco.smileessence.R;

public class PreferenceHelperTest extends AndroidTestCase
{

    private Pref pref;

    @Override
    public void setUp() throws Exception
    {
        pref = new Pref(getContext());
    }

    public void testNotFound() throws Exception
    {
        assertEquals("", pref.getValue(0, ""));
    }

    public void testUseDefault() throws Exception
    {
        assertNotSame(0, pref.getValue(R.string.key_text_size, 10));
    }

    public void testPutAndGet() throws Exception
    {
        assertEquals(true, pref.putValue(R.string.key_test_preference, 10));
        assertEquals(10, pref.getValue(R.string.key_test_preference, 0));
    }
}
