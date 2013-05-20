package net.miz_hi.smileessence.view.activity;

import net.miz_hi.smileessence.Client;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity
{

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		WebView webView = new WebView(this);

		CookieSyncManager.createInstance(this).resetSync();
		setContentView(webView, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		Uri uri = getIntent().getData();
		if (uri == null)
			finish();
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				if (url.startsWith(Client.CALLBACK_OAUTH))
				{
					Intent intent = new Intent();
					intent.setData(Uri.parse(url));
					setResult(RESULT_OK, intent);
					finish();
				}
			}

		});

		webView.loadUrl(uri.toString());
	}
}
