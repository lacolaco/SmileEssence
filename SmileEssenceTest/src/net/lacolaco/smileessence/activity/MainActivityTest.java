package net.lacolaco.smileessence.activity;

import android.test.ActivityInstrumentationTestCase2;
import net.lacolaco.smileessence.notification.Notificator;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class net.lacolaco.smileessence.activity.MainActivityTest \
 * net.lacolaco.smileessence.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    public MainActivityTest()
    {
        super(MainActivity.class);
    }

    public void testTitle() throws Exception
    {
        assertEquals("SmileEssence", getActivity().getTitle());
    }

    public void testNotification() throws Exception
    {

        final Notificator notificator = new Notificator(getActivity(), "Test Notice");
        assertNotNull(notificator.makeCrouton());
        assertNotNull(notificator.makeToast());
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Notificator.startNotification();
                notificator.publish();
                Notificator.stopNotification();
                notificator.publish();
            }
        });

    }
}
