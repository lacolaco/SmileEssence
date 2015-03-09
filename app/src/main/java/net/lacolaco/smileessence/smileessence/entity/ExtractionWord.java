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

package net.lacolaco.smileessence.entity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.viewmodel.IViewModel;

import java.util.List;

@Table(name = "Extraction")
public class ExtractionWord extends Model implements IViewModel
{

    // ------------------------------ FIELDS ------------------------------

    @Column(name = "Text", notNull = true)
    public String text;

    // -------------------------- STATIC METHODS --------------------------

    public ExtractionWord()
    {
        super();
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public ExtractionWord(String text)
    {
        super();
        this.text = text;
    }

    public static List<ExtractionWord> getAll()
    {
        return new Select().from(ExtractionWord.class).execute();
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface IViewModel ---------------------

    @Override
    public View getView(Activity activity, LayoutInflater inflater, View convertedView)
    {
        if(convertedView == null)
        {
            convertedView = inflater.inflate(R.layout.menu_item_simple_text, null);
        }
        TextView textView = (TextView) convertedView.findViewById(R.id.textView_menuItem_simple);
        textView.setText(this.text);
        return convertedView;
    }
}
