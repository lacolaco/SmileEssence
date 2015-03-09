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

package net.lacolaco.smileessence.viewmodel;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import net.lacolaco.smileessence.R;

public class EditableCheckBoxModel implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    private final String text;
    private boolean freezing;
    private String inputText;
    private boolean checked;

    // --------------------------- CONSTRUCTORS ---------------------------

    public EditableCheckBoxModel(String text)
    {
        this.text = text;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public String getInputText()
    {
        return inputText;
    }

    public EditableCheckBoxModel setInputText(String inputText)
    {
        this.inputText = inputText;
        return this;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public EditableCheckBoxModel setChecked(boolean checked)
    {
        this.checked = checked;
        return this;
    }

    public boolean isFreezing()
    {
        return freezing;
    }

    public EditableCheckBoxModel setFreezing(boolean freezing)
    {
        this.freezing = freezing;
        return this;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface IViewModel ---------------------

    @Override
    public View getView(Activity activity, LayoutInflater inflater, View convertedView)
    {
        if(convertedView == null)
        {
            convertedView = inflater.inflate(R.layout.menu_item_editable_checkbox, null);
        }
        CheckBox checkBox = (CheckBox) convertedView.findViewById(R.id.checkBox_menuItem);
        checkBox.setText(this.text);
        checkBox.setChecked(checked);
        checkBox.setEnabled(!isFreezing());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                EditableCheckBoxModel.this.setChecked(isChecked);
            }
        });
        EditText editText = (EditText) convertedView.findViewById(R.id.edittext_editable_checkbox);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                EditableCheckBoxModel.this.setInputText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        return convertedView;
    }
}
