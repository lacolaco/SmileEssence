package net.miz_hi.smileessence.auth;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.auth.Consumers.Consumer;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.view.WebViewActivity;
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
	private ExecutorService executor;

	public static final String CALLBACK_OAUTH = "oauth://smileessence";
	public static final String OAUTH_VERIFIER = "oauth_verifier";

	public AuthorizeHelper(Activity activity, Consumer consumer)
	{
		this.activity = activity;
		this.consumer = consumer;
		executor = Executors.newSingleThreadExecutor();
	}

	public void oauthSend()
	{
		try
		{
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumer.key, consumer.secret);
			Future<RequestToken> f = executor.submit(new Callable<RequestToken>()
			{

				@Override
				public RequestToken call() throws Exception
				{
					return twitter.getOAuthRequestToken(CALLBACK_OAUTH);
				}
			});
			req = f.get();
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
			final String verifier = uri.getQueryParameter(OAUTH_VERIFIER);
			AccessToken accessToken = null;
			Future<AccessToken> f = executor.submit(new Callable<AccessToken>()
			{

				@Override
				public AccessToken call() throws Exception
				{
					return twitter.getOAuthAccessToken(req, verifier);
				}
			});
			accessToken = f.get();
			if (accessToken != null)
			{
				account = new Account(accessToken, consumer);
				AuthentificationDB db = AuthentificationDB.instance();
				db.save(account);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return account;
	}
}
