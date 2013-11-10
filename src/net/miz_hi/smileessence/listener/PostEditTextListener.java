package net.miz_hi.smileessence.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.util.StringUtils;

public class PostEditTextListener implements TextWatcher, OnFocusChangeListener
{

    TextView viewCount;

    public PostEditTextListener(TextView viewCount)
    {
        this.viewCount = viewCount;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, 0);
        }
        else
        {
            InputMethodManager imm = (InputMethodManager) Client.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void afterTextChanged(Editable arg0)
    {
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
    {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
    {
        viewCount.setText(String.valueOf(140 - StringUtils.countTweetCharacters(arg0.toString())));
    }


}
