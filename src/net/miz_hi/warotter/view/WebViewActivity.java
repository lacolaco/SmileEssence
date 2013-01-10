package net.miz_hi.warotter.view;

import net.miz_hi.warotter.model.Warotter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity
{

	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		WebView webView = new WebView(this);
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
				if (url.startsWith(Warotter.CALLBACK_OAUTH))
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
