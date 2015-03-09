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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.twitter.Validator;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.logging.Logger;
import net.lacolaco.smileessence.preference.UserPreferenceHelper;
import net.lacolaco.smileessence.twitter.TwitterApi;
import net.lacolaco.smileessence.twitter.task.TweetTask;
import net.lacolaco.smileessence.twitter.util.TwitterUtils;
import net.lacolaco.smileessence.util.BitmapThumbnailTask;
import net.lacolaco.smileessence.util.IntentUtils;
import net.lacolaco.smileessence.util.UIHandler;
import net.lacolaco.smileessence.view.adapter.PostState;
import net.lacolaco.smileessence.view.dialog.DialogHelper;
import net.lacolaco.smileessence.view.dialog.PostMenuDialogFragment;
import net.lacolaco.smileessence.view.dialog.SelectImageDialogFragment;
import net.lacolaco.smileessence.viewmodel.StatusViewModel;
import twitter4j.Status;
import twitter4j.StatusUpdate;

import java.io.File;

public class PostFragment extends Fragment implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener,
        PostState.OnPostStateChangeListener
{

    // ------------------------------ FIELDS ------------------------------

    private EditText editText;
    private TextView textViewCount;
    private Button buttonTweet;
    private ViewGroup viewGroupReply;
    private ViewGroup viewGroupMedia;

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface OnClickListener ---------------------

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.button_post_delete:
            {
                deletePost();
                break;
            }
            case R.id.button_post_media:
            {
                setImage();
                break;
            }
            case R.id.button_post_menu:
            {
                openPostMenu();
                break;
            }
            case R.id.button_post_tweet:
            {
                submitPost();
                break;
            }
            case R.id.button_post_reply_delete:
            {
                deleteReply();
                break;
            }
            case R.id.button_post_media_delete:
            {
                removeImage();
                break;
            }
            case R.id.image_post_media:
            {
                displayImage();
                break;
            }
        }
    }

    // --------------------- Interface OnFocusChangeListener ---------------------

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(hasFocus)
        {
            showIME();
        }
        else
        {
            hideIME();
        }
    }

    // --------------------- Interface OnPostStateChangeListener ---------------------


    @Override
    public void onPostStateChange(final PostState postState)
    {
        Logger.debug("PostFragment PostStateChange");
        final MainActivity activity = (MainActivity) getActivity();
        if(editText != null)
        {
            final int start = postState.getSelectionStart();
            final int end = postState.getSelectionEnd();
            editText.removeTextChangedListener(this);
            editText.setTextKeepState(postState.getText());
            editText.addTextChangedListener(this);
            updateTextCount(editText.getText());
            new UIHandler()
            {
                @Override
                public void run()
                {
                    editText.setSelection(start, end);
                }
            }.postAtFrontOfQueue();
        }
        if(viewGroupReply != null)
        {
            if(postState.getInReplyToStatusID() >= 0)
            {
                viewGroupReply.setVisibility(View.VISIBLE);
                final Account account = activity.getCurrentAccount();
                TwitterUtils.tryGetStatus(account, postState.getInReplyToStatusID(), new TwitterUtils.StatusCallback()
                {
                    @Override
                    public void success(Status status)
                    {
                        View header = viewGroupReply.findViewById(R.id.layout_post_reply_status);
                        header = new StatusViewModel(status, account).getView(activity, activity.getLayoutInflater(), header);
                        header.setBackgroundColor(getResources().getColor(R.color.transparent));
                        header.setClickable(false);
                    }

                    @Override
                    public void error()
                    {
                        viewGroupReply.setVisibility(View.GONE);
                    }
                });
                ImageButton imageButtonDeleteReply = (ImageButton) viewGroupReply.findViewById(R.id.button_post_reply_delete);
                imageButtonDeleteReply.setOnClickListener(this);
            }
            else
            {
                viewGroupReply.setVisibility(View.GONE);
            }
        }
        if(viewGroupMedia != null)
        {
            ImageView imageViewMedia = (ImageView) viewGroupMedia.findViewById(R.id.image_post_media);
            if(TextUtils.isEmpty(postState.getMediaFilePath()))
            {
                viewGroupMedia.setVisibility(View.GONE);
            }
            else
            {
                viewGroupMedia.setVisibility(View.VISIBLE);

            }
            new BitmapThumbnailTask(activity, postState.getMediaFilePath(), imageViewMedia).execute();
        }
    }

    // --------------------- Interface TextWatcher ---------------------

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        updateTextCount(s);
    }

    public void updateTextCount(CharSequence s)
    {
        int remainingCount = 140 - TwitterUtils.getFixedTextLength(s.toString());
        if(!TextUtils.isEmpty(PostState.getState().getMediaFilePath()))
        {
            remainingCount -= new Validator().getShortUrlLength();
        }
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
        setStateFromView();
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Logger.debug("PostFragment Create");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.actionbar_post);
        showIME();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Logger.debug("PostFragment CreateView");
        MainActivity activity = (MainActivity) getActivity();
        PostState.getState().setListener(this);
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(activity);
        View v = inflater.inflate(R.layout.fragment_post, null);
        buttonTweet = getTweetButton(v);
        buttonTweet.setOnClickListener(this);
        editText = getEditText(v);
        textViewCount = getCountTextView(v);
        int textSize = preferenceHelper.getValue(R.string.key_setting_text_size, 10);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
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
        ImageButton imageButtonDeleteText = (ImageButton) v.findViewById(R.id.button_post_delete);
        imageButtonDeleteText.setOnClickListener(this);
        ImageButton imageButtonMedia = (ImageButton) v.findViewById(R.id.button_post_media);
        imageButtonMedia.setOnClickListener(this);
        ImageButton imageButtonMenu = (ImageButton) v.findViewById(R.id.button_post_menu);
        imageButtonMenu.setOnClickListener(this);
        //Reply view
        viewGroupReply = getReplyViewGroup(v);
        ImageButton imageButtonDeleteReply = (ImageButton) viewGroupReply.findViewById(R.id.button_post_reply_delete);
        imageButtonDeleteReply.setOnClickListener(this);
        //Media view
        viewGroupMedia = getMediaViewGroup(v);
        ImageView imageViewMedia = (ImageView) viewGroupMedia.findViewById(R.id.image_post_media);
        ImageButton imageButtonDeleteMedia = (ImageButton) viewGroupMedia.findViewById(R.id.button_post_media_delete);
        imageViewMedia.setOnClickListener(this);
        imageButtonDeleteMedia.setOnClickListener(this);
        editText.requestFocus();
        return v;
    }

    @Override
    public void onDestroyView()
    {
        Logger.debug("PostFragment DestroyView");
        super.onDestroyView();
        setStateFromView();
        PostState.getState().removeListener();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        Logger.debug("PostFragment ViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        PostState state = PostState.getState();
        onPostStateChange(state);
    }

    private void deletePost()
    {
        editText.setText("");
        PostState.getState().beginTransaction().setText("").setCursor(0).commit();
        deleteReply();
    }

    private void deleteReply()
    {
        viewGroupReply.setVisibility(View.GONE);
        PostState.getState().beginTransaction().setInReplyToStatusID(-1).commit();
    }

    private void displayImage()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(PostState.getState().getMediaFilePath())), "image/*");
        IntentUtils.startActivityIfFound(getActivity(), intent);
    }

    private TextView getCountTextView(View v)
    {
        return (TextView) v.findViewById(R.id.post_text_count);
    }

    private EditText getEditText(View v)
    {
        return (EditText) v.findViewById(R.id.post_edit_text);
    }

    private ViewGroup getMediaViewGroup(View v)
    {
        return (ViewGroup) v.findViewById(R.id.post_media_parent);
    }

    private ViewGroup getReplyViewGroup(View v)
    {
        return (ViewGroup) v.findViewById(R.id.post_inreplyto_parent);
    }

    private Button getTweetButton(View v)
    {
        return (Button) v.findViewById(R.id.button_post_tweet);
    }

    private void hideIME()
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void openPostMenu()
    {
        setStateFromView();
        hideIME();
        PostMenuDialogFragment menuDialogFragment = new PostMenuDialogFragment();
        DialogHelper.showDialog(getActivity(), menuDialogFragment, PostMenuDialogFragment.TAG);
    }

    private void removeImage()
    {
        hideIME();
        viewGroupMedia.setVisibility(View.GONE);
        ((ImageView) viewGroupMedia.findViewById(R.id.image_post_media)).setImageBitmap(null);
        PostState.getState().beginTransaction().setMediaFilePath("").commit();
    }

    private void setImage()
    {
        setStateFromView();
        hideIME();
        SelectImageDialogFragment selectImageDialogFragment = new SelectImageDialogFragment();
        DialogHelper.showDialog(getActivity(), selectImageDialogFragment);
    }

    private void setStateFromView()
    {
        PostState state = PostState.getState();
        state.removeListener();
        state.beginTransaction()
             .setText(editText.getText().toString())
             .setSelection(editText.getSelectionStart(), editText.getSelectionEnd())
             .commit();
        state.setListener(this);
    }

    private void showIME()
    {
        if(editText != null)
        {
            new UIHandler()
            {
                @Override
                public void run()
                {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, 0);
                }
            }.postDelayed(100);
        }
    }

    private void submitPost()
    {
        hideIME();
        setStateFromView();
        PostState state = PostState.getState();
        StatusUpdate statusUpdate = state.toStatusUpdate();
        MainActivity mainActivity = (MainActivity) getActivity();
        TweetTask tweetTask = new TweetTask(TwitterApi.getTwitter(mainActivity.getCurrentAccount()), statusUpdate, state.getMediaFilePath(), mainActivity);
        tweetTask.execute();
        PostState.newState().beginTransaction().commit();
        mainActivity.setSelectedPageIndex(MainActivity.ADAPTER_HOME);
    }
}
