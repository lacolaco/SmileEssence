package net.miz_hi.warotter.auth;

import net.miz_hi.warotter.auth.Consumers.Consumer;
import net.miz_hi.warotter.model.Account;
import net.miz_hi.warotter.util.EnumRequestCode;
import net.miz_hi.warotter.view.WebViewActivity;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class AuthorizeHelper
{
	private Activity activity;
	private Consumer consumer;
	private Twitter twitter;
	private RequestToken req;

	public static final String CALLBACK_OAUTH = "oauth://warotter";
	public static final String OAUTH_VERIFIER = "oauth_verifier";

	public AuthorizeHelper(Activity activity, Consumer consumer)
	{
		this.activity = activity;
		this.consumer = consumer;
	}

	public void oauthSend()
	{
		try
		{
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumer.key, consumer.secret);
			req = twitter.getOAuthRequestToken(CALLBACK_OAUTH);
			Intent intent = new Intent(activity, WebViewActivity.class);
			intent.setData(Uri.parse(req.getAuthorizationURL()));
			activity.startActivityForResult(intent, EnumRequestCode.AUTHORIZE.ordinal());
		}
		catch (Exception e)
		{

		}
	}

	public Account oauthRecieve(Uri uri)
	{
		Account account = null;
		try
		{
			String verifier = uri.getQueryParameter(OAUTH_VERIFIER);
			AccessToken accessToken = null;
			accessToken = twitter.getOAuthAccessToken(req, verifier);
			if (accessToken != null)
			{
				account = new Account(accessToken, consumer);
				AuthentificationDB db = AuthentificationDB.instance();
				db.save(account);
			}
		}
		catch (Exception e)
		{
		}
		return account;
	}
}
