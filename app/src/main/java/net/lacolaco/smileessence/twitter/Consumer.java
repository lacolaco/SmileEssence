package net.lacolaco.smileessence.twitter;

import net.lacolaco.smileessence.preference.AppPreferenceHelper;

public class Consumer {

    public String key;
    public String secret;

    public Consumer(String key, String secret) {
        this.key = key;
        this.secret = secret;
    }

    public static Consumer load(AppPreferenceHelper pref) {
        String key = pref.getValue("consumerKey", "");
        String secret = pref.getValue("consumerSecret", "");
        if (key.equals("") || secret.equals("")) {
            return null;
        }
        return new Consumer(key, secret);
    }

    public static void save(AppPreferenceHelper pref, String key, String secret) {
        pref.putValue("consumerKey", key);
        pref.putValue("consumerSecret", secret);
    }

    public static void clear(AppPreferenceHelper pref) {
        pref.putValue("consumerKey", "");
        pref.putValue("consumerSecret", "");
    }
}
