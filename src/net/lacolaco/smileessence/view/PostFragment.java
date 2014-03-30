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

package net.lacolaco.smileessence.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.notification.Notificator;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.BitmapFileTask;
import net.lacolaco.smileessence.view.adapter.PostState;

public class PostFragment extends Fragment implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener, PostState.OnPostStateChangeListener
{

    private EditText editText;
    private TextView textViewCount;
    private Button buttonTweet;
    private LinearLayout viewGroupReply;
    private ViewGroup viewGroupMedia;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity)getActivity();
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(activity);
        PostState state = PostState.getState();

        View v = inflater.inflate(R.layout.fragment_post, null);
        buttonTweet = (Button)v.findViewById(R.id.post_tweet_button);
        buttonTweet.setOnClickListener(this);
        editText = (EditText)v.findViewById(R.id.post_edit_text);
        textViewCount = (TextView)v.findViewById(R.id.post_text_count);
        int textSize = preferenceHelper.getValue(R.string.key_setting_text_size, 10);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        editText.setText(state.getText());
        editText.setTextSize(textSize + 4);
        editText.setMovementMethod(new ArrowKeyMovementMethod()
        {
            @Override
            protected boolean right(TextView widget, Spannable buffer)
            {
                //Don't back to Home
                return widget.getSelectionEnd() == widget.length() || super.right(widget, buffer);
            }
        });
        ImageButton imageButtonDeleteText = (ImageButton)v.findViewById(R.id.post_delete);
        imageButtonDeleteText.setOnClickListener(this);
        ImageButton imageButtonMedia = (ImageButton)v.findViewById(R.id.post_media);
        imageButtonMedia.setOnClickListener(this);
        ImageButton imageButtonMenu = (ImageButton)v.findViewById(R.id.post_menu);
        imageButtonMenu.setOnClickListener(this);
        //Reply view
        viewGroupReply = (LinearLayout)v.findViewById(R.id.post_inreplyto_parent);
        TextView textViewReply = (TextView)viewGroupReply.findViewById(R.id.post_reply_text);
        ImageButton imageButtonDeleteReply = (ImageButton)viewGroupReply.findViewById(R.id.post_reply_delete);
        imageButtonDeleteReply.setOnClickListener(this);
        if(TextUtils.isEmpty(state.getInReplyToScreenName()))
        {
            viewGroupReply.setVisibility(View.GONE);
        }
        else
        {
            viewGroupReply.setVisibility(View.VISIBLE);
            textViewReply.setText(String.format("%s: %s", state.getInReplyToScreenName(), state.getInReplyToText()));
        }
        //Media view
        viewGroupMedia = (LinearLayout)v.findViewById(R.id.post_media_parent);
        ImageView imageViewMedia = (ImageView)viewGroupMedia.findViewById(R.id.post_media_image);
        ImageButton imageButtonDeleteMedia = (ImageButton)viewGroupMedia.findViewById(R.id.post_media_delete);
        imageViewMedia.setOnClickListener(this);
        imageButtonDeleteMedia.setOnClickListener(this);
        if(TextUtils.isEmpty(state.getMediaFilePath()))
        {
            viewGroupMedia.setVisibility(View.GONE);
        }
        else
        {
            viewGroupMedia.setVisibility(View.VISIBLE);
            new BitmapFileTask(state.getMediaFilePath(), imageViewMedia).execute();
        }
        state.setListener(this);
        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroy();
        PostState.getState().removeListener();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        int remainingCount = 140 - TwitterUtils.getFixedTextLength(s.toString());
        textViewCount.setText(String.valueOf(remainingCount));
        if(remainingCount == 140)
        {
            textViewCount.setTextColor(getResources().getColor(R.color.red));
            buttonTweet.setEnabled(false);
        }
        else if(remainingCount < 0)
        {
            textViewCount.setTextColor(getResources().getColor(R.color.red));
            buttonTweet.setEnabled(false);
        }
        else
        {
            textViewCount.setTextAppearance(getActivity(), android.R.style.TextAppearance_Widget_TextView);
            buttonTweet.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(hasFocus)
        {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, 0);
        }
        else
        {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            PostState.getState().beginTransaction().setText(editText.getText().toString()).commit();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.post_delete:
            {
                editText.setText("");
                break;
            }
            case R.id.post_media:
            {
                //TODO media dialog
                new Notificator(getActivity(), "Media").publish();
                break;
            }
            case R.id.post_menu:
            {
                //TODO post menu
                new Notificator(getActivity(), "Menu").publish();
                break;
            }
            case R.id.post_tweet_button:
            {
                //TODO tweet
                new Notificator(getActivity(), "Tweet").publish();
                break;
            }
            case R.id.post_reply_delete:
            {
                viewGroupReply.setVisibility(View.GONE);
                break;
            }
            case R.id.post_media_delete:
            {
                viewGroupMedia.setVisibility(View.GONE);
                break;
            }
            case R.id.post_media_image:
            {
                //TODO intent for image
                new Notificator(getActivity(), "Image").publish();
                break;
            }
        }
    }

    @Override
    public void onPostStateChange(PostState postState)
    {
        if(editText != null)
        {
            editText.setText(postState.getText());
        }
        if(viewGroupReply != null)
        {
            TextView textViewReply = (TextView)viewGroupReply.findViewById(R.id.post_reply_text);
            if(TextUtils.isEmpty(postState.getInReplyToScreenName()))
            {
                viewGroupReply.setVisibility(View.GONE);
            }
            else
            {
                viewGroupReply.setVisibility(View.VISIBLE);
                textViewReply.setText(String.format("%s: %s", postState.getInReplyToScreenName(), postState.getInReplyToText()));
            }
        }
        if(viewGroupMedia != null)
        {
            ImageView imageViewMedia = (ImageView)viewGroupMedia.findViewById(R.id.post_media_image);
            if(TextUtils.isEmpty(postState.getMediaFilePath()))
            {
                viewGroupMedia.setVisibility(View.GONE);
            }
            else
            {
                viewGroupMedia.setVisibility(View.VISIBLE);
                new BitmapFileTask(postState.getMediaFilePath(), imageViewMedia).execute();
            }
        }
        if(postState.isRequestedToOpen())
        {
            ((MainActivity)getActivity()).setSelectedPageIndex(MainActivity.PAGE_POST);
        }
    }
}
