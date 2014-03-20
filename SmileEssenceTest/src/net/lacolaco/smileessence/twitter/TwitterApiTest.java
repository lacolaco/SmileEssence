package net.lacolaco.smileessence.twitter;

import junit.framework.TestCase;

public class TwitterApiTest extends TestCase
{

    private static final String CONSUMER_KEY = "SIt6h4O6qmBB2URSKsF1Q";
    private static final String CONSUMER_SECRET = "Uil1dyrqiodLLqXIB6B0rVwVxFfFCxTf8ggAcszWc";
    private static final String TOKEN = "498602690-IF2Ht1Q4yn2FfDitTQsuP0LMUKN5rzPCWpHBy72t";
    private static final String TOKEN_SECRET = "xSukt39B6f8DZXVyagCgZsaaml8NObyIQIdtYiFqsI";
    private static final String SCREEN_NAME = "laco0416";
    private static final long USER_ID = 498602690;
    private TwitterApi api;

    @Override
    public void setUp() throws Exception
    {
        api = new TwitterApi(TOKEN, TOKEN_SECRET);
    }

    public void testReadProperties() throws Exception
    {
        assertEquals(CONSUMER_KEY, api.getTwitter().getConfiguration().getOAuthConsumerKey());
        assertEquals(CONSUMER_SECRET, api.getTwitter().getConfiguration().getOAuthConsumerSecret());
    }

    public void testAuthenticate() throws Exception
    {
        assertEquals(SCREEN_NAME, api.getTwitter().getScreenName());
    }

    public void testAccessApi() throws Exception
    {
        assertNotNull(api.getTwitter().users().showUser(USER_ID));
    }
}
