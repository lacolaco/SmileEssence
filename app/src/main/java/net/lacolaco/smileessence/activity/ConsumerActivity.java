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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.twitter.Consumer;
import net.lacolaco.smileessence.twitter.OAuthSession;

import twitter4j.HttpClient;
import twitter4j.HttpClientFactory;
import twitter4j.HttpParameter;
import twitter4j.HttpResponse;
import twitter4j.HttpResponseEvent;
import twitter4j.HttpResponseListener;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

public class ConsumerActivity extends Activity implements View.OnClickListener, TextWatcher {

    private EditText idEditText;
    private Button fetchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_consumer);

        idEditText = (EditText) findViewById(R.id.editText_consumer_uuid);
        idEditText.addTextChangedListener(this);
        fetchButton = (Button) findViewById(R.id.button_consumer_fetch);
        fetchButton.setOnClickListener(this);
        fetchButton.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_consumer_fetch: {
                AsyncTask<String, Void, Consumer> task = new AsyncTask<String, Void, Consumer>() {
                    @Override
                    protected Consumer doInBackground(String... params) {
                        HttpParameter[] reqParams = HttpParameter.getParameterArray("id", params[0]);

                        try {
                            HttpResponse resp = HttpClientFactory.getInstance()
                                    .get("https://ses-cloud.appspot.com/api/consumer", reqParams, null, null);
                            int statusCode = resp.getStatusCode();
                            if (200 <= statusCode && statusCode <= 299) {
                                String consumerKey, consumerSecret;
                                try {
                                    JSONObject json = resp.asJSONObject();
                                    consumerKey = json.getString("consumerKey");
                                    consumerSecret = json.getString("consumerSecret");
                                    return new Consumer(consumerKey, consumerSecret);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Consumer consumer) {
                        super.onPostExecute(consumer);
                        if(consumer != null) {
                            Intent intent = new Intent();
                            intent.putExtra("consumerKey", consumer.key);
                            intent.putExtra("consumerSecret", consumer.secret);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Notificator.publish(ConsumerActivity.this, "取得に失敗しました");
                        }
                    }
                };
                task.execute(idEditText.getText().toString());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        fetchButton.setEnabled(s.length() != 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}