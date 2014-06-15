/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.OAuthSession;
import twitter4j.auth.AccessToken;

public class OAuthActivity extends Activity implements View.OnClickListener, TextWatcher
{

    public static final int PIN_CODE_LENGTH = 7;
    private TextView linkTextView;
    private EditText pinEditText;
    private Button authButton;
    private OAuthSession oauthSession;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_oauth);

        linkTextView = (TextView) findViewById(R.id.textView_oauth_link);
        pinEditText = (EditText) findViewById(R.id.editText_oauth_pin);
        pinEditText.addTextChangedListener(this);
        authButton = (Button) findViewById(R.id.button_oauth_auth);
        authButton.setOnClickListener(this);
        authButton.setEnabled(false);

        oauthSession = new OAuthSession();
        String url = oauthSession.getAuthorizationURL();
        if(!TextUtils.isEmpty(url))
        {
            linkTextView.setText(url);
        }
        else
        {
            new Notificator(this, R.string.notice_error_authenticate_request).makeToast().show();
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.button_oauth_auth:
            {
                AccessToken accessToken = oauthSession.getAccessToken(pinEditText.getText().toString());
                if(accessToken != null)
                {
                    Intent intent = new Intent();
                    intent.putExtra(OAuthSession.KEY_TOKEN, accessToken.getToken());
                    intent.putExtra(OAuthSession.KEY_TOKEN_SECRET, accessToken.getTokenSecret());
                    intent.putExtra(OAuthSession.KEY_USER_ID, accessToken.getUserId());
                    intent.putExtra(OAuthSession.KEY_SCREEN_NAME, accessToken.getScreenName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        authButton.setEnabled(s.length() == PIN_CODE_LENGTH);
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }
}